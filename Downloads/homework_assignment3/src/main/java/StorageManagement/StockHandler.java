package StorageManagement;

public interface StockHandler {
    void addStock(StorageLocation location, int amount);
    void removeStock(StorageLocation location, int amount);
}
