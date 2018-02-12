package com.vitalityactive.va.userpreferences;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.UpdatePartyServiceRequest;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class EmailPreferenceServiceClient implements WebServiceResponseParser<String> {
    private WebServiceClient webServiceClient;
    private UserPreferencesService service;
    private PartyInformationRepository repository;
    private Call<String> request;
    private EventDispatcher eventDispatcher;
    private RequestResult emailPreferenceRequestResult = RequestResult.NONE;
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;

    @Inject
    public EmailPreferenceServiceClient(WebServiceClient webServiceClient, UserPreferencesService service, PartyInformationRepository repository, EventDispatcher eventDispatcher, AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        this.webServiceClient = webServiceClient;
        this.service = service;
        this.repository = repository;
        this.eventDispatcher = eventDispatcher;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    public void optInToEmailCommunication() {
        toggleEmailCommunication(true);
    }

    public void optOutOfEmailCommunication() {
        toggleEmailCommunication(false);
    }

    private void toggleEmailCommunication(boolean value) {
        if (isEmailToggleRequestInProgress()) {
            return;
        }
        request = service.getEmailPreferenceUpdateRequest(accessTokenAuthorizationProvider.getAuthorization(),
                repository.getTenantId(),
                repository.getPartyId(),
                getEmailCommunicationRequest(value));
        webServiceClient.executeAsynchronousRequest(request, this);
    }

    private UpdatePartyServiceRequest getEmailCommunicationRequest(boolean value) {
        return  UpdatePartyServiceRequest.builder()
                .addGeneralPreference(UpdatePartyServiceRequest.GeneralPreferenceType.EMAIL, Boolean.toString(value))
                .build();
    }

    public boolean isEmailToggleRequestInProgress() {
        return request != null && webServiceClient.isRequestInProgress(request.request().toString());
    }

    @Override
    public void parseResponse(String body) {
        onEmailPreferenceRequestCompleted(RequestResult.SUCCESSFUL);
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        onEmailPreferenceRequestCompleted(RequestResult.GENERIC_ERROR);
    }

    @Override
    public void handleGenericError(Exception exception) {
        onEmailPreferenceRequestCompleted(RequestResult.GENERIC_ERROR);
    }

    @Override
    public void handleConnectionError() {
        onEmailPreferenceRequestCompleted(RequestResult.CONNECTION_ERROR);
    }

    private void onEmailPreferenceRequestCompleted(RequestResult requestResult) {
        setEmailPreferenceRequestResult(requestResult);
        eventDispatcher.dispatchEvent(new EmailPreferenceToggleCompletedEvent());
    }

    private synchronized void setEmailPreferenceRequestResult(RequestResult emailPreferenceRequestResult) {
        this.emailPreferenceRequestResult = emailPreferenceRequestResult;
    }

    public synchronized RequestResult getEmailPreferenceRequestResult() {
        return emailPreferenceRequestResult;
    }
}
