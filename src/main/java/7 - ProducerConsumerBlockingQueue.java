import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ProducerConsumerBlockingQueue {
    BlockingQueue<Integer> queue;

    void main(String[] args) {
        startingBlockingQueue(10);
        useVirtualThread();
    }

    private void startingBlockingQueue(int queueSize) {
        queue = new ArrayBlockingQueue<>(queueSize);
    }

    private void useVirtualThread() {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            producer(executor);
            consumer(executor);
        }
    }

    private void producer(ExecutorService executor) {
        executor.submit(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    IO.println("Producing " + i);
                    queue.put(i);
                    Thread.sleep(50); // producing
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private void consumer(ExecutorService executor) {
        executor.submit(() -> {
            try {
                while (true) {
                    Integer value = queue.take();
                    IO.println("Consuming " + value);
                    Thread.sleep(100); // consuming
                    if (value == 99) break; // stop condition
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
