package com.vitalityactive.va.appconfig;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.AppConfigResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class AppConfigServiceClient {
    private final WebServiceClient webServiceClient;
    private final AppConfigService appConfigService;
    private final PartyInformationRepository partyInformationRepository;
    private final AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;

    @Inject
    public AppConfigServiceClient(WebServiceClient webServiceClient, AppConfigService appConfigService, PartyInformationRepository partyInformationRepository, AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        this.webServiceClient = webServiceClient;
        this.appConfigService = appConfigService;
        this.partyInformationRepository = partyInformationRepository;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    public void updateAppConfig(WebServiceResponseParser<AppConfigResponse> parser) {
        long tenantId = partyInformationRepository.getTenantId();
        String authorization = accessTokenAuthorizationProvider.getAuthorization();
        Call<AppConfigResponse> request = appConfigService.getAppConfigRequest(tenantId, authorization);
        webServiceClient.executeAsynchronousRequest(request, parser);
    }
}
