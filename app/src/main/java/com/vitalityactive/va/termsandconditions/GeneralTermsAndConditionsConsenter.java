package com.vitalityactive.va.termsandconditions;

import com.vitalityactive.va.constants.UserInstructions;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceResponseParser;

public class GeneralTermsAndConditionsConsenter implements TermsAndConditionsConsenter {
    private final EventDispatcher eventDispatcher;
    private final CaptureLoginEventServiceClient serviceClient;
    private final GeneralTermsAndConditionsInstructionRepository instructionRepository;
    private RequestResult agreeRequestResult = RequestResult.NONE;
    private RequestResult disagreeRequestResult = RequestResult.NONE;

    public GeneralTermsAndConditionsConsenter(EventDispatcher eventDispatcher,
                                              CaptureLoginEventServiceClient serviceClient,
                                              GeneralTermsAndConditionsInstructionRepository instructionRepository) {
        this.eventDispatcher = eventDispatcher;
        this.serviceClient = serviceClient;
        this.instructionRepository = instructionRepository;
    }

    @Override
    public void agreeToTermsAndConditions() {
        serviceClient.captureLoginEvent(instructionRepository.getInstructionId(), UserInstructions.Types.LOGIN_T_AND_C, true, new AgreeResponseParser());
    }

    @Override
    public void agreeToTermsAndConditions(String name, String rewardKey, String instructionType) {
        agreeToTermsAndConditions();
    }

    @Override
    public void disagreeToTermsAndConditions() {
        serviceClient.captureLoginEvent(instructionRepository.getInstructionId(), UserInstructions.Types.LOGIN_T_AND_C, false, new DisagreeResponseParser());
    }

    @Override
    public void disagreeToTermsAndConditions(String name, String rewardKey, String instructionType) {
        agreeToTermsAndConditions();
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
        return serviceClient.isRequestInProgress();
    }

    private class AgreeResponseParser implements WebServiceResponseParser<String> {

        @Override
        public void parseResponse(String body) {
            instructionRepository.removeInstruction();
            onRequestCompleted(RequestResult.SUCCESSFUL);
        }

        @Override
        public void parseErrorResponse(String errorBody, int code) {
            onRequestCompleted(RequestResult.GENERIC_ERROR);
        }

        @Override
        public void handleGenericError(Exception exception) {
            onRequestCompleted(RequestResult.GENERIC_ERROR);
        }

        @Override
        public void handleConnectionError() {
            onRequestCompleted(RequestResult.CONNECTION_ERROR);
        }

        void onRequestCompleted(RequestResult requestResult) {
            setAgreeRequestResult(requestResult);
            eventDispatcher.dispatchEvent(new TermsAndConditionsAgreeRequestCompletedEvent(requestResult));
        }
    }

    private class DisagreeResponseParser extends AgreeResponseParser {
        @Override
        void onRequestCompleted(RequestResult requestResult) {
            setDisagreeRequestResult(requestResult);
            eventDispatcher.dispatchEvent(new TermsAndConditionsDisagreeRequestCompletedEvent(requestResult));
        }
    }
}
