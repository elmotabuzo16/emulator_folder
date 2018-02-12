package com.vitalityactive.va.vhc.service;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestFailedEvent;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepository;

import javax.inject.Inject;

import retrofit2.Call;

public class HealthAttributeServiceClient implements WebServiceResponseParser<HealthAttributeResponse> {

    private final WebServiceClient webServiceClient;
    private final HealthAttributeService service;
    private final PartyInformationRepository partyInformationRepository;
    private final EventDispatcher eventDispatcher;
    private final AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;
    private final HealthAttributeRepository healthAttributeRepository;
    private Call<HealthAttributeResponse> healthAttributeRequest;

    @Inject
    public HealthAttributeServiceClient(WebServiceClient webServiceClient,
                                        HealthAttributeService service,
                                        PartyInformationRepository partyInformationRepository,
                                        EventDispatcher eventDispatcher,
                                        AccessTokenAuthorizationProvider accessTokenAuthorizationProvider,
                                        HealthAttributeRepository healthAttributeRepository
    ) {
        this.webServiceClient = webServiceClient;
        this.service = service;
        this.partyInformationRepository = partyInformationRepository;
        this.eventDispatcher = eventDispatcher;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
        this.healthAttributeRepository = healthAttributeRepository;
    }

    public void fetchHealthAttributeEventTypes(int[] configuredVHCFeatureTypeKeys) {
        healthAttributeRequest =
                service.getHealthAttributeRequest(accessTokenAuthorizationProvider.getAuthorization(),
                        partyInformationRepository.getTenantId(),
                        partyInformationRepository.getPartyId(),
                        new HealthAttributeServiceRequestBody(configuredVHCFeatureTypeKeys));

        webServiceClient.executeAsynchronousRequest(healthAttributeRequest, this);
    }

    @Override
    public void parseResponse(HealthAttributeResponse healthAttributeResponse) {
        healthAttributeRepository.persistHealthAttributeResponse(healthAttributeResponse);
        eventDispatcher.dispatchEvent(new HealthAttributeRequestSuccess());
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        eventDispatcher.dispatchEvent(new RequestFailedEvent(RequestFailedEvent.Type.GENERIC_ERROR));
    }

    @Override
    public void handleGenericError(Exception exception) {
        eventDispatcher.dispatchEvent(new RequestFailedEvent(RequestFailedEvent.Type.GENERIC_ERROR));
    }

    @Override
    public void handleConnectionError() {
        eventDispatcher.dispatchEvent(new RequestFailedEvent(RequestFailedEvent.Type.CONNECTION_ERROR));
    }

    public void cancelRequest() {
        if (healthAttributeRequest != null) {
            healthAttributeRequest.cancel();
            healthAttributeRequest = null;
        }
    }
}
