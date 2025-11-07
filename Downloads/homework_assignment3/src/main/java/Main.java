
import java.util.Random;
import java.util.concurrent.TimeUnit;
import ChargingStation.AGV;
import ChargingStation.ChargingManager;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        int stationCount = 3; 
        int totalAGV = 10;     
        int parallelTasks = 5; 

        ChargingManager manager = new ChargingManager(stationCount, 15 * 60); // 15 min threshold
        Random rand = new Random();

        System.out.println("=== Starting AGV Charging Simulation ===");

        // Simulate AGV arrivals randomly over time
        for (int i = 0; i < totalAGV; i++) {
            boolean urgent = rand.nextBoolean(); 
            int battery = rand.nextInt(50) + 10;
            AGV agv = new AGV(battery, urgent);

            manager.submitAGV(agv);
            System.out.println("Submitted AGV-" + agv.getId() + " (Battery=" + battery + ", Urgent=" + urgent + ")");

            // Random delay before next AGV arrives (0..3 sec)
            Thread.sleep(rand.nextInt(3000));
        }

        
        // Wait for all tasks to finish
        while (!manager.getQueue().isEmpty() || !manager.getExecutor().isTerminated()) {
            Thread.sleep(1000);
        }

        manager.shutdown();
        System.out.println("=== Simulation Finished ===");
    }
}
