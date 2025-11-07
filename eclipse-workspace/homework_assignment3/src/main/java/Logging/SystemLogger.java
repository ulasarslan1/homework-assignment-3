package Logging;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SystemLogger implements Logger {
    private String logDirectory;
    private String archiveDirectory;
    
    public SystemLogger(String logDirectory) {
        this.logDirectory = logDirectory;
        this.archiveDirectory = logDirectory + File.separator + "archive";
        createDirectories();
    }
    
    private void createDirectories() {
        new File(logDirectory).mkdirs();
        new File(archiveDirectory).mkdirs();
    }
    
    @Override
    public void logInfo(String component, String message) {
        log("INFO", component, message);
    }
    
    @Override
    public void logWarning(String component, String message) {
        log("WARNING", component, message);
    }
    
    @Override
    public void logError(String component, String message) {
        log("ERROR", component, message);
    }
    
    @Override
    public void logStorageAction(String storageUnit, String action) {
        String message = String.format("Storage %s: %s", storageUnit, action);
        log("INFO", "STORAGE", message);
    }
    

    private void log(String level, String component, String message) {
        LogEntry entry = new LogEntry(level, component, message);
        writeToCSV(entry);
        System.out.println("[" + level + "] " + component + ": " + message);
    }
    
    private void writeToCSV(LogEntry entry) {
        String date = LocalDate.now().toString();
        String filename = "pharmacy_log_" + date + ".csv";
        String filePath = logDirectory + File.separator + filename;
        
        try {
            File file = new File(filePath);
            boolean isNewFile = !file.exists();
            
            try (FileWriter writer = new FileWriter(filePath, true)) {
                if (isNewFile) {
                    writer.write(LogEntry.getCSVHeader());
                }
                writer.write(entry.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Error log write: " + e.getMessage());
        }
    }
    
    @Override
    public List<String> getLogsByDate(LocalDate date) {
        return readLogsFromFile(date);
    }
    
    @Override
    public List<String> searchLogs(String regexPattern, LocalDate date) {
        List<String> allLogs = readLogsFromFile(date);
        if (allLogs.isEmpty()) return new ArrayList<>();
        
        try {
            Pattern pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
            
            return allLogs.stream()
                .skip(1)
                .filter(line -> pattern.matcher(line).find())
                .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Regex error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @Override
    public boolean moveLogFile(LocalDate date, String targetDirectory) {
        String filename = "pharmacy_log_" + date + ".csv";
        String sourcePath = logDirectory + File.separator + filename;
        String targetPath = targetDirectory + File.separator + filename;
        
        try {
            Path source = Paths.get(sourcePath);
            Path target = Paths.get(targetPath);
            
            
            Files.createDirectories(target.getParent());
            
            
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Log file moved: " + targetPath);
            return true;
        } catch (IOException e) {
            System.err.println("Error file move: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean deleteLogFile(LocalDate date) {
        String filename = "pharmacy_log_" + date + ".csv";
        String filePath = logDirectory + File.separator + filename;
        
        try {
            boolean deleted = Files.deleteIfExists(Paths.get(filePath));
            if (deleted) {
                System.out.println("Log deleted: " + filename);
            } else {
                System.out.println("Log not founded: " + filename);
            }
            return deleted;
        } catch (IOException e) {
            System.err.println("Error file delete: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean archiveLogFile(LocalDate date) {
        String filename = "pharmacy_log_" + date + ".csv";
        String sourcePath = logDirectory + File.separator + filename;
        String zipPath = archiveDirectory + File.separator + "pharmacy_log_" + date + ".zip";
        
        try {
            if (!Files.exists(Paths.get(sourcePath))) {
                System.out.println("No file found for archive: " + filename);
                return false;
            }
            
            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath));
                 FileInputStream fis = new FileInputStream(sourcePath)) {
                
                ZipEntry zipEntry = new ZipEntry(filename);
                zos.putNextEntry(zipEntry);
                
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                
                zos.closeEntry();
            }
            
            
            Files.deleteIfExists(Paths.get(sourcePath));
            System.out.println("File has archived: " + zipPath);
            return true;
            
        } catch (IOException e) {
            System.err.println("Archive Error: " + e.getMessage());
            return false;
        }
    }
    
    private List<String> readLogsFromFile(LocalDate date) {
        String filename = "pharmacy_log_" + date + ".csv";
        String filePath = logDirectory + File.separator + filename;
        
        if (!Files.exists(Paths.get(filePath))) {
            System.out.println("No log file found: " + filename);
            return new ArrayList<>();
        }
        
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Log read error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

	@Override
	public void logMedicationAction(String medicationId, String action, int quantity, String storageUnit) {
		String message = String.format("Medication %s: %s - Quantity: %d", medicationId, action, quantity);
        log("INFO", "MEDICATION", message);
		
	}

	@Override
	public void logRobotAction(String robotId, String action, String location) {
		String message = String.format("Robot %s: %s", robotId, action);
        log("INFO", "ROBOT", message);
		
	}
}