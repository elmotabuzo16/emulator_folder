package com.vitalityactive.va.register;

import com.vitalityactive.va.authentication.BasicAuthorizationProvider;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.RegistrationServiceRequest;
import com.vitalityactive.va.register.entity.RegistrationCredentials;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class RegistrationServiceClient {
    private final WebServiceClient webServiceClient;
    private BasicAuthorizationProvider basicAuthorizationProvider;
    private Call<String> registrationRequest;
    private ServiceGenerator serviceGenerator;

    @Inject
    public RegistrationServiceClient(WebServiceClient webServiceClient, BasicAuthorizationProvider basicAuthorizationProvider, ServiceGenerator serviceGenerator) {
        this.webServiceClient = webServiceClient;
        this.basicAuthorizationProvider = basicAuthorizationProvider;
        this.serviceGenerator = serviceGenerator;
    }

    public void register(WebServiceResponseParser<String> parser, RegistrationCredentials registrationCredentials) {
        registrationRequest = serviceGenerator.create(RegistrationService.class).getRegistrationRequest(new RegistrationServiceRequest(registrationCredentials), basicAuthorizationProvider.getAuthorization());
        webServiceClient.executeAsynchronousRequest(registrationRequest, parser);
    }

    public boolean isRegistering() {
        return registrationRequest != null && webServiceClient.isRequestInProgress(registrationRequest.request().toString());
    }
}
