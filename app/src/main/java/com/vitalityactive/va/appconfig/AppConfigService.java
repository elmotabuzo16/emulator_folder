package com.vitalityactive.va.appconfig;

import com.vitalityactive.va.networking.model.AppConfigResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface AppConfigService {
    @Headers({"Content-Type: application/json"})
    @GET("vitality-application-configuration-service-1/1.0/svc/{tenant}/getApplicationConfiguration")
    Call<AppConfigResponse> getAppConfigRequest(@Path("tenant") long tenantId,
                                                @Header("Authorization") String authorizationHeader);
}
