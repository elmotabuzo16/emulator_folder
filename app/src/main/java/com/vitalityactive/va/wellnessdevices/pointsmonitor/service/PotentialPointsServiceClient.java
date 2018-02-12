package com.vitalityactive.va.wellnessdevices.pointsmonitor.service;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class PotentialPointsServiceClient {
    private final static String GET_POTENTIAL_POINTS_JSON = "{\"eventType\":[{\"typeKey\":121}]}";

    private final WebServiceClient webServiceClient;
    private final PartyInformationRepository partyInformationRepository;
    private final PotentialPointsService service;
    private Call<EventType> potentialPointsRequest;
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;

    @Inject
    public PotentialPointsServiceClient(WebServiceClient webServiceClient,
                                        PartyInformationRepository partyInformationRepository,
                                        PotentialPointsService service,
                                        AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        this.webServiceClient = webServiceClient;
        this.partyInformationRepository = partyInformationRepository;
        this.service = service;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    public void getPotentialPoints(int typeKey,
                                   WebServiceResponseParser<EventType> responseParser) {
        potentialPointsRequest = getPotentialPointsRequest(typeKey);
        webServiceClient.executeAsynchronousRequest(potentialPointsRequest, responseParser);
    }

    public boolean isRequestInProgress() {
        return potentialPointsRequest != null &&
                webServiceClient.isRequestInProgress(potentialPointsRequest.request().toString());
    }

    private Call<EventType> getPotentialPointsRequest(int typeKey) {
        long tenantId = partyInformationRepository.getTenantId();
        long partyId = partyInformationRepository.getPartyId();
        String authorization = accessTokenAuthorizationProvider.getAuthorization();
        return service.getPotentialPoints(tenantId, partyId, authorization, GET_POTENTIAL_POINTS_JSON);
    }
}
