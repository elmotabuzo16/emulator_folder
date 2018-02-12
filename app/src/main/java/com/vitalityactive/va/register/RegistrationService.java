package com.vitalityactive.va.register;

import com.vitalityactive.va.networking.model.RegistrationServiceRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RegistrationService {
    @Headers({"Content-Type: application/json"})
    @POST("tstc-integration-platform-services-service-v1/1.0/register")
    Call<String> getRegistrationRequest(@Body RegistrationServiceRequest registrationServiceRequest, @Header("Authorization") String authorization);
}
