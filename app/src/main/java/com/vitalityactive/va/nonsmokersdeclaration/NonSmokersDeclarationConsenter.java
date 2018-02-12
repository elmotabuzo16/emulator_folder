package com.vitalityactive.va.nonsmokersdeclaration;

import com.vitalityactive.va.EventServiceClient;
import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.ProcessEventsServiceResponse;
import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsAgreeRequestCompletedEvent;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;

import java.util.concurrent.atomic.AtomicBoolean;

public class NonSmokersDeclarationConsenter implements TermsAndConditionsConsenter, WebServiceResponseParser<ProcessEventsServiceResponse> {
    private final EventDispatcher eventDispatcher;
    private final EventServiceClient eventServiceClient;
    private final InsurerConfigurationRepository insurerConfigurationRepository;
    private RequestResult agreeRequestResult = RequestResult.NONE;
    private AtomicBoolean isRequestInProgress = new AtomicBoolean();

    public NonSmokersDeclarationConsenter(EventDispatcher eventDispatcher, EventServiceClient eventServiceClient, InsurerConfigurationRepository insurerConfigurationRepository) {
        this.eventDispatcher = eventDispatcher;
        this.eventServiceClient = eventServiceClient;
        this.insurerConfigurationRepository = insurerConfigurationRepository;
    }

    @Override
    public void agreeToTermsAndConditions() {
        if (!isRequestInProgress.compareAndSet(false, true)) {
            return;
        }
        if (insurerConfigurationRepository.shouldShowNonSmokersPrivacyPolicy()) {
            eventServiceClient.sendEvents(new long[] {EventType._NONSMOKERSDECLRTN, EventType._NONSMOKERSDATAGR}, this);
        } else {
            eventServiceClient.sendEvents(new long[] {EventType._NONSMOKERSDECLRTN}, this);
        }
    }

    @Override
    public void agreeToTermsAndConditions(String name, String rewardKey, String instructionType) {
        agreeToTermsAndConditions();
    }

    @Override
    public void disagreeToTermsAndConditions() {

    }

    @Override
    public void disagreeToTermsAndConditions(String name, String rewardKey, String instructionType) {

    }

    @Override
    public synchronized RequestResult getAgreeRequestResult() {
        return agreeRequestResult;
    }

    @Override
    public RequestResult getDisagreeRequestResult() {
        return RequestResult.NONE;
    }

    @Override
    public boolean isRequestInProgress() {
        return isRequestInProgress.get();
    }

    @Override
    public void parseResponse(ProcessEventsServiceResponse body) {
        completeRequest(RequestResult.SUCCESSFUL);
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        handleGenericError();
    }

    private void handleGenericError() {
        completeRequest(RequestResult.GENERIC_ERROR);
    }

    @Override
    public void handleGenericError(Exception exception) {
        handleGenericError();
    }

    @Override
    public void handleConnectionError() {
        completeRequest(RequestResult.CONNECTION_ERROR);
    }

    private void completeRequest(RequestResult requestResult) {
        setAgreeRequestResult(requestResult);
        eventDispatcher.dispatchEvent(new TermsAndConditionsAgreeRequestCompletedEvent(requestResult));
        isRequestInProgress.set(false);
    }

    private synchronized void setAgreeRequestResult(RequestResult agreeRequestResult) {
        this.agreeRequestResult = agreeRequestResult;
    }
}
