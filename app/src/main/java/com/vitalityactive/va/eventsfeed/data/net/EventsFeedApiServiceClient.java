package com.vitalityactive.va.eventsfeed.data.net;

import com.vitalityactive.va.eventsfeed.data.net.response.EventsFeedResult;
import com.vitalityactive.va.eventsfeed.data.net.request.EventsFeedRequest;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;

import retrofit2.Call;

/**
 * Created by jayellos on 11/17/17.
 */

public class EventsFeedApiServiceClient {

    private final WebServiceClient webServiceClient;
    private Call<EventsFeedResult> serviceRequest;
    private ServiceGenerator serviceGenerator;

    public EventsFeedApiServiceClient(WebServiceClient webServiceClient, ServiceGenerator serviceGenerator) {
        this.webServiceClient = webServiceClient;
        this.serviceGenerator = serviceGenerator;
    }

    public void getEventsFeedByParty(WebServiceResponseParser<EventsFeedResult> parser, long tenantId, String partyid, EventsFeedRequest request, String authorization){
        serviceRequest = serviceGenerator.create(EventsFeedApiService.class).getEventsFeedByParty(tenantId, partyid, request, authorization);
        webServiceClient.executeAsynchronousRequest(serviceRequest, parser);
    }

    public boolean isFetching() {
        return serviceRequest != null && webServiceClient.isRequestInProgress(serviceRequest.request().toString());
    }
}
