package ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

final class BackgroundWorker {

    private final ExecutorService worker = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "experiment-ui-worker");
        thread.setDaemon(true);
        return thread;
    });

    private final BooleanProperty busy = new SimpleBooleanProperty(false);
    private final Consumer<String> statusSink;
    private final BiConsumer<String, Throwable> errorSink;

    BackgroundWorker(Consumer<String> statusSink, BiConsumer<String, Throwable> errorSink) {
        this.statusSink = statusSink;
        this.errorSink = errorSink;
    }

    BooleanProperty busyProperty() {
        return busy;
    }

    void run(String runningStatus, ThrowingRunnable action, Runnable onSuccess) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                action.run();
                return null;
            }
        };

        task.setOnRunning(event -> {
            busy.set(true);
            statusSink.accept(runningStatus);
        });
        task.setOnSucceeded(event -> {
            busy.set(false);
            onSuccess.run();
        });
        task.setOnFailed(event -> {
            busy.set(false);
            errorSink.accept(runningStatus.replace("...", " failed"), task.getException());
            statusSink.accept("Ready");
        });

        worker.submit(task);
    }

    void shutdown() {
        worker.shutdownNow();
    }

    @FunctionalInterface
    interface ThrowingRunnable {
        void run() throws Exception;
    }
}
