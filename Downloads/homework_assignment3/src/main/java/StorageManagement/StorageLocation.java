package StorageManagement;

public class StorageLocation {

    private String id;
    private int capacity;
    private int currentLoad;

    public StorageLocation(String id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.currentLoad = 0;
    }

    public String getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    public boolean isFull() {
        return currentLoad >= capacity;
    }

    public void addItem() {
        if (!isFull()) {
            currentLoad++;
        }
    }

    public void removeItem() {
        if (currentLoad > 0) {
            currentLoad--;
        }
    }
}
