package com.vitalityactive.va.partnerjourney.service;

import android.support.annotation.NonNull;

import com.vitalityactive.va.partnerjourney.service.models.PartnerDetailResponse;
import com.vitalityactive.va.partnerjourney.service.models.PartnerListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PartnerRetrievalService {
    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-vitality-products-service-1/1.0/svc/{tenantId}/getPartnersByCategory/{partyId}")
    Call<PartnerListResponse> getPartners(@NonNull @Header("Authorization") String authorization, @Path("tenantId") long tenantId, @Path("partyId") long partyId, @NonNull @Body PartnerListRequest request);

    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-vitality-products-service-1/1.0/svc/{tenantId}/getEligibilityContent/{partyId}/{vitalityMembershipId}")
    Call<PartnerDetailResponse> getPartnerDetails(@Header("Authorization") String authorization, @Path("tenantId") long tenantId, @Path("partyId") long partyId, @Path("vitalityMembershipId") String vitalityMembershipId, @NonNull @Body PartnerDetailRequest request);
}
