package TaskManagement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskQueueTest {

    @Test
    public void testAddAndRetrieveTask() {
        TaskQueue queue = new TaskQueue();
        Task task = new Task("DISPENSE", "ZONE A", "PATIENT 1");
        queue.addTask(task);

        assertFalse(queue.isEmpty());
        Task retrieved = queue.getNextTask();
        assertEquals(task.getId(), retrieved.getId());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testQueueSize() {
        TaskQueue queue = new TaskQueue();
        queue.addTask(new Task("RESTOCK", "ZONE B", "STORAGE"));
        queue.addTask(new Task("DISPENSE", "ZONE C", "PATIENT 2"));
        assertEquals(2, queue.size());
    }
}