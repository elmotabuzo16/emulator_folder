package com.vitalityactive.va.home.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EventByPartyService {
    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-events-service-v1/1.0/svc/{tenantId}/getEventByParty/{partyId}")
    Call<EventByPartyResponse> getEventByParty(@Path("tenantId") long tenantId,
                                               @Path("partyId") long partyid,
                                               @Header("Authorization") String authorization,
                                               @Body EventByPartyInboundPayload request);
}
