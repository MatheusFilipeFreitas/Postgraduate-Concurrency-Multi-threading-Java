import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class LifecycleFixed {

    void main() {
        newFixedThreadPool();
        oldFixedThreadPool();
    }

    private void newFixedThreadPool() {
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            executor.submit(() -> {
                IO.println("Java 19 > running fixed thread pool");
            });
        }
        IO.println("Java 19 > ended fixed thread pool");
    }

    private void oldFixedThreadPool() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            executor.submit(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                IO.println("Java 19 < running fixed thread pool");
            });
        } finally {
            shutdownAndAwaitTermination(executor);
        }
        IO.println("Java 19 < ended fixed thread pool");
    }

    private void shutdownAndAwaitTermination(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();

                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    IO.println("Java 19 < could not shutdown executor");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
