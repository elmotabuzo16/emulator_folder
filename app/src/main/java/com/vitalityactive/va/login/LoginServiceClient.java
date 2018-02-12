package com.vitalityactive.va.login;

import com.vitalityactive.va.authentication.BasicAuthorizationProvider;
import com.vitalityactive.va.login.LoginService;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.LoginServiceRequest;
import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.register.entity.Password;
import com.vitalityactive.va.register.entity.Username;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class LoginServiceClient {
    private WebServiceClient webServiceClient;
    private Call<LoginServiceResponse> loginRequest;
    private BasicAuthorizationProvider authorizationProvider;
    private ServiceGenerator serviceGenerator;

    @Inject
    public LoginServiceClient(WebServiceClient webServiceClient, BasicAuthorizationProvider authorizationProvider, ServiceGenerator serviceGenerator) {
        this.webServiceClient = webServiceClient;
        this.authorizationProvider = authorizationProvider;
        this.serviceGenerator = serviceGenerator;
    }

    public boolean isAuthenticating() {
        return loginRequest != null && webServiceClient.isRequestInProgress(loginRequest.request().toString());
    }

    public void authenticate(Username username, Password password, WebServiceResponseParser<LoginServiceResponse> parser) {
        LoginServiceRequest request = new LoginServiceRequest(new LoginServiceRequest.LoginRequest(username, password));
        String authorizationHeader = authorizationProvider.getAuthorization();
        loginRequest = serviceGenerator.create(LoginService.class).getLoginRequest(request, authorizationHeader);
        webServiceClient.executeAsynchronousRequest(loginRequest, parser);
    }
}
