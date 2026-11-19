import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

class IoBoundVirtual {

    void main(String[] args) {
        useVirtualThread();
    }

    private void useVirtualThread() {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 100_000).forEach(i -> {
                runTaskInExecutor(executor, i);
            });
        }
    }

    private void runTaskInExecutor(ExecutorService executor, int taskIndex) {
        executor.submit(() -> {
            try {
                runTask(taskIndex);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private void runTask(int taskIndex) throws InterruptedException {
        IO.println("Task " + taskIndex + " started in " + Thread.currentThread());
        Thread.sleep(1000);
        IO.println("Task " + taskIndex + " finished in " + Thread.currentThread());
    }
}
