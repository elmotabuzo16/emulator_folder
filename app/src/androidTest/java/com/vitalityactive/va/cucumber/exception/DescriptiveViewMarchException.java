package com.vitalityactive.va.cucumber.exception;

import android.support.annotation.StringRes;

import com.vitalityactive.va.MockJUnitAndCucumberRunner;

public class DescriptiveViewMarchException extends RuntimeException {
    public DescriptiveViewMarchException(String message, Throwable causedBy) {
        super(message, causedBy);
    }

    public static String getResourceName(int textResourceId) {
        return MockJUnitAndCucumberRunner.getInstance().getApplication().getResources().getResourceName(textResourceId);
    }

    public static String getStringValue(@StringRes int textResourceId) {
        return MockJUnitAndCucumberRunner.getInstance().getApplication().getResources().getString(textResourceId);
    }
}
