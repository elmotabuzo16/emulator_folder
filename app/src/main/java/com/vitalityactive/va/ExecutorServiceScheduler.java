package com.vitalityactive.va;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ExecutorServiceScheduler implements Scheduler {

    private final ExecutorService executorService;

    public ExecutorServiceScheduler(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void schedule(Runnable runnable) {
        executorService.submit(runnable);
    }

    @Override
    public void cancel() {

    }

    public void schedule(List<Runnable> runnables, Callback callback) {
        List<Future> futures = new ArrayList<>();
        for (Runnable runnable : runnables) {
            futures.add(executorService.submit(runnable));
        }
        for (Future future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        callback.onSchedulingCompleted();
    }

    public interface Callback {
        void onSchedulingCompleted();
    }
}
