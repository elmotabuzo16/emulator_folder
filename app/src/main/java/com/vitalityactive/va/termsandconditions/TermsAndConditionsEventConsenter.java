package com.vitalityactive.va.termsandconditions;

import android.support.annotation.NonNull;

import com.vitalityactive.va.EventServiceClient;
import com.vitalityactive.va.ProcessEventsServiceResponse;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceResponseParser;

import java.util.concurrent.atomic.AtomicBoolean;

public class TermsAndConditionsEventConsenter extends TermsAndConditionsConsenterBase {
    private final EventDispatcher eventDispatcher;
    private final EventServiceClient eventServiceClient;
    @NonNull
    private final long[] agreeEvents;
    @NonNull
    private final long[] disagreeEvents;
    private AtomicBoolean isRequestInProgress = new AtomicBoolean();
    private RequestResult agreeRequestResult = RequestResult.NONE;
    private RequestResult disagreeRequestResult = RequestResult.NONE;
    private WebServiceResponseParser<ProcessEventsServiceResponse> agreeResponseParser = new WebServiceResponseParser<ProcessEventsServiceResponse>() {
        @Override
        public void parseResponse(ProcessEventsServiceResponse body) {
            completeAgreeRequest(RequestResult.SUCCESSFUL);
        }

        @Override
        public void parseErrorResponse(String errorBody, int code) {
            completeAgreeRequest(RequestResult.GENERIC_ERROR);
        }

        @Override
        public void handleGenericError(Exception exception) {
            completeAgreeRequest(RequestResult.GENERIC_ERROR);
        }

        @Override
        public void handleConnectionError() {
            completeAgreeRequest(RequestResult.CONNECTION_ERROR);
        }
    };
    private WebServiceResponseParser<ProcessEventsServiceResponse> disagreeResponseParser = new WebServiceResponseParser<ProcessEventsServiceResponse>() {
        @Override
        public void parseResponse(ProcessEventsServiceResponse body) {
            completeDisagreeRequest(RequestResult.SUCCESSFUL);
        }

        @Override
        public void parseErrorResponse(String errorBody, int code) {
            completeDisagreeRequest(RequestResult.GENERIC_ERROR);
        }

        @Override
        public void handleGenericError(Exception exception) {
            completeDisagreeRequest(RequestResult.GENERIC_ERROR);
        }

        @Override
        public void handleConnectionError() {
            completeDisagreeRequest(RequestResult.CONNECTION_ERROR);
        }
    };

    public TermsAndConditionsEventConsenter(EventDispatcher eventDispatcher,
                                            EventServiceClient eventServiceClient,
                                            @NonNull long[] agreeEvents,
                                            @NonNull long[] disagreeEvents) {
        this.eventDispatcher = eventDispatcher;
        this.eventServiceClient = eventServiceClient;
        this.agreeEvents = agreeEvents;
        this.disagreeEvents = disagreeEvents;
    }

    @Override
    public void agreeToTermsAndConditions() {
        if (shouldAbortAgreement()) return;

        eventServiceClient.sendEvents(agreeEvents, agreeResponseParser);
    }

    private boolean shouldAbortAgreement() {
        return agreeEvents.length == 0 || !isRequestInProgress.compareAndSet(false, true);
    }

    @Override
    public void agreeToTermsAndConditions(String rewardName, String rewardKey, String instructionType) {
        if (shouldAbortAgreement()) return;

        eventServiceClient.sendEvents(agreeEvents, agreeResponseParser, rewardName, rewardKey, instructionType);
    }

    @Override
    public void disagreeToTermsAndConditions() {
        if (shouldAbortAgreement()) return;

        eventServiceClient.sendEvents(disagreeEvents, disagreeResponseParser);
    }

    @Override
    public void disagreeToTermsAndConditions(String name, String rewardKey, String instructionType) {
        if (shouldAbortAgreement()) return;

        eventServiceClient.sendEvents(agreeEvents, disagreeResponseParser, name, rewardKey, instructionType);
    }

    @Override
    public synchronized RequestResult getAgreeRequestResult() {
        return agreeRequestResult;
    }

    private synchronized void setAgreeRequestResult(RequestResult agreeRequestResult) {
        this.agreeRequestResult = agreeRequestResult;
    }

    @Override
    public synchronized RequestResult getDisagreeRequestResult() {
        return disagreeRequestResult;
    }

    private synchronized void setDisagreeRequestResult(RequestResult disagreeRequestResult) {
        this.disagreeRequestResult = disagreeRequestResult;
    }

    @Override
    public boolean isRequestInProgress() {
        return isRequestInProgress.get();
    }

    private void completeAgreeRequest(RequestResult requestResult) {
        setAgreeRequestResult(requestResult);
        eventDispatcher.dispatchEvent(new TermsAndConditionsAgreeRequestCompletedEvent(requestResult));
        isRequestInProgress.set(false);
    }

    private void completeDisagreeRequest(RequestResult requestResult) {
        setDisagreeRequestResult(requestResult);
        eventDispatcher.dispatchEvent(new TermsAndConditionsDisagreeRequestCompletedEvent(requestResult));
        isRequestInProgress.set(false);
    }
}
