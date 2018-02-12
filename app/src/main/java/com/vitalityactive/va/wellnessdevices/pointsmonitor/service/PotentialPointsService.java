package com.vitalityactive.va.wellnessdevices.pointsmonitor.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PotentialPointsService {
    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-vitality-points-service-1/1.0/svc/{tenant}/getPotentialPointsByEventType/{partyId}")
    Call<EventType> getPotentialPoints(@Path("tenant") long tenantId,
                                       @Path("partyId") long partyId,
                                       @Header("Authorization") String authorization,
                                       @Body String request);
}
