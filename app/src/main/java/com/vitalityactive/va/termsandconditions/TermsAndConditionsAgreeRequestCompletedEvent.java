package com.vitalityactive.va.termsandconditions;

import com.vitalityactive.va.networking.RequestResult;

public class TermsAndConditionsAgreeRequestCompletedEvent {
    private final RequestResult requestResult;

    public TermsAndConditionsAgreeRequestCompletedEvent(RequestResult requestResult) {
        this.requestResult = requestResult;
    }

    public RequestResult getRequestResult() {
        return requestResult;
    }
}
