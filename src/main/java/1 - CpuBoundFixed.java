import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class CpuBoundFixed {
    private int processorCores = 0;

    void main(String[] args) {
        getCpuCores();
        useFixedThreadPool();
    }

    private void getCpuCores() {
        processorCores = Runtime.getRuntime().availableProcessors();
        IO.println("CPU cores: " + processorCores);
    }

    private void useFixedThreadPool() {
        try (ExecutorService fixedThreadPool = Executors.newFixedThreadPool(processorCores)) {
            for (int i = 0; i < processorCores * 2; i++) {
                final int taskNumber = i;
                runTaskInExecutor(fixedThreadPool, taskNumber);
            }
        }
    }

    private void runTaskInExecutor(ExecutorService fixedThreadPool, int taskNumber) {
        fixedThreadPool.submit(() -> {
            IO.println("Task " + taskNumber + " started in " + Thread.currentThread());
            long result = runIntensiveTask();
            IO.println("Task " + taskNumber + " (result: " + result + ") finished in " + Thread.currentThread());
        }); // executor.shutdown() is called automatically
    }

    private long runIntensiveTask() {
        long sum = 0;
        for (int i = 0; i < 1_000_000_000; i++) {
            sum += i;
        }
        return sum;
    }
}
