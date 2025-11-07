package TaskManagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {

    private TaskManager manager;

    @BeforeEach
    public void setup() {
        manager = new TaskManager(3); // 3 parallel tasks
    }

    @Test
    public void testCreateAndAssignTask() {
        manager.createTask("DISPENSE", "ZONE A", "PATIENT 1");
        assertTrue(manager.hasPendingTasks());

        Task task = manager.assignNextTask();
        assertNotNull(task);
        assertEquals("IN_PROGRESS", task.getStatus());
    }

    @Test
    public void testCompleteTask() {
        manager.createTask("RESTOCK", "ZONE B", "STORAGE");
        Task task = manager.assignNextTask();
        assertNotNull(task);

        manager.completeTask(task);
        assertEquals("COMPLETED", task.getStatus());
    }

    @Test
    public void testFailTask() {
        manager.createTask("RETRIEVE", "ZONE C", "DISPENSARY");
        Task task = manager.assignNextTask();
        assertNotNull(task);

        manager.failTask(task);
        assertEquals("FAILED", task.getStatus());
    }

    @Test
    public void testProcessTasksExecutesAll() throws InterruptedException {
        manager.createTask("A", "A", "A");
        manager.createTask("B", "B", "B");
        manager.createTask("C", "C", "C");

        manager.processTasks();
        Thread.sleep(1500);

        assertFalse(manager.hasPendingTasks(), "All tasks should be moved from the queue");
    }
}
