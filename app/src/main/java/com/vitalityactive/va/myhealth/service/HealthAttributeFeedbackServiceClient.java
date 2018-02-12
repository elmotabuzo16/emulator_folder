package com.vitalityactive.va.myhealth.service;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.myhealth.dto.MyHealthRepository;
import com.vitalityactive.va.myhealth.events.HealthAttributeFeedbackTipsEvent;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.HealthAttributeFeedbackRequest;
import com.vitalityactive.va.networking.model.HealthAttributeFeedbackResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class HealthAttributeFeedbackServiceClient implements WebServiceResponseParser<HealthAttributeFeedbackResponse> {

    private final AccessTokenAuthorizationProvider authorizationProvider;
    private final PartyInformationRepository partyInformationRepository;
    private final EventDispatcher eventDispatcher;
    MyHealthRepository myHealthRepository;
    private RequestResult healthAttributesFeedbackResult;
    private HealthAttributeFeedbackService attributeFeedbackService;
    private Call<HealthAttributeFeedbackResponse> healthAttributeRequest;
    private WebServiceClient webServiceClient;

    @Inject
    public HealthAttributeFeedbackServiceClient(WebServiceClient webServiceClient,
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

    public void fetchHealthAttributeFeedback(List<Long> typeKeyValues) {
        HealthAttributeFeedbackRequest healthAttributeFeedbackRequest = new HealthAttributeFeedbackRequest();
        List<HealthAttributeFeedbackRequest.HealthAttributeType> typeKeys = new ArrayList<>();
        for (Long typeKey : typeKeyValues) {
            HealthAttributeFeedbackRequest.HealthAttributeType healthAttributeType = new HealthAttributeFeedbackRequest.HealthAttributeType();
            healthAttributeType.typeKey = typeKey;
            typeKeys.add(healthAttributeType);
        }
        healthAttributeFeedbackRequest.healthAttributeTypes = typeKeys.toArray(new HealthAttributeFeedbackRequest.HealthAttributeType[typeKeys.size()]);

        healthAttributeRequest =
                attributeFeedbackService.getHealthAttributeFeedback(authorizationProvider.getAuthorization(),
                        partyInformationRepository.getTenantId(),
                        partyInformationRepository.getPartyId(),
                        healthAttributeFeedbackRequest);
        webServiceClient.executeAsynchronousRequest(healthAttributeRequest, this);
    }

    @Override
    public void parseResponse(HealthAttributeFeedbackResponse healthAttributeFeedbackResponse) {
        myHealthRepository.persistMostRecentHealthAttributeFeedback(healthAttributeFeedbackResponse);
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

    public synchronized RequestResult getHealthAtttributeFeedbackResult() {
        return healthAttributesFeedbackResult;
    }


    public synchronized void setHealthAttributeFeedbackResult(RequestResult healthAttributesFeedbackResult) {
        this.healthAttributesFeedbackResult = healthAttributesFeedbackResult;
    }

    private void onHealthAttributeFeedbackResultCompleted(RequestResult requestResult) {
        setHealthAttributeFeedbackResult(requestResult);
        eventDispatcher.dispatchEvent(new HealthAttributeFeedbackTipsEvent());
    }

    public void cancel() {
        if (healthAttributeRequest != null) {
            healthAttributeRequest.cancel();
            healthAttributeRequest = null;
        }
    }
}
