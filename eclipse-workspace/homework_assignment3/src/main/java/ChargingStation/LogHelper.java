package ChargingStation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


public class LogHelper {
    private static final Path LOG_DIR = Paths.get("logs");
    private static final Path META = LOG_DIR.resolve("metadata.txt");
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    static {
        try {
            Files.createDirectories(LOG_DIR);
            if (!Files.exists(META)) {
                Files.createFile(META);
            }
        } catch (IOException e) {
            System.err.println("Failed init LogHelper: " + e.getMessage());
        }
    }

    public static synchronized void log(String equipmentId, String message) {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        Path file = LOG_DIR.resolve(equipmentId + "_" + date + ".log");
        String line = now.format(DF) + " - " + message;
        try (BufferedWriter bw = Files.newBufferedWriter(file, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            bw.write(line);
            bw.newLine();
            addMetadata(equipmentId, date, file);
        } catch (IOException e) {
            System.err.println("Error writing to log: " + e.getMessage());
        }
    }

    private static synchronized void addMetadata(String equipmentId, String date, Path file) {
    	String id = UUID.randomUUID().toString()
                .replaceAll("[^0-9]", "")
                .substring(0, 18); 
        String entry = String.format(
            "{\"id\":\"%s\",\"equipmentId\":\"%s\",\"date\":\"%s\",\"path\":\"%s\",\"createdAt\":\"%s\"}",
            id, equipmentId, date, file.toString(), LocalDateTime.now().format(DF)            
        );

        try (BufferedWriter bw = Files.newBufferedWriter(
                META, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            bw.write(entry);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Failed to update metadata: " + e.getMessage());
        }
    }

    public static Path findLog(String equipmentOrDate) {
        try {
            if (!Files.exists(META)) return null;

            for (String line : Files.readAllLines(META, StandardCharsets.UTF_8)) {
                if (line.contains("\"equipmentId\":\"" + equipmentOrDate + "\"") ||
                    line.contains("\"date\":\"" + equipmentOrDate + "\"")) {

                    int p = line.indexOf("\"path\":\"");
                    if (p >= 0) {
                        int s = p + 8;
                        int e = line.indexOf("\"", s);
                        if (e > s) {
                            String path = line.substring(s, e);
                            return Paths.get(path);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read metadata: " + e.getMessage());
        }
        return null;
    }
}
