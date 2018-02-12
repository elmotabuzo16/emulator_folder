package com.vitalityactive.va.vhc.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HealthAttributeService {
    @Headers({"Content-Type: application/json"})
    @POST("vitality-event-points-services-service-1/1.0/svc/{tenantId}/getPotentialPointsEventsCompletedAndHealthyRanges/{partyId}")
    Call<HealthAttributeResponse> getHealthAttributeRequest(@Header("Authorization") String authorization,
                                                            @Path("tenantId") long tenantId,
                                                            @Path("partyId") long partyId,
                                                            @Body HealthAttributeServiceRequestBody healthAttributeServiceRequestBody);


   }
