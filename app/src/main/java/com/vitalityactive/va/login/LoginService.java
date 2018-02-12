package com.vitalityactive.va.login;

import com.vitalityactive.va.networking.model.LoginServiceRequest;
import com.vitalityactive.va.networking.model.LoginServiceResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginService {

    @Headers({"Content-Type: application/json"})
    @POST("tstc-integration-platform-services-service-v1/1.0/login")
    Call<LoginServiceResponse> getLoginRequest(@Body LoginServiceRequest loginServiceRequest, @Header("Authorization") String authorizationHeader);
}
