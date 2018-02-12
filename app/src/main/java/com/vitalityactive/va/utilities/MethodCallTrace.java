package com.vitalityactive.va.utilities;

import android.os.Debug;
import android.os.Environment;
import android.util.Log;

import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.VitalityActiveApplication;

public class MethodCallTrace {
    private static final String TAG = "MethodCallTrace";

    public static void start(String name) {
        if (isTraceEnabledOnThisBuild()) {
            String tracePath = Environment.getExternalStorageDirectory().getPath() + "/" + name + ".trace";
            Log.d(TAG, String.format("starting trace for %s to %s from %s", name, tracePath, getStackDetails(1)));
            tryStartTrace(tracePath);
        }
    }

    public static void start() {
        if (isTraceEnabledOnThisBuild()) {
            start(getMethodName());
        }
    }

    public static void stop() {
        if (isTraceEnabledOnThisBuild()) {
            Debug.stopMethodTracing();
            Log.d(TAG, String.format("trace done @ %s", getStackDetails()));
        }
    }

    public static long enterMethod() {
        if (isTraceEnabledOnThisBuild()) {
            long timestamp = TimeUtilities.getCurrentTimestamp();
            Log.d(TAG, String.format("entering %s(...)", getStackDetails()));
            return timestamp;
        }
        return 0;
    }

    public static void exitMethod(long start) {
        if (isTraceEnabledOnThisBuild()) {
            long end = TimeUtilities.getCurrentTimestamp();
            long total = end - start;
            Log.d(TAG, String.format("%d later: leaving %s(...)", total, getStackDetails()));
        }
    }

    private static boolean isTraceEnabledOnThisBuild() {
        return VitalityActiveApplication.isDevDebugBuild() && BuildConfig.TRACE_ENABLED;
    }

    private static void tryStartTrace(String tracePath) {
        try {
            Debug.startMethodTracing(tracePath);
        } catch (Exception e) {
            Log.w(TAG, "failed to start method tracing", e);
        }
    }

    private static String getMethodName() {
        try {
            return new Throwable().getStackTrace()[2].getMethodName();
        } catch (Throwable ignored) {
            return "";
        }
    }

    private static String getStackDetails() {
        return getStackDetails(1);  // +1 for this method
    }

    private static String getStackDetails(int adjustment) {
        try {
            return new Throwable().getStackTrace()[2 + adjustment].toString();
        } catch (Throwable ignored) {
            return "";
        }
    }
}
