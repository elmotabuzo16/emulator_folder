package com.vitalityactive.va.myhealth.service;


import com.vitalityactive.va.networking.model.HealthAttributeFeedbackRequest;
import com.vitalityactive.va.networking.model.HealthAttributeFeedbackResponse;
import com.vitalityactive.va.networking.model.HealthAttributeInformationRequest;
import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HealthAttributeFeedbackService {

    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-health-service-v1/1.0/svc/{tenantId}/getHealthAttributeFeedback/{partyId}")
    Call<HealthAttributeFeedbackResponse> getHealthAttributeFeedback(@Header("Authorization") String authorization,
                                                                     @Path("tenantId") long tenantId,
                                                                     @Path("partyId") long partyId, @Body HealthAttributeFeedbackRequest healthAttributeFeedbackRequest);

    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-health-service-v1/1.0/svc/{tenantId}/getHealthInformation/{partyId}")
    Call<HealthAttributeInformationResponse> getHealthAttributesInformation(@Header("Authorization") String authorization,
                                                                            @Path("tenantId") long tenantId,
                                                                            @Path("partyId") long partyId, @Body HealthAttributeInformationRequest healthAttributeInformationRequest);
}
