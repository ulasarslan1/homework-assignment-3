package TaskManagement;



public class TaskManager implements TaskAssignable {
    private TaskQueue taskQueue;
    
    public TaskManager() {
		taskQueue = new TaskQueue();
	}
    
    public TaskQueue getTaskQueue() {return taskQueue;}
		
	@Override
	public void createTask(String type, String source, String destination) {
		Task task = new Task(type, source, destination);
        taskQueue.addTask(task);
        	
	}
    
    @Override
    public void assignTask(Task task) {
        task.setStatus("IN_PROGRESS");
        
    }

    @Override
    public void completeTask(Task task) {
        task.setStatus("COMPLETED");
        
    }

    @Override
    public void failTask(Task task) {
        task.setStatus("FAILED");
        
    }

    //@Override
    public Task assignNextTask() {
        if (taskQueue.isEmpty()) {
            return null; //nothing to assign
        }
        Task next = taskQueue.getNextTask();
        assignTask(next);
        return next; //return the assigned task
    }

    public boolean hasPendingTasks() {
        return !taskQueue.isEmpty();
    }

	

    
   

	
}
