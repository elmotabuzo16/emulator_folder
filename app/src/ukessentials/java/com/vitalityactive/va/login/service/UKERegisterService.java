package com.vitalityactive.va.login.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UKERegisterService {
    @Headers({"Content-Type: application/json"})
    @POST("tstc-integration-platform-services-service-ex/1.0/register")
    Call<LoginDetailsForTokenResponse> getLoginDetailsForTokenRequest(@Body LoginDetailsForTokenRequest loginDetailsForTokenRequest, @Header("Authorization") String authorizationHeader);
}
