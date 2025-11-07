package StorageManagement;

import java.util.HashMap;
import java.util.Map;

public class Inventory {

    private Map<String, Integer> stockMap;

    public Inventory() {
        this.stockMap = new HashMap<>();
    }

    public void updateStock(StorageLocation location, int change) {
        stockMap.put(location.getId(), getStock(location) + change);
    }

    public int getStock(StorageLocation location) {
        return stockMap.getOrDefault(location.getId(), 0);
    }

    public int countItems(StorageLocation location) {
        return getStock(location);
    }

    public void printInventory() {
        System.out.println("---- INVENTORY STATUS ----");
        stockMap.forEach((id, qty) -> System.out.println("Location " + id + ": " + qty + " items"));
    }
}
