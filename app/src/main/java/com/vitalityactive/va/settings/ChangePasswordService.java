package com.vitalityactive.va.settings;


import com.vitalityactive.va.networking.model.ChangePasswordRequest;
import com.vitalityactive.va.networking.model.ChangePasswordResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ChangePasswordService {

    @Headers({"Content-Type: application/json"})
    @POST("tstc-integration-platform-services-service-v1/1.0/changePassword")
    Call<ChangePasswordResponse> getChangePasswordRequest(@Body ChangePasswordRequest changePasswordRequest,
                                                          @Header("Authorization") String authorizationHeader);
}
