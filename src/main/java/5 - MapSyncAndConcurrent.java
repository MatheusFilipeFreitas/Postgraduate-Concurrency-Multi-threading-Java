import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class MapSyncAndConcurrent {

    void main(String[] args) {
        useSyncronizedMap();
        useConcurrentHashMap();
    }

    private void useSyncronizedMap() {
        IO.println("Syncronized Map");
        Map<String, Integer> map = Collections.synchronizedMap(new HashMap<>());
        runTask(map);
    }

    private void useConcurrentHashMap() {
        IO.println("ConcurrentHashMap");
        Map<String, Integer> map = new ConcurrentHashMap<>();
        runTask(map);
    }

    private void runTask(Map<String, Integer> map) {
        int numThreads = Runtime.getRuntime().availableProcessors() * 2;
        int operationsPerThread = 100_000;

        try (ExecutorService executorService = Executors.newFixedThreadPool(numThreads)) {
            long startTime = System.nanoTime();
            for (int i = 0; i < numThreads; i++) {
                executorService.submit(() -> {
                    for (int j = 0; j < operationsPerThread; j++) {
                        String key = Thread.currentThread().getName() + "_" + j;
                        map.put(key, j);
                        map.get(key);
                    }
                });
            }
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.MINUTES);

            long endTime = System.nanoTime();
            long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            IO.println("Duration: " + duration + "ms");
            IO.println("Map size: " + map.size());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
