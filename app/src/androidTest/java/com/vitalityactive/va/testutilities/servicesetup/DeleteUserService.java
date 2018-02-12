package com.vitalityactive.va.testutilities.servicesetup;

import com.vitalityactive.va.TestUtilities;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface DeleteUserService {
    @Headers({TestUtilities.ACCESS_TOKEN,
            "Accept: application/json"})
    @DELETE("tstc-integration-platform-services-service-v1/1.0/register")
    Call<String> getDeleteRequest(@Query(value = "userId", encoded = true) String userId);
}
