package com.vitalityactive.va.events;

import com.vitalityactive.va.networking.RequestResult;

public class BaseEventWithResponse<T> extends BaseEvent {
    private final RequestResult requestResult;
    private final T responseBody;

    public BaseEventWithResponse(RequestResult requestResult) {
        this(requestResult, null);
    }

    public BaseEventWithResponse(RequestResult requestResult,
                                 T responseBody) {
        this.requestResult = requestResult;
        this.responseBody = responseBody;
    }

    public boolean isSuccessful() {
        return requestResult == RequestResult.SUCCESSFUL;
    }

    public RequestResult getRequestResult() {
        return requestResult;
    }

    public T getResponseBody() {
        return responseBody;
    }
}
