package com.vitalityactive.va.snv.history.service;

import com.google.gson.Gson;
import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.dependencyinjection.NetworkModule;
import com.vitalityactive.va.home.service.EventByPartyResponse;

import java.io.IOException;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by dharel.h.rosell on 11/30/2017.
 */

public interface ScreeningAndVaccinationHistoryService {

    @Headers({"Content-Type: application/json"})
//    @POST("vitality-event-points-services-service-1/1.0/svc/{tenant}/getEventByParty/{partyid}")
    @POST("vitality-manage-events-service-v1/1.0/svc/{tenant}/getEventByParty/{partyid}")
    Call<EventByPartyResponse> getScreenAndVaccinationHistory(@Header("Authorization") String authorization,
                                                              @Path("tenant") long tenantId,
                                                              @Path("partyid") long partyId,
                                                              @Body ScreeningAndVaccinationHistoryRequest request);

}
