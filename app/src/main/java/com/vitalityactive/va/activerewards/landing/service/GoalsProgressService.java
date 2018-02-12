package com.vitalityactive.va.activerewards.landing.service;

import com.vitalityactive.va.networking.model.goalandprogress.GetGoalProgressAndDetailsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GoalsProgressService {
    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-goals-service-1/1.0/svc/{tenantId}/getGoalProgressAndDetails")
    Call<GetGoalProgressAndDetailsResponse> getGoalProgressAndDetails(@Path("tenantId") long tenantId,
                                                                      @Header("Authorization") String authorization,
                                                                      @Body GoalProgressAndDetailsRequestBody request);
}
