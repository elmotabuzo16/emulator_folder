package com.vitalityactive.va.activerewards.rewards.service;

import android.support.annotation.NonNull;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.activerewards.landing.service.EffectiveDate;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.dependencyinjection.ar.ActiveRewardsScope;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.UpdatePartyServiceRequest;
import com.vitalityactive.va.partnerjourney.PartnerType;
import com.vitalityactive.va.partnerjourney.service.models.PartnerListResponse;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.NonUserFacingDateFormatter;

import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Call;

@ActiveRewardsScope
public class RewardsServiceClient {
    private Call<RewardsServiceResponse> currentRewardsRequest;
    private Call<PartnerListResponse> rewardsPartnersRequest;
    private RewardsService service;
    private PartyInformationRepository partyInformationRepository;
    private AccessTokenAuthorizationProvider authorizationProvider;
    private WebServiceClient webServiceClient;
    private Call<RewardsServiceResponse> rewardsHistoryRequest;
    private Call<PartnerEmailResponse> partnerRegisteredEmailRequest;
    private HashMap<Long, Call<SingleAwardedRewardServiceResponse>> rewardVoucherRequests = new HashMap<>();

    @Inject
    RewardsServiceClient(RewardsService service,
                         PartyInformationRepository partyInformationRepository,
                         AccessTokenAuthorizationProvider authorizationProvider,
                         WebServiceClient webServiceClient) {
        this.service = service;
        this.partyInformationRepository = partyInformationRepository;
        this.authorizationProvider = authorizationProvider;
        this.webServiceClient = webServiceClient;
    }

    public void fetchCurrentRewards(WebServiceResponseParser<RewardsServiceResponse> parser) {
        String today = NonUserFacingDateFormatter.getYearMonthDayFormatter().format(LocalDate.now());
        currentRewardsRequest = service.getCurrentRewardsRequest(partyInformationRepository.getTenantId(),
                authorizationProvider.getAuthorization(),
                new RewardsServiceRequest(new EffectiveDate(today, today), partyInformationRepository.getPartyId()));
        webServiceClient.executeAsynchronousRequest(currentRewardsRequest, parser);
    }

    public void fetchActiveRewardsPartners(WebServiceResponseParser<PartnerListResponse> parser) {
        rewardsPartnersRequest = service.getActiveRewardsPartners(authorizationProvider.getAuthorization(),
                partyInformationRepository.getTenantId(), partyInformationRepository.getPartyId(),
                new PartnersServiceRequest(PartnerType.REWARDS.productFeatureCategoryTypeKey));
        webServiceClient.executeAsynchronousRequest(rewardsPartnersRequest, parser);
    }

    public void fetchRewardsHistory(WebServiceResponseParser<RewardsServiceResponse> parser) {
        String yesterday = NonUserFacingDateFormatter.getYearMonthDayFormatter().format(LocalDate.now().minusDays(1));
        String sixMonthsAgo = NonUserFacingDateFormatter.getYearMonthDayFormatter().format(LocalDate.now().minusMonths(6).toFirstDayOfMonth());
        rewardsHistoryRequest = service.getRewardsHistoryRequest(partyInformationRepository.getTenantId(),
                authorizationProvider.getAuthorization(),
                new RewardsServiceRequest(new EffectiveDate(yesterday, sixMonthsAgo), partyInformationRepository.getPartyId()));
        webServiceClient.executeAsynchronousRequest(rewardsHistoryRequest, parser);
    }

    public void fetchPartnerRegisteredEmail(WebServiceResponseParser<PartnerEmailResponse> parser) {
        partnerRegisteredEmailRequest = service.getPartnerRegisteredEmailRequest(authorizationProvider.getAuthorization(), partyInformationRepository.getTenantId(), partyInformationRepository.getPartyId());
        webServiceClient.executeAsynchronousRequest(partnerRegisteredEmailRequest, parser);
    }

    public boolean isCurrentRewardsRequestInProgress() {
        return currentRewardsRequest != null && webServiceClient.isRequestInProgress(currentRewardsRequest.request().toString());
    }

    public boolean isRewardsHistoryRequestInProgress() {
        return rewardsHistoryRequest != null && webServiceClient.isRequestInProgress(rewardsHistoryRequest.request().toString());
    }

    public boolean isPartnerRegisteredEmailRequestInProgress() {
        return partnerRegisteredEmailRequest != null && webServiceClient.isRequestInProgress(partnerRegisteredEmailRequest.request().toString());
    }

    public void updatePartnerRegisteredEmail(@NonNull String partnerRegisteredEmail, WebServiceResponseParser<String> parser) {
        UpdatePartyServiceRequest requestBody = UpdatePartyServiceRequest.builder()
                .addGeneralPreference(UpdatePartyServiceRequest.GeneralPreferenceType.STARBUCKS_EMAIL, partnerRegisteredEmail)
                .build();
        Call<String> updatePartnerRegisteredEmailRequest = service.getUpdatePartnerRegisteredEmailRequest(
                authorizationProvider.getAuthorization(),
                partyInformationRepository.getTenantId(),
                partyInformationRepository.getPartyId(),
                requestBody);
        webServiceClient.executeAsynchronousRequest(updatePartnerRegisteredEmailRequest, parser);
    }

    public void selectVoucher(long uniqueId, long rewardValueLinkId, WebServiceResponseParser<SelectVoucherServiceResponse> parser) {
        Call<SelectVoucherServiceResponse> selectVoucherRequest = service.getSelectVoucherRequest(
                authorizationProvider.getAuthorization(),
                partyInformationRepository.getTenantId(),
                uniqueId,
                rewardValueLinkId);
        webServiceClient.executeAsynchronousRequest(selectVoucherRequest, parser);
    }

    public void fetchSingleAwardedReward(long awardedRewardId, WebServiceResponseParser<SingleAwardedRewardServiceResponse> parser) {
        Call<SingleAwardedRewardServiceResponse> rewardVoucherRequest = service.getSingleAwardedRewardRequest(
                authorizationProvider.getAuthorization(),
                partyInformationRepository.getTenantId(),
                new RewardVoucherServiceRequest(awardedRewardId),
                awardedRewardId);
        if (beginFetchingRewardVoucher(awardedRewardId, rewardVoucherRequest)) {
            webServiceClient.executeAsynchronousRequest(rewardVoucherRequest, parser);
        }
    }

    private synchronized boolean beginFetchingRewardVoucher(long rewardVoucherUniqueId, Call<SingleAwardedRewardServiceResponse> rewardVoucherRequest) {
        Call<SingleAwardedRewardServiceResponse> request = rewardVoucherRequests.get(rewardVoucherUniqueId);
        if (request != null && webServiceClient.isRequestInProgress(request.request().toString())) {
            return false;
        }
        rewardVoucherRequests.put(rewardVoucherUniqueId, rewardVoucherRequest);
        return true;
    }

    public boolean isFetchingRewardVoucher(long rewardVoucherUniqueId) {
        Call<SingleAwardedRewardServiceResponse> request = getRewardVoucherRequests().get(rewardVoucherUniqueId);
        return request != null && webServiceClient.isRequestInProgress(request.request().toString());
    }

    private synchronized HashMap<Long, Call<SingleAwardedRewardServiceResponse>> getRewardVoucherRequests() {
        return rewardVoucherRequests;
    }

    public void markNoRewardWheelSpinUsed(long wheelSpinUniqueId, WebServiceResponseParser<String> parser) {
        Call<String> request = service.getMarkNoRewardWheelSpinUsedRequest(
                authorizationProvider.getAuthorization(),
                partyInformationRepository.getTenantId(),
                new MarkNoRewardWheelSpinUsedRequest(wheelSpinUniqueId, partyInformationRepository.getPartyId()));
        webServiceClient.executeAsynchronousRequest(request, parser);
    }

}
