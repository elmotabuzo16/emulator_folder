package com.vitalityactive.va.uicomponents.slotmachine;

import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

public class SlotMachineScroller implements Runnable {
    private final SlotMachineView view;
    private final Handler handler;
    private final Scroller scroller;
    private int lastY;
    private int distance;
    private int previousDistance;

    public SlotMachineScroller(SlotMachineView view) {
        this.view = view;
        handler = new Handler();
        scroller = new Scroller(view.getContext(), new AccelerateDecelerateInterpolator());
    }

    public void startScroll(int distance, int milliseconds) {
        this.distance = distance;
        lastY = 0;
        scroller.forceFinished(true);
        scroller.startScroll(0, 0, 0, distance, milliseconds);
        handler.post(this);
    }

    public void cancelSpin() {
        if (!scroller.isFinished()) {
            scroller.forceFinished(true);
        }
    }

    @Override
    public void run() {
        if (scroller.isFinished()) {
            return;
        }

        scroller.computeScrollOffset();
        int currentY = scroller.getCurrY();
        int delta = currentY - lastY;
        lastY = currentY;

        if (Math.abs(delta) != previousDistance && delta != 0) {
            view.scroll(delta);
        }

        if (scroller.isFinished()) {
            previousDistance = distance;
            onFinishedScrolling();
        } else {
            handler.post(this);
        }
    }

    protected void onFinishedScrolling() {
        view.onScrollFinished();
    }
}
