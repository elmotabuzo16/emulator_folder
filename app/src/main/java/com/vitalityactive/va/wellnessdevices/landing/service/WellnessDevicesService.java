package com.vitalityactive.va.wellnessdevices.landing.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface WellnessDevicesService {
    @Headers({"Content-Type: application/json"})
    @GET("vdp-api/1.0/devices/tenant/{tenant}/identifier/{partyId}")
    Call<GetFullListResponse> getFullList(@Path("tenant") long tenantId,
                                          @Path("partyId") long partyId,
                                          @Header("Authorization") String authorization);
}
