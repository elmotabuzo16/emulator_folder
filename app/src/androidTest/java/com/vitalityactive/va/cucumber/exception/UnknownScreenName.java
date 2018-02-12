package com.vitalityactive.va.cucumber.exception;

public class UnknownScreenName extends RuntimeException {
    public UnknownScreenName(String screenName) {
        super(String.format("Failed to start screen, unknown screen name '%s'", screenName));
    }
}
