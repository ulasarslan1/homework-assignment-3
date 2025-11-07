package StorageManagement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Exceptions.StorageException;
import StorageManagement.*;



class StorageLocationTest {

    private StorageLocation location;

    @BeforeEach
    void setUp() throws StorageException {
        location = new StorageLocation("A1", 3); // capacity 3
    }

    @Test
    void testValidConstructor() throws StorageException {
        StorageLocation loc = new StorageLocation("B1", 5);
        assertEquals("B1", loc.getId());
        assertEquals(5, loc.getCapacity());
        assertEquals(0, loc.getCurrentLoad());
    }

    @Test
    void testConstructorInvalidIdThrowsException() {
        StorageException ex = assertThrows(StorageException.class, () -> {
            new StorageLocation(null, 5);
        });
        assertTrue(ex.getMessage().contains("Failed to create StorageLocation"));

        StorageException ex2 = assertThrows(StorageException.class, () -> {
            new StorageLocation("", 5);
        });
        assertTrue(ex2.getMessage().contains("Failed to create StorageLocation"));
    }

    @Test
    void testConstructorInvalidCapacityThrowsException() {
        StorageException ex = assertThrows(StorageException.class, () -> {
            new StorageLocation("C1", 0);
        });
        assertTrue(ex.getMessage().contains("Failed to create StorageLocation"));

        StorageException ex2 = assertThrows(StorageException.class, () -> {
            new StorageLocation("C2", -5);
        });
        assertTrue(ex2.getMessage().contains("Failed to create StorageLocation"));
    }

    @Test
    void testAddItemIncrementsLoad() throws StorageException {
        location.addItem();
        assertEquals(1, location.getCurrentLoad());
    }

    @Test
    void testAddItemThrowsExceptionWhenFull() throws StorageException {
        location.addItem();
        location.addItem();
        location.addItem();
        StorageException ex = assertThrows(StorageException.class, () -> location.addItem());
        assertFalse(ex.getMessage().contains("StorageLocation is full"));
    }

    @Test
    void testRemoveItemDecrementsLoad() throws StorageException {
        location.addItem();
        location.addItem();
        location.removeItem();
        assertEquals(1, location.getCurrentLoad());
    }

    @Test
    void testRemoveItemThrowsExceptionWhenEmpty() {
        StorageException ex = assertThrows(StorageException.class, () -> location.removeItem());
        assertFalse(ex.getMessage().contains("StorageLocation is empty"));
    }

    @Test
    void testIsFull() throws StorageException {
        assertFalse(location.isFull());
        location.addItem();
        location.addItem();
        location.addItem();
        assertTrue(location.isFull());
    }
}
