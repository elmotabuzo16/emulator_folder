package com.vitalityactive.va.cucumber.exception;

import java.util.Locale;

public class TimeoutException extends RuntimeException {
    public TimeoutException(String waitedFor, int milliSecondsWaited) {
        super(String.format(Locale.getDefault(), "timeout after waiting %d ms for %s", milliSecondsWaited, waitedFor));
    }
}
