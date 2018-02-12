package com.vitalityactive.va.pointsmonitor;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.PointsHistoryServiceRequest;
import com.vitalityactive.va.networking.model.PointsHistoryServiceResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class PointsMonitorServiceClient {
    private final WebServiceClient webServiceClient;
    private final PointsMonitorService service;
    private PartyInformationRepository partyInformationRepository;
    private Call<PointsHistoryServiceResponse> serviceRequest;
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;

    @Inject
    public PointsMonitorServiceClient(WebServiceClient webServiceClient,
                                      PointsMonitorService service,
                                      PartyInformationRepository partyInformationRepository,
                                      AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        this.webServiceClient = webServiceClient;
        this.service = service;
        this.partyInformationRepository = partyInformationRepository;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    public boolean isFetchingPointsHistory() {
        return serviceRequest != null && webServiceClient.isRequestInProgress(serviceRequest.request().toString());
    }

    public void fetchPointsHistory(WebServiceResponseParser<PointsHistoryServiceResponse> parser) {
        serviceRequest = service.getPointsHistoryRequest(
                partyInformationRepository.getTenantId(),
                partyInformationRepository.getVitalityMembershipId(),
                new PointsHistoryServiceRequest(new long[] {0, 1}),
                accessTokenAuthorizationProvider.getAuthorization());
        webServiceClient.executeAsynchronousRequest(serviceRequest, parser);
    }
}
