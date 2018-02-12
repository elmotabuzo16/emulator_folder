package com.vitalityactive.va.home.service;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class HomeScreenCardStatusServiceClient {
    private final WebServiceClient webServiceClient;
    private final PartyInformationRepository partyInformationRepository;
    private final HomeScreenCardStatusService service;
    private Call<HomeScreenCardStatusResponse> request;
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;

    @Inject
    public HomeScreenCardStatusServiceClient(WebServiceClient webServiceClient,
                                             PartyInformationRepository partyInformationRepository,
                                             HomeScreenCardStatusService service,
                                             AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        this.webServiceClient = webServiceClient;
        this.partyInformationRepository = partyInformationRepository;
        this.service = service;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    public boolean isRequestInProgress() {
        return request != null && webServiceClient.isRequestInProgress(request.request().toString());
    }

    public void fetchHomeCards(WebServiceResponseParser<HomeScreenCardStatusResponse> responseParser) {
        request = getHomeScreenCardStatusResponseCall();
        webServiceClient.executeAsynchronousRequest(request, responseParser);
    }

    private Call<HomeScreenCardStatusResponse> getHomeScreenCardStatusResponseCall() {
        long tenantId = partyInformationRepository.getTenantId();
        long partyId = partyInformationRepository.getPartyId();
        String vitalityMembershipId = partyInformationRepository.getVitalityMembershipId();
        return service.getHomeScreenCardStatus(tenantId, partyId, vitalityMembershipId, accessTokenAuthorizationProvider.getAuthorization());
    }
}
