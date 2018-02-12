package com.vitalityactive.va.help.service;

import android.annotation.SuppressLint;
import android.util.Log;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.help.model.HelpResponse;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.search.ContentHelpResponse;

import javax.inject.Inject;

import retrofit2.Call;

/**
 * Created by christian.j.p.capin on 11/30/2017.
 */

public class HelpServiceClient {
    private final WebServiceClient webServiceClient;
    private ServiceGenerator serviceGenerator;
    private Call<HelpResponse> helpRequest;
    private Call<ContentHelpResponse> detailsRequest;
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;

    private static final String TAG = "HelpServiceClient";

    @Inject
    public HelpServiceClient(WebServiceClient webServiceClient,
                             ServiceGenerator serviceGenerator,
                             AccessTokenAuthorizationProvider accessTokenAuthorizationProvider){


        this.webServiceClient = webServiceClient;
        this.serviceGenerator = serviceGenerator;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    @SuppressLint("LongLogTag")
    public boolean isHelpRequestInProgress() {
        Log.d(TAG, "isHelpRequestInProgreses");
        return helpRequest != null && webServiceClient.isRequestInProgress(helpRequest.request().toString());
    }

    @SuppressLint("LongLogTag")
    public boolean isDetailsRequestInProgress() {
        Log.d(TAG, "isDetailsRequestInProgreses");
        return detailsRequest != null && webServiceClient.isRequestInProgress(detailsRequest.request().toString());
    }

    public void fetchHelpDetails(WebServiceResponseParser<HelpResponse> responseParser) {
        String tagName = "help";
        helpRequest = getHelpResponseCall(tagName);
        webServiceClient.executeAsynchronousRequest(helpRequest, responseParser);
    }

    public void fetchHelpContents(String tagkey, String tagName, WebServiceResponseParser<ContentHelpResponse> responseParser){
        detailsRequest = getDetailsResponseCall(tagkey, tagName);
        webServiceClient.executeAsynchronousRequest(detailsRequest, responseParser);
    }

    @SuppressLint("LongLogTag")
    private Call<HelpResponse> getHelpResponseCall(String tagName) {
        long groupId = 20143;
        return serviceGenerator.create(HelpService.class).getHelpSuggestions(tagName, groupId, accessTokenAuthorizationProvider.getAuthorization());
    }

    @SuppressLint("ContentHelpResponse")
    private Call<ContentHelpResponse> getDetailsResponseCall(String tagkey, String tagName) {
        long groupId = 20143;
        return serviceGenerator.create(HelpService.class).getHelpDetails(accessTokenAuthorizationProvider.getAuthorization(), groupId, tagkey, tagName);
    }
}
