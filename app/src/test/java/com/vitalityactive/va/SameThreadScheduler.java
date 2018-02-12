package com.vitalityactive.va;

public class SameThreadScheduler implements MainThreadScheduler {
    public boolean invoked;

    @Override
    public void schedule(Runnable runnable) {
        invoked = true;
        runnable.run();
    }

    @Override
    public void cancel() {

    }

}
