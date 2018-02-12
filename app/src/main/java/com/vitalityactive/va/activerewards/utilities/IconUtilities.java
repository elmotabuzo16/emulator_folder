package com.vitalityactive.va.activerewards.utilities;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.GoalTrackerStatus;

public class IconUtilities {

    @DrawableRes
    public static int getIconId(int statusCode) {
        switch (statusCode) {
            case GoalTrackerStatus._ACHIEVED:
            case GoalTrackerStatus._MANUALLYACHIEVED:
                return R.drawable.achieved_24;
            case GoalTrackerStatus._INPROGRESS:
                return R.drawable.in_progress_24;
            case GoalTrackerStatus._NOTACHIEVED:
                return R.drawable.notachieved_24;
            case GoalTrackerStatus._PENDING:
                return R.drawable.pending_24;
            case GoalTrackerStatus._CANCELLED:
            case GoalTrackerStatus._MANUALCANCELLATION:
            case GoalTrackerStatus._SYSTEMCANCELLATION:
                return R.drawable.notachieved_24;
            default:
                return R.drawable.achieved_24;
        }
    }

    @ColorRes
    public static int getIconColor(int statusCode) {
        switch (statusCode) {
            case GoalTrackerStatus._ACHIEVED:
            case GoalTrackerStatus._MANUALLYACHIEVED:
                return R.color.jungle_green;
            case GoalTrackerStatus._INPROGRESS:
                return R.color.water_blue;
            case GoalTrackerStatus._NOTACHIEVED:
                return R.color.crimson_red;
            case GoalTrackerStatus._PENDING:
                return R.color.pending_orange;
            case GoalTrackerStatus._CANCELLED:
            case GoalTrackerStatus._MANUALCANCELLATION:
            case GoalTrackerStatus._SYSTEMCANCELLATION:
                return R.color.crimson_red;
            default:
                return R.color.jungle_green;
        }
    }
}
