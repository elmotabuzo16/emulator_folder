package com.vitalityactive.va.activerewards.landing.service;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.constants.Goal;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.goalandprogress.GetGoalProgressAndDetailsResponse;
import com.vitalityactive.va.activerewards.CalendarUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class GoalsProgressServiceClient {
    private final WebServiceClient webServiceClient;
    private final PartyInformationRepository partyInformationRepository;
    private final GoalsProgressService service;
    private Call<GetGoalProgressAndDetailsResponse> getGoalProgressAndDetailsRequest;
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;

    @Inject
    public GoalsProgressServiceClient(WebServiceClient webServiceClient,
                                      PartyInformationRepository partyInformationRepository,
                                      GoalsProgressService service,
                                      AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        this.webServiceClient = webServiceClient;
        this.partyInformationRepository = partyInformationRepository;
        this.service = service;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    public boolean isRequestInProgress() {
        return getGoalProgressAndDetailsRequest != null &&
                webServiceClient.isRequestInProgress(getGoalProgressAndDetailsRequest.request().toString());
    }

    public void stopRequest(){
        if(isRequestInProgress()) {
            webServiceClient.cancelRequest(getGoalProgressAndDetailsRequest.request().toString());
        }
    }

    public void getGoalProgressAndDetails(WebServiceResponseParser<GetGoalProgressAndDetailsResponse> responseParser) {
        getGoalProgressAndDetailsRequest = getGoalProgressAndDetailsResponseCall();
        webServiceClient.executeAsynchronousRequest(getGoalProgressAndDetailsRequest, responseParser);
    }

    public void getGoalProgressAndDetails(WebServiceResponseParser<GetGoalProgressAndDetailsResponse> responseParser,
                                          String date1,
                                          String date2) {
        getGoalProgressAndDetailsRequest = getGoalProgressAndDetailsResponseCall(date1, date2);
        webServiceClient.executeAsynchronousRequest(getGoalProgressAndDetailsRequest, responseParser);
    }

    private Call<GetGoalProgressAndDetailsResponse> getGoalProgressAndDetailsResponseCall() {
        // for tests use "2017-08-01", "2017-08-15"
        EffectiveDate week = CalendarUtils.getCurrentWeekRange();
        return getGoalProgressAndDetailsResponseCall(week.effectiveFrom, week.effectiveTo);
    }

    private Call<GetGoalProgressAndDetailsResponse> getGoalProgressAndDetailsResponseCall(String date1, String date2) {
        long tenantId = partyInformationRepository.getTenantId();
        String authorization = accessTokenAuthorizationProvider.getAuthorization();
        // for testing with wiremock use hardcoded PartyId == 15282952
        GoalProgressAndDetailsRequestBody goalProgressAndDetailsRequestBody = new GoalProgressAndDetailsRequestBody(Goal._ACTIVEREWARDS,
                partyInformationRepository.getPartyId(),
                new EffectiveDate(date1, date2));
        return service.getGoalProgressAndDetails(tenantId, authorization, goalProgressAndDetailsRequestBody);
    }
}
