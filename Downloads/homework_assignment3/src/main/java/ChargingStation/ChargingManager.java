package ChargingStation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChargingManager {
    private int stationCount;
    private ChargingQueue queue = new ChargingQueue(); 
    private ExecutorService executor;
    private Semaphore slots;
    private long dropThresholdSeconds;
    private AtomicBoolean running = new AtomicBoolean(false);

    public ChargingManager(int stationCount, long dropThresholdSeconds) {
        this.stationCount = stationCount;
        this.executor = Executors.newFixedThreadPool(Math.max(1, stationCount));
        this.slots = new Semaphore(stationCount);
        this.dropThresholdSeconds = dropThresholdSeconds;
    }

    public void submitAGV(AGV agv) {
        queue.add(agv);
        if (running.compareAndSet(false, true)) {
            executor.submit(this::processQueue);
        }
    }

    private void processQueue() {
        try {
            while (!queue.isEmpty()) {
                AGV candidate = queue.peek();
                if (candidate == null) break;

                long waited = (System.currentTimeMillis() - candidate.getArrivalTime()) / 1000;
                if (waited > dropThresholdSeconds) {
                    queue.poll();
                    LogHelper.log("ChargingQueue", "Dropped AGV-" + candidate.getId() + " after waiting " + waited + "s");
                    continue;
                }

                if (slots.tryAcquire()) {
                    AGV agv = queue.poll();
                    if (agv == null) {
                        slots.release();
                        continue;
                    }

                    executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ChargingStation station = new ChargingStation((int)(Math.random() * stationCount) + 1, agv);
                                station.run();
                            } finally {
                                slots.release();
                            }
                        }
                    });
                } else {
                    try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); break; }
                }
            }
        } finally {
            running.set(false);
        }
    }

    public ChargingQueue getQueue() {
        return queue;
    }

    public void shutdown() {
        executor.shutdown();
    }

	public ExecutorService getExecutor() {
		return executor;
	}

}
