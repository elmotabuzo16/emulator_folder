package com.vitalityactive.va.forgotpassword.service;

import com.vitalityactive.va.networking.model.ForgotPasswordRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ForgotPasswordService {
    @Headers({"Content-Type: application/json"})
    @POST("tstc-integration-platform-services-service-v1/1.0/forgotPassword")
    Call<String> getForgotPasswordRequest(@Body ForgotPasswordRequest request, @Header("Authorization") String authorizationHeader);
}
