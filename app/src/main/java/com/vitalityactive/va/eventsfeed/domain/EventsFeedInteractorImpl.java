package com.vitalityactive.va.eventsfeed.domain;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.eventsfeed.EventsFeedRepository;
import com.vitalityactive.va.eventsfeed.EventsFeedRequestCompletedEvent;
import com.vitalityactive.va.eventsfeed.data.net.EventsFeedApiServiceClient;
import com.vitalityactive.va.eventsfeed.data.net.request.EventsFeedRequest;
import com.vitalityactive.va.eventsfeed.data.net.response.EventsFeedResult;
import com.vitalityactive.va.networking.RequestFailedEvent;
import com.vitalityactive.va.networking.WebServiceResponseParser;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EventsFeedInteractorImpl implements EventsFeedInteractor, WebServiceResponseParser<EventsFeedResult> {
    private EventsFeedRepository repository;
    private EventsFeedApiServiceClient eventsFeedApiServiceClient;
    private EventDispatcher eventDispatcher;

    private PartyInformationRepository partyInformationRepository;
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;

    private Context context;

    @Inject
    public EventsFeedInteractorImpl(EventsFeedRepository repository, EventsFeedApiServiceClient eventsFeedApiServiceClient,
                                    EventDispatcher eventDispatcher, PartyInformationRepository partyInformationRepository,
                                    AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        this.repository = repository;
        this.eventsFeedApiServiceClient = eventsFeedApiServiceClient;
        this.eventDispatcher = eventDispatcher;
        this.partyInformationRepository = partyInformationRepository;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    @Override
    public boolean hasEntries() {
        return repository.hasEntries();
    }

    @Override
    public boolean isFetching() {
        return eventsFeedApiServiceClient.isFetching();
    }

    @Override
    public void fetchEventsFeed(EventsFeedRequest requestBody) {
        // VA-15847: always fetch history, even if there are present
        refresh(requestBody);
    }

    @Override
    public void refresh(EventsFeedRequest requestBody) {
        if (isFetching()) {
            return;
        }

        eventsFeedApiServiceClient.getEventsFeedByParty(this,
                partyInformationRepository.getTenantId(),
                partyInformationRepository.getPartyId()+"",
                requestBody,
                accessTokenAuthorizationProvider.getAuthorization()
        );

//        loadMockData();
    }

    @Override
    public void parseResponse(EventsFeedResult response) {
        repository.persistResponse(response);
        eventDispatcher.dispatchEvent(new EventsFeedRequestCompletedEvent());
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        eventDispatcher.dispatchEvent(new RequestFailedEvent(RequestFailedEvent.Type.GENERIC_ERROR));
    }

    @Override
    public void handleGenericError(Exception exception) {
        eventDispatcher.dispatchEvent(new RequestFailedEvent(RequestFailedEvent.Type.GENERIC_ERROR));
    }

    @Override
    public void handleConnectionError() {
        eventDispatcher.dispatchEvent(new RequestFailedEvent(RequestFailedEvent.Type.CONNECTION_ERROR));
    }

    public void loadMockData(){
        new GetMockJsonData().execute();
    }

    public String readJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("events_feed_new.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    private class GetMockJsonData extends AsyncTask<Void, Void, EventsFeedResult>{

        @Override
        protected EventsFeedResult doInBackground(Void... voids) {

            EventsFeedResult response;

            Gson gson = new Gson();
            response = gson.fromJson(readJSONFromAsset(), EventsFeedResult.class);

            repository.persistResponse(response);

            return response;
        }

        @Override
        protected void onPostExecute(EventsFeedResult eventsFeedResult) {
            super.onPostExecute(eventsFeedResult);

            eventDispatcher.dispatchEvent(new EventsFeedRequestCompletedEvent());
        }
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }
}
