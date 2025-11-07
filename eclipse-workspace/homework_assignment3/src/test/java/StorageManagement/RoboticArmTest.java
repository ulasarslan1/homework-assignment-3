

package StorageManagement;

import static org.junit.jupiter.api.Assertions.*;
import StorageManagement.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Exceptions.StorageException;

class RoboticArmTest {

    private RoboticArm arm;

    @BeforeEach
    void setUp() {
        arm = new RoboticArm();
    }

    @Test
    void testMoveToStorageWhenActive() {
        arm.setActive(true); // ensure arm is active
        assertDoesNotThrow(() -> arm.moveToStorage());
    }

    @Test
    void testMoveToStorageThrowsExceptionIfInactiveManuallySet() {
        arm.setActive(false); // manually deactivate
        StorageException exception = assertThrows(StorageException.class, () -> {
            arm.moveToStorage();
        });
        assertTrue(exception.getMessage().contains("inactive"));
    }

    @Test
    void testActivateSetsActiveTrue() throws StorageException {
        arm.setActive(false); // deactivate first
        arm.activate();
        assertTrue(arm.isActive());
    }

    @Test
    void testDeactivateSetsActiveFalse() throws StorageException {
        arm.deactivate();
        assertFalse(arm.isActive());
    }

    @Test
    void testManualSetActive() {
        arm.setActive(false);
        assertFalse(arm.isActive());

        arm.setActive(true);
        assertTrue(arm.isActive());
    }

    @Test
    void testMoveToStorageAfterActivation() throws StorageException {
        arm.setActive(false);        // start inactive
        arm.activate();              // activate first
        assertDoesNotThrow(() -> arm.moveToStorage());
    }

    @Test
    void testMoveToStorageAfterDeactivationThrowsException() throws StorageException {
        arm.deactivate();            // deactivate
        StorageException exception = assertThrows(StorageException.class, () -> {
            arm.moveToStorage();
        });
        assertTrue(exception.getMessage().contains("inactive"));
    }
}
