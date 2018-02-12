package com.vitalityactive.va.snv.service;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by paule.glenn.s.acuin on 11/23/2017.
 */

public interface HealthAttributeService {
    @Headers({"Content-Type: application/json"})
    @POST("vitality-event-points-services-service-1/1.0/svc/{tenantId}/getPotentialPointsEventsCompletedAndHealthyRanges/{partyId}")
    Call<HealthAttributeResponse> getHealthAttributeRequest(@Header("Authorization") String authorization,
                                                            @Path("tenantId") long tenantId,
                                                            @Path("partyId") long partyId,
                                                            @Body HealthAttributeServiceRequestBody healthAttributeServiceRequestBody);

    //@GET /svc/{tenant}/getHomeScreenGetPointsGetStatus/{partyId}/{vitalitymembershipId}
}
