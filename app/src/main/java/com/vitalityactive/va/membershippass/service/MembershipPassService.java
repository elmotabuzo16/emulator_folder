package com.vitalityactive.va.membershippass.service;

import com.vitalityactive.va.membershippass.model.MembershipPassResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by christian.j.p.capin on 11/16/2017.
 */

public interface MembershipPassService {
    @SuppressWarnings("SpellCheckingInspection")
    @Headers({"Content-Type: application/json"})
    @GET("vitality-party-membership-services-service-v1/1.0/svc/{tenant}/getMemberProfile/{partyId}")
    Call<MembershipPassResponse> getVitalityMembershipById(@Path("tenant") long tenantId,
                                                           @Path("partyId") long vitalityMembershipId,
                                                           @Header("Authorization") String authorization);
}
