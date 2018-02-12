package com.vitalityactive.va.login;

import com.vitalityactive.va.TestUtilities;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CreateUserInstructionService {
    @Headers({TestUtilities.ACCESS_TOKEN,
            "Accept: application/json"})
    @POST("vitality-manage-user-service-v1/1.0/2/v1/createUserInstruction")
    Call<String> getCreateUserInstructionRequest(@Body CreateUserInstructionServiceRequest requestBody);
}
