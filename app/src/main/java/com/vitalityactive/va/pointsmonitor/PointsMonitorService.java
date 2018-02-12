package com.vitalityactive.va.pointsmonitor;

import com.vitalityactive.va.networking.model.PointsHistoryServiceRequest;
import com.vitalityactive.va.networking.model.PointsHistoryServiceResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PointsMonitorService {
    @Headers({"Content-Type: application/json"})
    @POST("vitality-goals-points-services-service-v1/1.0/svc/{tenantId}/getAllPointsHistory/{vitalityMembershipId}")
    Call<PointsHistoryServiceResponse> getPointsHistoryRequest(@Path("tenantId") long tenantId,
                                                               @Path("vitalityMembershipId") String vitalityMembershipId,
                                                               @Body PointsHistoryServiceRequest request,
                                                               @Header("Authorization") String authorization);
}
