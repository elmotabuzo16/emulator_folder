package com.vitalityactive.va.snv.onboarding.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by kerry.e.lawagan on 11/24/2017.
 */

public interface GetPotentialPointsAndEventsCompletedPointsService {

    @Headers({"Content-Type: application/json"})
    @POST("vitality-event-points-services-service-1/1.0/svc/{tenantId}/getPotentialPointsAndEventsCompletedPoints/{partyId}")
    Call<GetPotentialPointsAndEventsCompletedPointsResponse> getPotentialPointsAndEventsCompletedPointsRequest(@Header("Authorization") String authorization,
                                                 @Path("tenantId") long tenantId,
                                                 @Path("partyId") long partyId,
                                                 @Body GetPotentialPointsAndEventsCompletedPointsRequest request);
}
