import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class ScheduledThread {

    void main() {
        try {
            useScheduledThreadPool();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void useScheduledThreadPool() throws InterruptedException {
        try (ScheduledExecutorService executor = Executors.newScheduledThreadPool(2)) {
            oneTimeRun(executor); // initial delay = 3
            everyFiveSecondsRunFixedRate(executor); // initial delay = 1, fixed rate = 5
            runEveryWithFixedDelay(executor); // initial delay = 1, fixed delay = 5
            observationSleep();
        }
    }

    private void oneTimeRun(ScheduledExecutorService executor) {
        executor.schedule(() -> {
            IO.println("Unique execution task ran at " + LocalTime.now());
        }, 3, TimeUnit.SECONDS);
    }

    private void everyFiveSecondsRunFixedRate(ScheduledExecutorService executor) {
        executor.scheduleAtFixedRate(() -> {
            IO.println("Periodic execution (rate) task ran at " + LocalTime.now());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, 1, 5, TimeUnit.SECONDS);
    }

    private void runEveryWithFixedDelay(ScheduledExecutorService executor) {
        executor.scheduleWithFixedDelay(() -> {
            IO.println("Periodic execution (delay) task ran at " + LocalTime.now());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, 1, 5, TimeUnit.SECONDS);
    }

    private void observationSleep() throws InterruptedException {
        Thread.sleep(20000);
    }
}
