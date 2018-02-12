package com.vitalityactive.va.termsandconditions;

import com.vitalityactive.va.networking.model.CaptureLoginEventServiceRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CaptureLoginEventService {
    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-user-service-v1/1.0/svc/{tenantId}/captureLoginEvents/{partyId}")
    Call<String> getCaptureLoginEventRequest(@Header("Authorization") String authorization,
                                             @Path("tenantId") long tenantId,
                                             @Path("partyId") long partyId,
                                             @Body CaptureLoginEventServiceRequest requestBody);
}
