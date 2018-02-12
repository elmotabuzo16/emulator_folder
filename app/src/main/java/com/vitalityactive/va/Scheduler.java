package com.vitalityactive.va;

public interface Scheduler {
    void schedule(Runnable runnable);

    void cancel();
}
