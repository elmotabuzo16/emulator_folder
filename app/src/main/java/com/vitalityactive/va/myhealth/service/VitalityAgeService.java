package com.vitalityactive.va.myhealth.service;

import com.vitalityactive.va.networking.model.VitalityAgeRequest;
import com.vitalityactive.va.networking.model.VitalityAgeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface VitalityAgeService {

    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-health-service-v1/1.0/svc/{tenantId}/getHealthAttribute/{partyId}")
    Call<VitalityAgeResponse> getVitalityAgeValue(@Header("Authorization") String authorization,
                                                  @Path("tenantId") long tenantId,
                                                  @Path("partyId") long partyId, @Body VitalityAgeRequest vitalityAgeRequest);
}
