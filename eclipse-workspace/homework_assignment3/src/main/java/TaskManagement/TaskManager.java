package TaskManagement;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskManager implements TaskAssignable {

    private TaskQueue taskQueue;
    private ExecutorService executor;

    public TaskManager(int parallelTasks) {
        this.taskQueue = new TaskQueue();
        this.executor = Executors.newFixedThreadPool(parallelTasks);
    }

    public TaskQueue getTaskQueue() {
        return taskQueue;
    }

    @Override
    public void createTask(String type, String source, String destination) {
        Task task = new Task(type, source, destination);
        taskQueue.addTask(task);
    }

    @Override
    public void assignTask(Task task) {
        task.setStatus("IN_PROGRESS");
        System.out.println("[TASK] Assigned  " + task);
    }

    @Override
    public void completeTask(Task task) {
        task.setStatus("COMPLETED");
        System.out.println("[TASK] Completed " + task.getId());
    }

    @Override
    public void failTask(Task task) {
        task.setStatus("FAILED");
        System.out.println("[TASK] Failed" + task.getId());
    }

    public Task assignNextTask() {
        if (taskQueue.isEmpty()) {
            return null;
        }
        Task next = taskQueue.getNextTask();
        assignTask(next);
        return next;
    }

    public void processTasks() {
        while (!taskQueue.isEmpty()) {
            Task task = assignNextTask();

            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000); 
                        completeTask(task);
                    } catch (Exception e) {
                        failTask(task);
                    }
                }
            });
        }
    }

    public boolean hasPendingTasks() {
        return !taskQueue.isEmpty();
    }

    public void shutdown() {
        executor.shutdown();
    }
}
