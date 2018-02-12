package com.vitalityactive.va;

import com.vitalityactive.va.networking.model.EventServiceRequest;
import com.vitalityactive.va.networking.model.ProcessEventsServiceRequest;
import com.vitalityactive.va.networking.model.ProcessEventsServiceRequestSNV;
import com.vitalityactive.va.networking.model.ProcessEventsV2ServiceRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EventService {

    String ENDPOINT = "vitality-manage-events-service-v1/1.0/svc/";

    @Headers({"Content-Type: application/json"})
    @POST(ENDPOINT + "{tenantId}/createEvent")
    Call<EventServiceResponse> getEventsRequest(@Path("tenantId") long tenantId,
                                                @Header("Authorization") String authorization,
                                                @Body EventServiceRequest eventServiceRequest);

    @Headers({"Content-Type: application/json"})
    @POST(ENDPOINT + "{tenantId}/processEvents")
    Call<ProcessEventsServiceResponse> getProcessEventsRequest(@Path("tenantId") long tenantId,
                                                               @Header("Authorization") String authorization,
                                                               @Body ProcessEventsServiceRequest body);

    @Headers({"Content-Type: application/json"})
    @POST(ENDPOINT + "{tenantId}/processEvents")
    Call<ProcessEventsServiceResponse> getProcessEventsRequest(@Path("tenantId") long tenantId,
                                                               @Header("Authorization") String authorization,
                                                               @Body ProcessEventsServiceRequestSNV body);

    @Headers({"Content-Type: application/json"})
    @POST(ENDPOINT + "{tenantId}/processEventsV2")
    Call<ProcessEventsServiceResponse> getProcessEventsV2Request(@Path("tenantId") long tenantId,
                                                                 @Header("Authorization") String authorization,
                                                                 @Body ProcessEventsV2ServiceRequest processEventsServiceRequest);
}
