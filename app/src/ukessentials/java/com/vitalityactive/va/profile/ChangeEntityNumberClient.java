package com.vitalityactive.va.profile;

import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.ChangeEntityNumberRequest;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class ChangeEntityNumberClient {

    private WebServiceClient webServiceClient;
    private ServiceGenerator serviceGenerator;
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;

    @Inject
    ChangeEntityNumberClient(WebServiceClient webServiceClient, ServiceGenerator serviceGenerator, AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        this.webServiceClient = webServiceClient;
        this.serviceGenerator = serviceGenerator;
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
    }

    void addNewNumber(String partyId, String dateOfBirth, String entityNumber, WebServiceResponseParser<Void> parser){

        ChangeEntityNumberRequest changeEntityNumberRequest = new ChangeEntityNumberRequest(partyId, dateOfBirth, entityNumber);

        Call<Void> request = serviceGenerator.create(ChangeEntityNumberService.class).getAddEntityRequest(changeEntityNumberRequest, accessTokenAuthorizationProvider.getAuthorization());
        webServiceClient.executeAsynchronousRequest(request, parser);
    }

    void changeNewNumber(String partyId, String dateOfBirth, String entityNumber, WebServiceResponseParser<Void> parser){

        ChangeEntityNumberRequest changeEntityNumberRequest = new ChangeEntityNumberRequest(partyId, dateOfBirth, entityNumber);

        Call<Void> request = serviceGenerator.create(ChangeEntityNumberService.class).getChangeEntityRequest(changeEntityNumberRequest, accessTokenAuthorizationProvider.getAuthorization());
        webServiceClient.executeAsynchronousRequest(request, parser);
    }



}
