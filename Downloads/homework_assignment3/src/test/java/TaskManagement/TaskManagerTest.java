package TaskManagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {

    private TaskManager manager;

    @BeforeEach
    public void setup() {
        manager = new TaskManager();
    }

    @Test
    public void testCreateAndAssignTask() {
        manager.createTask("DISPENSE", "ZONE A", "PATIENT 1");
        assertTrue(manager.hasPendingTasks());

        Task task = manager.assignNextTask();
        assertEquals("IN_PROGRESS", task.getStatus());
    }

    @Test
    public void testCompleteTask() {
        manager.createTask("RESTOCK", "ZONE B", "STORAGE");
        Task task = manager.assignNextTask();
        manager.completeTask(task);
        assertEquals("COMPLETED", task.getStatus());
    }

    @Test
    public void testFailTask() {
        manager.createTask("RETRIEVE", "ZONE C", "DISPENSARY");
        Task task = manager.assignNextTask();
        manager.failTask(task);
        assertEquals("FAILED", task.getStatus());
    }
}