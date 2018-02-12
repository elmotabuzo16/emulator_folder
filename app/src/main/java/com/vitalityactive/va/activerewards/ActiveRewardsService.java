package com.vitalityactive.va.activerewards;

import com.vitalityactive.va.activerewards.landing.service.ActiveRewardsActivationPayload;
import com.vitalityactive.va.networking.model.ar.ActivateServiceResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ActiveRewardsService {
    @Headers({"Content-Type: application/json",
            "Accept: application/json"
    })
    @POST("vitality-agreement-goals-services-service-1/1.0/svc/{tenantId}/activateActiveRewards/{partyId}")
    Call<ActivateServiceResponse> getActivateRequest(@Header("Authorization") String authorization,
                                                     @Path("tenantId") long tenantId,
                                                     @Path("partyId") long partyId,
                                                     @Body ActiveRewardsActivationPayload request);
}
