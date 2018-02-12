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

/**
 * Created by dharel.h.rosell on 1/11/2018.
 */

@Singleton
public class ShareVitalityStatusPreferenceServiceClient implements WebServiceResponseParser<String> {

    private WebServiceClient webServiceClient;
    private UserPreferencesService service;
    private PartyInformationRepository repository;
    private Call<String> request;
    private EventDispatcher eventDispatcher;
    private RequestResult shareVitalityPreferenceRequestResult = RequestResult.NONE;
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;

    @Inject
    public ShareVitalityStatusPreferenceServiceClient(WebServiceClient webServiceClient, UserPreferencesService service, PartyInformationRepository repository, EventDispatcher eventDispatcher, AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        this.webServiceClient = webServiceClient;
        this.service = service;
        this.repository = repository;
        this.eventDispatcher = eventDispatcher;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    public void optInToShareStatusVitality() {
        toggleShareVitalityStatus(true);
    }

    public void optOutOfShareStatusVitality() {
        toggleShareVitalityStatus(false);
    }

    private void toggleShareVitalityStatus(boolean value) {
        if (isShareVitalityToggleRequestInProgress()) {
            return;
        }
        request = service.getEmailPreferenceUpdateRequest(accessTokenAuthorizationProvider.getAuthorization(),
                repository.getTenantId(),
                repository.getPartyId(),
                getShareVitalityStatusRequest(value));
        webServiceClient.executeAsynchronousRequest(request, this);
    }

    private UpdatePartyServiceRequest getShareVitalityStatusRequest(boolean value) {
        return  UpdatePartyServiceRequest.builder()
                .addGeneralPreference(UpdatePartyServiceRequest.GeneralPreferenceType.SHARE_VITALITY_STATUS, Boolean.toString(value))
                .build();
    }

    public boolean isShareVitalityToggleRequestInProgress() {
        return request != null && webServiceClient.isRequestInProgress(request.request().toString());
    }

    @Override
    public void parseResponse(String response) {
        onShareVitalityStatusRequestCompleted(RequestResult.SUCCESSFUL);
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        onShareVitalityStatusRequestCompleted(RequestResult.GENERIC_ERROR);
    }

    @Override
    public void handleGenericError(Exception exception) {
        onShareVitalityStatusRequestCompleted(RequestResult.GENERIC_ERROR);
    }

    @Override
    public void handleConnectionError() {
        onShareVitalityStatusRequestCompleted(RequestResult.CONNECTION_ERROR);
    }

    private void onShareVitalityStatusRequestCompleted(RequestResult requestResult) {
        setShareVitalityStatusRequestResult(requestResult);
        eventDispatcher.dispatchEvent(new ShareVitlaityStatusPreferenceToggleCompletedEvent());
    }

    private synchronized void setShareVitalityStatusRequestResult(RequestResult shareVitalityPreferenceRequestResult) {
        this.shareVitalityPreferenceRequestResult = shareVitalityPreferenceRequestResult;
    }

    public synchronized RequestResult geShareVitalityStatusRequestResult() {
        return shareVitalityPreferenceRequestResult;
    }
}
