package com.vitalityactive.va.splashscreen;

import com.vitalityactive.va.networking.RequestResult;

public class AppConfigDataUpdateFailedEvent {
    private RequestResult errorType;

    public AppConfigDataUpdateFailedEvent(RequestResult errorType) {
        this.errorType = errorType;
    }

    public RequestResult getErrorType() {
        return errorType;
    }
}
