package ChargingStation;

import org.junit.jupiter.api.Test;

import Exceptions.ChargingException;

public class ChargingSimulationTest {

    @Test
    public void testAGVChargingSimulation() throws InterruptedException {
        // Drop threshold: 15 seconds
        ChargingManager manager = new ChargingManager(2, 15);

        // Simulate 5 AGVs
        for (int i = 0; i < 5; i++) {
            boolean urgent = (i % 2 == 0); 
            int battery = (int) (Math.random() * 50);
            AGV agv = new AGV(battery, urgent);
            manager.submitAGV(agv);
        }

        // Simulate extra arriving AGVs after 1 second
        Thread.sleep(1000);
        for (int i = 0; i < 5; i++) {
            AGV agv = new AGV((int) (Math.random() * 30), false);
            manager.submitAGV(agv);
        }

        // Give some time for all AGVs to be processed
        Thread.sleep(5000);

        manager.shutdown(); // shutdown executor
    }

    @Test
    public void testChargeZeroShouldThrowException() {
        AGV agv = new AGV(0, false);
        try {
            agv.charge();
        } catch (ChargingException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }
    }

    @Test
    public void testStationMalfunctionHandling() throws InterruptedException {
        ChargingManager manager = new ChargingManager(2, 15);
        AGV agv = new AGV(40, false);
        manager.submitAGV(agv);

        // Give some time for charging
        Thread.sleep(2000);

        manager.shutdown();
    }
}
