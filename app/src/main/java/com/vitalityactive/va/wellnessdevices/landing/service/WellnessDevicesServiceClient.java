package com.vitalityactive.va.wellnessdevices.landing.service;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class WellnessDevicesServiceClient {
    private final WebServiceClient webServiceClient;
    private final PartyInformationRepository partyInformationRepository;
    private final WellnessDevicesService service;
    private Call<GetFullListResponse> getFullListRequest;
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;

    @Inject
    public WellnessDevicesServiceClient(WebServiceClient webServiceClient,
                                        PartyInformationRepository partyInformationRepository,
                                        WellnessDevicesService service,
                                        AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        this.webServiceClient = webServiceClient;
        this.partyInformationRepository = partyInformationRepository;
        this.service = service;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    public boolean isRequestInProgress() {
        return getFullListRequest != null &&
                webServiceClient.isRequestInProgress(getFullListRequest.request().toString());
    }

    public void getFullList(WebServiceResponseParser<GetFullListResponse> responseParser) {
        getFullListRequest = getFullListResponseCall();
        webServiceClient.executeAsynchronousRequest(getFullListRequest, responseParser);
    }

    private Call<GetFullListResponse> getFullListResponseCall() {
        long tenantId = partyInformationRepository.getTenantId();
        long partyId = partyInformationRepository.getPartyId();
        String authorization = accessTokenAuthorizationProvider.getAuthorization();
        return service.getFullList(tenantId, partyId, authorization);
    }

}
