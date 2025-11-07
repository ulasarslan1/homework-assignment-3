import java.util.Random;
import ChargingStation.AGV;
import ChargingStation.ChargingManager;
import TaskManagement.TaskManager;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        int stationCount = 3;  // N = 3 charging stations
        int totalAGV = 10;     // K = 10 AGVs
        int parallelTasks = 5; // M = 5 parallel tasks

        ChargingManager chargingManager = new ChargingManager(stationCount, 15 * 60);
        TaskManager taskManager = new TaskManager(parallelTasks);

        Random rand = new Random();

        System.out.println("=== AGV + Task Simulation Started ===");

        for (int i = 0; i < totalAGV; i++) {

            boolean urgent = rand.nextBoolean();
            int battery = rand.nextInt(50) + 10;
            AGV agv = new AGV(battery, urgent);

            chargingManager.submitAGV(agv);
            System.out.println("AGV Submitted  ID=" + agv.getId() + " Battery=" + battery);

            taskManager.createTask("CHARGE", "Dock", "Station");

            Thread.sleep(rand.nextInt(2000));
        }

        System.out.println("=== Processing Tasks... ===");
        taskManager.processTasks();

        taskManager.shutdown();
        System.out.println("=== Simulation Finished ===");
    }
}
