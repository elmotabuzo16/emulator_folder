package com.vitalityactive.va.termsandconditions;

import com.vitalityactive.va.networking.RequestResult;

public class TermsAndConditionsDisagreeRequestCompletedEvent {
    private final RequestResult requestResult;

    public TermsAndConditionsDisagreeRequestCompletedEvent(RequestResult requestResult) {
        this.requestResult = requestResult;
    }

    public RequestResult getRequestResult() {
        return requestResult;
    }
}
