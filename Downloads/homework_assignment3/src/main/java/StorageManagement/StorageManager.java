package StorageManagement;

import java.util.ArrayList;
import java.util.List;

public class StorageManager implements StockHandler {

    private final List<StorageLocation> storageLocations;
    private final RoboticArm roboticArm;
    private final Inventory inventory;

    public StorageManager() {
        this.storageLocations = new ArrayList<>();
        this.roboticArm = new RoboticArm();
        this.inventory = new Inventory();
    }

    public void addStorageLocation(StorageLocation location) {
        storageLocations.add(location);
        System.out.println("New storage location added: " + location.getId());
    }

    @Override
    public void addStock(StorageLocation location, int amount) {
        for (int i = 0; i < amount; i++) {
            location.addItem();
        }
        inventory.updateStock(location, amount);
        System.out.println(amount + " items added to location " + location.getId());
    }

    @Override
    public void removeStock(StorageLocation location, int amount) {
        for (int i = 0; i < amount; i++) {
            location.removeItem();
        }
        inventory.updateStock(location, -amount);
        roboticArm.moveToStorage();
        System.out.println(amount + " items removed from location " + location.getId());
    }

    public void manageInventory() {
        System.out.println("Inventory management started...");
        for (StorageLocation location : storageLocations) {
            int count = inventory.countItems(location);
            System.out.println("Inventory at " + location.getId() + ": " + count + " items.");
        }
        System.out.println("Inventory management completed.");
    }

    public List<StorageLocation> getStorageLocations() {
        return storageLocations;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
