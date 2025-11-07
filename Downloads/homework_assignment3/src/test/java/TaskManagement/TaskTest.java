package TaskManagement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void testTaskInitialization() {
        Task task = new Task("DISPENSE", "ZONE A", "PATIENT 1");
        assertEquals("DISPENSE", task.getType());
        assertEquals("PENDING", task.getStatus());
        assertNotNull(task.getCreatedAt());
        assertTrue(task.getId().startsWith("T"));
    }

    @Test
    public void testStatusUpdate() {
        Task task = new Task("RESTOCK", "ZONE B", "STORAGE");
        task.setStatus("IN_PROGRESS");
        assertEquals("IN_PROGRESS", task.getStatus());
    }
}