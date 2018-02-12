package com.vitalityactive.va.home.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface HomeScreenCardStatusService {
    @SuppressWarnings("SpellCheckingInspection")
    @Headers({"Content-Type: application/json"})
    @GET("vitality-home-screen-points-agreement-service-1/1.0/svc/{tenant}/getHomeScreenGetPointsGetStatus/{partyId}/{vitalityMembershipId}")
    Call<HomeScreenCardStatusResponse> getHomeScreenCardStatus(@Path("tenant") long tenantId,
                                                               @Path("partyId") long partyId,
                                                               @Path("vitalityMembershipId") String vitalityMembershipId,
                                                               @Header("Authorization") String authorization);
}
