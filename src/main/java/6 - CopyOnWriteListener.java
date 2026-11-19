import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class CopyOnWriteListener {
    private Notifier notifier;

    interface EventListener {
        void onEvent(String event);
    }

    class Notifier {
        private final List<EventListener> listeners = new CopyOnWriteArrayList<>();

        public void addListener(EventListener listener) {
            listeners.add(listener);
            IO.println("Added listener. List size: " + listeners.size());
        }

        public void notifyListeners(String event) {
            IO.println("Notifying listeners: " + listeners.size() + " " + event);
            for (EventListener listener : listeners) {
                listener.onEvent(event);
                try {
                    Thread.sleep(10); // notifying
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    void main(String[] args) {
        creatingNotifier();
        usingFixedThreadPool();
    }

    private void creatingNotifier() {
        notifier = new Notifier();
        addingListeners("A - Received event: ");
        addingListeners("B - Received event: ");
    }

    private void addingListeners(String message) {
        notifier.addListener(event -> IO.println(message + event));
    }

    private void usingFixedThreadPool() {
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            executor.submit(() -> {
                for (int i = 0; i < 5; i++) {
                    notifier.notifyListeners("Event " + i);
                }
            });

            executor.submit(() -> {
                try {
                    Thread.sleep(50);
                    addingListeners("C - Received event: ");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
