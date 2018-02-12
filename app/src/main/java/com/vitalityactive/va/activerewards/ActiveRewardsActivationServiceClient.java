package com.vitalityactive.va.activerewards;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.activerewards.landing.service.ActiveRewardsActivationPayload;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.ar.ActivateServiceResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class ActiveRewardsActivationServiceClient {
    private final WebServiceClient webServiceClient;
    private final ActiveRewardsService activeRewardsService;
    private final AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;
    private final PartyInformationRepository partyInformationRepository;
    private Call<ActivateServiceResponse> activateRequest;

    @Inject
    ActiveRewardsActivationServiceClient(WebServiceClient webServiceClient,
                                         PartyInformationRepository partyInformationRepository,
                                         ActiveRewardsService activeRewardsService,
                                         AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        this.webServiceClient = webServiceClient;
        this.partyInformationRepository = partyInformationRepository;
        this.activeRewardsService = activeRewardsService;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    public void activate(final WebServiceResponseParser<ActivateServiceResponse> activationResponseParser) {
        activateRequest = activeRewardsService.getActivateRequest(
                accessTokenAuthorizationProvider.getAuthorization(),
                partyInformationRepository.getTenantId(),
                partyInformationRepository.getPartyId(),
                new ActiveRewardsActivationPayload(CalendarUtils.getToday(), "9999-01-01"));

        webServiceClient.executeAsynchronousRequest(activateRequest.clone(), activationResponseParser);
    }

    boolean isActivateRequestInProgress() {
        return activateRequest != null && webServiceClient.isRequestInProgress(activateRequest.request().toString());
    }
}
