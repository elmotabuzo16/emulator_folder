package com.vitalityactive.va.profile;

import com.vitalityactive.va.networking.model.ChangeEntityNumberRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ChangeEntityNumberService {

    @Headers({"Content-Type: application/json"})
    @POST("legacy/entitymaintenance/1.0/entitynumber")
    Call<Void> getAddEntityRequest(@Body ChangeEntityNumberRequest changeEntityNumberRequest,
                                                            @Header("Authorization") String authorizationHeader);

    @Headers({"Content-Type: application/json"})
    @PUT("legacy/entitymaintenance/1.0/entitynumber")
    Call<Void> getChangeEntityRequest(@Body ChangeEntityNumberRequest changeEntityNumberRequest,
                                      @Header("Authorization") String authorizationHeader);

}
