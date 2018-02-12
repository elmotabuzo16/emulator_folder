package com.vitalityactive.va.utilities;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.vitalityactive.va.VitalityActiveApplication;

import java.util.Locale;

public class InvalidModelLogger {
    private static final String TAG = "InvalidModelLogger";
    private static final int NEWLINES_TO_DROP_FROM_STACK_TRACE = 3;

    public static <T, U> void warn(T response, U uniqueIdentifier, String reason) {
        warn(response.getClass(), response, uniqueIdentifier, reason);
    }

    public static <T, U> void warn(Class<?> clazz, T response, U uniqueIdentifier, String reason) {
        if (!VitalityActiveApplication.isDevDebugBuild()) {
            return;
        }
        String className = clazz.getSimpleName();
        String stackTrace = getStackTraceString();
        Log.w(TAG, String.format(Locale.getDefault(), "Invalid '%s' found (unique id: %s) %s\n%s stack:%s",
                className, uniqueIdentifier, reason, getObjectAsJson(response), stackTrace));
    }

    private static <T> String getObjectAsJson(T response) {
        try {
            return String.format("json: %s\n", new GsonBuilder().create().toJson(response));
        } catch (Throwable ignored) {
            return "";
        }
    }

    private static String getStackTraceString() {
        String stackTraceString = "";
        try {
            stackTraceString = Log.getStackTraceString(new Throwable());
            for (int i = 0; i < NEWLINES_TO_DROP_FROM_STACK_TRACE; i++) {
                int index = stackTraceString.indexOf(10);
                stackTraceString = stackTraceString.substring(index + 1);
            }
            stackTraceString = "\n" + stackTraceString;
        } catch (Throwable ignored) {
        }
        return stackTraceString;
    }
}
