package com.vitalityactive.va.cucumber.exception;

import com.vitalityactive.va.cucumber.screens.BaseScreen;

public class UnexpectedScreenException extends RuntimeException {
    public <T extends BaseScreen> UnexpectedScreenException(Class<T> expected, String currentScreenDetails) {
        super(buildMessage(expected, currentScreenDetails));
    }

    private static <T extends BaseScreen> String buildMessage(Class<T> expected, String currentScreenDetails) {
        return String.format("unexpected screen found. expected %s but the current is %s",
                expected.getSimpleName(),
                currentScreenDetails);
    }
}
