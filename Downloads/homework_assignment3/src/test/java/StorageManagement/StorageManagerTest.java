package StorageManagement;


import StorageManagement.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Exceptions.StorageException;

class StorageManagerTest {

    private StorageManager manager;
    private StorageLocation loc1;
    private StorageLocation loc2;

    @BeforeEach
    void setUp() throws StorageException {
        manager = new StorageManager();
        loc1 = new StorageLocation("A1", 5);
        loc2 = new StorageLocation("B1", 3);

        manager.addStorageLocation(loc1);
        manager.addStorageLocation(loc2);
    }

    @Test
    void testAddStorageLocation() throws StorageException {
        List<StorageLocation> locations = manager.getStorageLocations();
        assertTrue(locations.contains(loc1));
        assertTrue(locations.contains(loc2));
    }

    @Test
    void testAddStockSuccessfully() throws StorageException {
        manager.addStock(loc1, 3);
        assertEquals(3, loc1.getCurrentLoad());
        assertEquals(3, manager.getInventory().getStock(loc1));
    }

    @Test
    void testAddStockInvalidAmountThrowsException() {
        assertThrows(StorageException.class, () -> manager.addStock(loc1, 0));
        assertThrows(StorageException.class, () -> manager.addStock(loc1, -5));
    }

    @Test
    void testAddStockNullLocationThrowsException() {
        assertThrows(StorageException.class, () -> manager.addStock(null, 1));
    }

    @Test
    void testRemoveStockSuccessfully() throws StorageException {
        manager.addStock(loc1, 3);
        manager.removeStock(loc1, 2);
        assertEquals(1, loc1.getCurrentLoad());
        assertEquals(1, manager.getInventory().getStock(loc1));
    }

    @Test
    void testRemoveStockMoreThanAvailableThrowsException() throws StorageException {
        manager.addStock(loc1, 2);
        StorageException ex = assertThrows(StorageException.class, () -> manager.removeStock(loc1, 5));
        assertTrue(ex.getMessage().contains("Failed to remove stock"));
    }

    @Test
    void testRemoveStockInvalidAmountThrowsException() {
        assertThrows(StorageException.class, () -> manager.removeStock(loc1, 0));
        assertThrows(StorageException.class, () -> manager.removeStock(loc1, -2));
    }

    @Test
    void testRemoveStockNullLocationThrowsException() {
        assertThrows(StorageException.class, () -> manager.removeStock(null, 1));
    }

    @Test
    void testManageInventory() throws StorageException {
        manager.addStock(loc1, 2);
        manager.addStock(loc2, 1);
        assertDoesNotThrow(() -> manager.manageInventory());
    }

    @Test
    void testGetInventory() {
        assertNotNull(manager.getInventory());
    }
}
