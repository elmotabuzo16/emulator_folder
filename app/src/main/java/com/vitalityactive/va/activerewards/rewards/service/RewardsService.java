package com.vitalityactive.va.activerewards.rewards.service;

import android.support.annotation.NonNull;

import com.vitalityactive.va.networking.model.UpdatePartyServiceRequest;
import com.vitalityactive.va.partnerjourney.service.models.PartnerListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RewardsService {
    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-rewards-service/1.0/svc/{tenantId}/getAwardedRewardByPartyId?currentRewards=true")
    Call<RewardsServiceResponse> getCurrentRewardsRequest(@Path("tenantId") long tenantId,
                                                          @NonNull @Header("Authorization") String authorization,
                                                          @NonNull @Body RewardsServiceRequest body);

    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-vitality-products-service-1/1.0/svc/{tenantId}/getPartnersByCategory/{partyId}")
    Call<PartnerListResponse> getActiveRewardsPartners(@NonNull @Header("Authorization") String authorization,
                                                       @Path("tenantId") long tenantId,
                                                       @Path("partyId") long partyId,
                                                       @NonNull @Body PartnersServiceRequest request);

    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-rewards-service/1.0/svc/{tenantId}/getAwardedRewardByPartyId?history=true")
    Call<RewardsServiceResponse> getRewardsHistoryRequest(@Path("tenantId") long tenantId,
                                                          @NonNull @Header("Authorization") String authorization,
                                                          @NonNull @Body RewardsServiceRequest body);

    @GET("vitality-manage-party-information-service-1/1.0/svc/{tenantId}/getPartyById/{partyId}")
    Call<PartnerEmailResponse> getPartnerRegisteredEmailRequest(@NonNull @Header("Authorization") String authorization, @Path("tenantId") long tenantId, @Path("partyId") long partyId);

    @Headers({"Content-Type: application/json"})
    @POST("vitality-party-party-information-services-service-1/1.0/svc/{tenantId}/updateParty/{partyId}")
    Call<String> getUpdatePartnerRegisteredEmailRequest(@Header("Authorization") String authorization,
                                                 @Path("tenantId") long tenantId,
                                                 @Path("partyId") long partyId,
                                                 @Body UpdatePartyServiceRequest request);

    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-rewards-service/1.0/svc/{tenantId}/exchangeReward/{awardedRewardId}/{rewardValueLinkId}?exchangeTypeKey=1")
    Call<SelectVoucherServiceResponse> getSelectVoucherRequest(@NonNull @Header("Authorization") String authorization, @Path("tenantId") long tenantId, @Path("awardedRewardId") long awardedRewardId, @Path("rewardValueLinkId") long rewardValueLinkId);

    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-rewards-service/1.0/svc/{tenantId}/getAwardedRewardByID")
    Call<SingleAwardedRewardServiceResponse> getSingleAwardedRewardRequest(@NonNull @Header("Authorization") String authorization, @Path("tenantId") long tenantId, @NonNull @Body RewardVoucherServiceRequest body, @Query("awardedRewardId") long awardedRewardId);

    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-rewards-service/1.0/svc/{tenantId}/updateRewardStatus?noReward=true")
    Call<String> getMarkNoRewardWheelSpinUsedRequest(@NonNull @Header("Authorization") String authorization, @Path("tenantId") long tenantId, @NonNull @Body MarkNoRewardWheelSpinUsedRequest body);
}
