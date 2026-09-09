package com.svgs.framework.reader;

/**
 * Single background thread that walks every registered metric and asks the
 * adapter for a fresh reading.
 */
public class Poller {
    public static final long POLL_INTERVAL_MS = 200;

    private static Thread poller;
    private static volatile boolean running;

    /**
     * Starts the poller if it isn't already going. This is idempotent because
     * screens get rebuilt on every scene swap and would otherwise each spawn a
     * thread.
     */
    public static synchronized void start() {
        if (running) {
            return;
        }
        running = true;

        poller = new Thread(() -> {
            while (running) {
                for (Runnable task : ReaderInterface.getPollTasks()) {
                    task.run();
                }

                try {
                    Thread.sleep(POLL_INTERVAL_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "obd-poller");

        poller.setDaemon(true);
        poller.start();
    }

    public static synchronized void stop() {
        running = false;
        if (poller != null) {
            poller.interrupt();
            poller = null;
        }
    }

    public static boolean isRunning() {
        return running;
    }
}
