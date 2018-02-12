package com.vitalityactive.va;

import android.os.Handler;
import android.os.Looper;

public class MainThreadSchedulerImpl implements MainThreadScheduler {
    private Handler handler;

    public MainThreadSchedulerImpl() {
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void schedule(Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            handler.post(runnable);
        }
    }

    @Override
    public void cancel() {
        handler.removeCallbacksAndMessages(null);
    }

    private boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
