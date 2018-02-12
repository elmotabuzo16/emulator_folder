package com.vitalityactive.va.uicomponents.slotmachine;

import android.util.Log;

public class SlotMachineScrollerWithBounce extends SlotMachineScroller {
    private static final String TAG = "SlotMachineScrllWBounce";

    // stop bouncing when the over scroll is less than this
    private static final int SNAP_TO_FINAL_AT_SIZE = 5;

    // amount to over scroll is opposite direction. (set to >1 <2)
    // =1 : the direction itself (so it will bounce exactly once then stop suddenly)
    // =2 : the direction in reverse (so it will over scroll the same distance, in opposite direction. it will never stop)
    // >2 will "gain momentum"/bounce away from the target in both directions, <1 will stop before reaching the target
    private static final double OVER_SCROLL_REVERSE_RATIO = 1.8;

    // how long over scroll durations should take
    private static final double OVER_SCROLL_SPEED = .5;
    private static final int MINIMUM_OVER_SCROLL_DURATION = 50;

    private int overScrollOffset = 0;

    public SlotMachineScrollerWithBounce(SlotMachineView view) {
        super(view);
    }

    public void startScroll(int distance, int milliseconds) {
        overScrollOffset = calculateStartingOverScrollOffset(distance);
        Log.d(TAG, String.format("startScroll %d, with overScrollOffset: %d", distance, overScrollOffset));
        startScrollWithoutOvershoot(distance + overScrollOffset, milliseconds);
    }

    private void startScrollWithoutOvershoot(int distance, int milliseconds) {
        super.startScroll(distance, milliseconds);
    }

    protected int calculateStartingOverScrollOffset(int distance) {
        // for small distances, add relatively large over scroll (else it just snaps)
        // for large distances, add relatively small over scroll (else it takes to long)
        int absDistance = Math.abs(distance);
        return (int) Math.sqrt(absDistance * 2);
    }

    @Override
    protected void onFinishedScrolling() {
        if (overScrollOffset == 0) {
            Log.d(TAG, "over scroll done, calling super.onFinishedScrolling");
            super.onFinishedScrolling();
        } else {
            int move = getDistanceToOvershoot();
            int duration = (int) Math.max(MINIMUM_OVER_SCROLL_DURATION, Math.abs(move) / OVER_SCROLL_SPEED);
            int newOverScrollOffset = overScrollOffset + move;
            Log.d(TAG, String.format("onFinishedScrolling over: %d, move: %d (new over=%d), duration: %d",
                    overScrollOffset, move, newOverScrollOffset, duration));
            overScrollOffset = newOverScrollOffset;
            startScrollWithoutOvershoot(move, duration);
        }
    }

    private int getDistanceToOvershoot() {
        if (Math.abs(overScrollOffset) < SNAP_TO_FINAL_AT_SIZE) {
            return  -overScrollOffset;
        } else {
            return (int) (overScrollOffset * -OVER_SCROLL_REVERSE_RATIO);
        }
    }
}
