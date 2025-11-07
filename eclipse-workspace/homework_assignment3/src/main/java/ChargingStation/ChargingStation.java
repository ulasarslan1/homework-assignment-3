package ChargingStation;

import Exceptions.ChargingException;

public class ChargingStation implements Runnable {
    private int stationId;
    private AGV agv; // AGV assigned to this station

    
    public ChargingStation(int stationId, AGV agv) {
        this.stationId = stationId;
        this.agv = agv;
    }

    @Override
    public void run() {
        try {
            LogHelper.log("Station-" + stationId, "Starting charging for AGV-" + agv.getId());
            // Random malfunction simulation
            if (Math.random() < 0.02) {
                throw new ChargingException("Hardware failure at station " + stationId, ChargingException.ChargingErrorType.MALFUNCTION);
            }
            // Normal charging
            agv.charge();
            LogHelper.log("Station-" + stationId, "Finished charging AGV-" + agv.getId());
        } catch (ChargingException e) {
            System.err.println("[ERROR] " + e.getMessage() + " | Type: " + e.getErrorType());
            LogHelper.log("Station-" + stationId, "ERROR: " + e.getErrorType());
        } catch (Exception e) {
            LogHelper.log("Station-" + stationId, "Unexpected error: " + e.getMessage());
        }
    }
    
    
}
