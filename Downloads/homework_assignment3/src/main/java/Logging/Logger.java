package Logging;

import java.time.LocalDate;
import java.util.List;

public interface Logger {
    void logInfo(String component, String message);
    void logWarning(String component, String message);
    void logError(String component, String message);
    
    void logMedicationAction(String medicationId, String action, int quantity, String storageUnit);
    void logStorageAction(String storageUnit, String action);
    void logRobotAction(String robotId, String action, String location);
    
    
    List<String> getLogsByDate(LocalDate date);
    List<String> searchLogs(String keyword, LocalDate date);
    
    boolean moveLogFile(LocalDate date, String targetDirectory);
    boolean deleteLogFile(LocalDate date);
    boolean archiveLogFile(LocalDate date);
}