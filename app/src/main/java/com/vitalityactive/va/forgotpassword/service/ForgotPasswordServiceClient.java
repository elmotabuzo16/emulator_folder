package com.vitalityactive.va.forgotpassword.service;

import com.vitalityactive.va.authentication.BasicAuthorizationProvider;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.ForgotPasswordRequest;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class ForgotPasswordServiceClient {
    private final WebServiceClient webServiceClient;
    private final BasicAuthorizationProvider authorizationProvider;
    private ServiceGenerator serviceGenerator;
    private Call<String> forgotPasswordRequest;

    @Inject
    public ForgotPasswordServiceClient(WebServiceClient webServiceClient, BasicAuthorizationProvider authorizationProvider, ServiceGenerator serviceGenerator) {
        this.webServiceClient = webServiceClient;
        this.authorizationProvider = authorizationProvider;
        this.serviceGenerator = serviceGenerator;
    }

    public void forgotPassword(String username, WebServiceResponseParser<String> parser) {
        forgotPasswordRequest = getForgotPasswordRequest(username);
        webServiceClient.executeAsynchronousRequest(forgotPasswordRequest, parser);
    }

    private Call<String> getForgotPasswordRequest(String username) {
        return serviceGenerator.create(ForgotPasswordService.class).getForgotPasswordRequest(new ForgotPasswordRequest(username), authorizationProvider.getAuthorization());
    }

    public boolean isRequestInProgress() {
        return forgotPasswordRequest != null && webServiceClient.isRequestInProgress(forgotPasswordRequest.request().toString());
    }
}
