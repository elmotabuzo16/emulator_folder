package com.vitalityactive.va.settings;

import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.authentication.BasicAuthorizationProvider;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.ChangePasswordRequest;
import com.vitalityactive.va.networking.model.ChangePasswordResponse;
import com.vitalityactive.va.register.entity.EmailAddress;
import com.vitalityactive.va.register.entity.Password;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class ChangePasswordClient {

    private WebServiceClient webServiceClient;
    private BasicAuthorizationProvider basicAuthorizationProvider;
    private ServiceGenerator serviceGenerator;

    @Inject
    ChangePasswordClient(WebServiceClient webServiceClient, BasicAuthorizationProvider authorizationProvider, ServiceGenerator serviceGenerator) {
        this.webServiceClient = webServiceClient;
        this.basicAuthorizationProvider = authorizationProvider;
        this.serviceGenerator = serviceGenerator;

    }

    void changeUserPassword(Password existingPassword, Password newPassword, EmailAddress userName , WebServiceResponseParser<ChangePasswordResponse> parser){
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(existingPassword, newPassword, userName);
        Call<ChangePasswordResponse> request = serviceGenerator.create(ChangePasswordService.class).getChangePasswordRequest(changePasswordRequest, basicAuthorizationProvider.getAuthorization());
        webServiceClient.executeAsynchronousRequest(request, parser);
    }
}
