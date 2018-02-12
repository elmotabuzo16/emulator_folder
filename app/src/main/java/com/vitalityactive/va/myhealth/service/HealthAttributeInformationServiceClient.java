package com.vitalityactive.va.myhealth.service;


import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.myhealth.dto.MyHealthRepository;
import com.vitalityactive.va.myhealth.events.HealthAttributeFeedbackTipsEvent;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.HealthAttributeInformationRequest;
import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class HealthAttributeInformationServiceClient implements WebServiceResponseParser<HealthAttributeInformationResponse> {

    private final AccessTokenAuthorizationProvider authorizationProvider;
    private final PartyInformationRepository partyInformationRepository;
    private final EventDispatcher eventDispatcher;
    private final MyHealthRepository myHealthRepository;
    private RequestResult healthAttributeTipsResult;
    private HealthAttributeFeedbackService attributeFeedbackService;
    private Call<HealthAttributeInformationResponse> healthAttributeTipRequest;
    private WebServiceClient webServiceClient;


    @Inject
    public HealthAttributeInformationServiceClient(WebServiceClient webServiceClient,
                                                   HealthAttributeFeedbackService attributeFeedbackService,
                                                   PartyInformationRepository partyInformationRepository,
                                                   EventDispatcher eventDispatcher,
                                                   AccessTokenAuthorizationProvider authorizationProvider, MyHealthRepository myHealthRepository) {
        this.webServiceClient = webServiceClient;
        this.attributeFeedbackService = attributeFeedbackService;
        this.authorizationProvider = authorizationProvider;
        this.partyInformationRepository = partyInformationRepository;
        this.eventDispatcher = eventDispatcher;
        this.webServiceClient = webServiceClient;
        this.attributeFeedbackService = attributeFeedbackService;
        this.myHealthRepository = myHealthRepository;
    }

    public void fetchHealthAttributeFeedbackTips(List<Long> typeKeyValues, boolean summary) {
        HealthAttributeInformationRequest healthAttributeInformationRequest = new HealthAttributeInformationRequest();
        healthAttributeInformationRequest.sectionKeys = typeKeyValues.toArray(new Long[typeKeyValues.size()]);
        healthAttributeInformationRequest.summary = summary;
        healthAttributeTipRequest =
                attributeFeedbackService.getHealthAttributesInformation(authorizationProvider.getAuthorization(),
                        partyInformationRepository.getTenantId(),
                        partyInformationRepository.getPartyId(),
                        healthAttributeInformationRequest);
        webServiceClient.executeAsynchronousRequest(healthAttributeTipRequest, this);
    }

    @Override
    public void parseResponse(HealthAttributeInformationResponse healthAttributeInformationResponse) {
        myHealthRepository.persistHealthAttributeTipResponse(healthAttributeInformationResponse);
        onHealthAttributeFeedbackResultCompleted(RequestResult.SUCCESSFUL);
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        onHealthAttributeFeedbackResultCompleted(RequestResult.GENERIC_ERROR);
    }

    @Override
    public void handleGenericError(Exception exception) {
        onHealthAttributeFeedbackResultCompleted(RequestResult.GENERIC_ERROR);
    }

    @Override
    public void handleConnectionError() {
        onHealthAttributeFeedbackResultCompleted(RequestResult.CONNECTION_ERROR);
    }

    public synchronized RequestResult getHealthAtttributeTipsFeedbackResult() {
        return healthAttributeTipsResult;
    }


    public synchronized void setHealthAttributeTipsResult(RequestResult healthAttributeTipsResult) {
        this.healthAttributeTipsResult = healthAttributeTipsResult;
    }

    private void onHealthAttributeFeedbackResultCompleted(RequestResult requestResult) {
        setHealthAttributeTipsResult(requestResult);
        eventDispatcher.dispatchEvent(new HealthAttributeFeedbackTipsEvent());
    }

    public boolean isFetchingHealthAttributeInformation() {
        return healthAttributeTipRequest != null && webServiceClient.isRequestInProgress(healthAttributeTipRequest.request().toString());
    }

    public void cancel() {
        if (healthAttributeTipRequest != null) {
            healthAttributeTipRequest.cancel();
            healthAttributeTipRequest = null;
        }
    }
}
