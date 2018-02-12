package com.vitalityactive.va.eventsfeed.data.net;

import com.vitalityactive.va.eventsfeed.data.net.response.EventsFeedResult;
import com.vitalityactive.va.eventsfeed.data.net.request.EventsFeedRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by jayellos on 11/17/17.
 */

public interface EventsFeedApiService {

    @Headers({"Content-Type: application/json"})
    @POST("vitality-manage-events-service-v1/1.0/svc/{tenantId}/getEventByParty/{partyid}")
    Call<EventsFeedResult> getEventsFeedByParty(@Path("tenantId") long tenantId,
                                                @Path("partyid") String partyid,
                                                @Body EventsFeedRequest request,
                                                @Header("Authorization") String authorization);
}