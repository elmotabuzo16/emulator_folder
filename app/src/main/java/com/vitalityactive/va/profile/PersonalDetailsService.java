package com.vitalityactive.va.profile;

import com.vitalityactive.va.networking.model.ChangeEmailRequest;
import com.vitalityactive.va.networking.model.PersonalDetailsRequest;
import com.vitalityactive.va.networking.model.PersonalDetailsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PersonalDetailsService{

    @Headers({"Content-Type: application/json"})
    @POST("tstc-integration-platform-services-service-v1/1.0/changeUsername")
    Call<PersonalDetailsResponse.ChangeEmailResponse> getChangeEmailRequest(@Body ChangeEmailRequest changeEmailRequest,
                                                           @Header("Authorization") String authorizationHeader);

    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-party-information-service-1/1.0/svc/{tenant}/getPartyByEmail")
    Call<PersonalDetailsResponse.VerifyResponse> getVerifyNewEmailRequest(@Path("tenant") long tenantId,
                                                          @Body PersonalDetailsRequest verifyNewEmailRequest,
                                                          @Header("Authorization") String authorizationHeader);
}