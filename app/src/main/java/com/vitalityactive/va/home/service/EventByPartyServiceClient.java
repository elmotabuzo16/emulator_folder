package com.vitalityactive.va.home.service;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class EventByPartyServiceClient {
    private final WebServiceClient webServiceClient;
    private final PartyInformationRepository partyInformationRepository;
    private final EventByPartyService service;
    private Call<EventByPartyResponse> request;
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;

    @Inject
    EventByPartyServiceClient(WebServiceClient webServiceClient,
                              PartyInformationRepository partyInformationRepository,
                              EventByPartyService service,
                              AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        this.webServiceClient = webServiceClient;
        this.partyInformationRepository = partyInformationRepository;
        this.service = service;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    public boolean isRequestInProgress() {
        return request != null && webServiceClient.isRequestInProgress(request.request().toString());
    }

    public void getEventStatusByKey(final int eventTypeKey, WebServiceResponseParser<EventByPartyResponse> responseParser) {
        request = getEventStatusByKeyResponseCall(eventTypeKey);
        webServiceClient.executeAsynchronousRequest(request, responseParser);
    }

    private Call<EventByPartyResponse> getEventStatusByKeyResponseCall(int eventTypeKey) {
        long tenantId = partyInformationRepository.getTenantId();
        long partyId = partyInformationRepository.getPartyId();
        String authorization = accessTokenAuthorizationProvider.getAuthorization();
        EventByPartyInboundPayload body = new EventByPartyInboundPayload(eventTypeKey);
        return service.getEventByParty(tenantId, partyId, authorization, body);
    }
}
