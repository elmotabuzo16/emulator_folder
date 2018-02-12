package com.vitalityactive.va.login.service;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.vitalityactive.va.authentication.BasicAuthorizationProvider;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.register.ActivationValues;

import retrofit2.Call;

public class UKERegisterServiceClient {
    private UKERegisterService service;
    private final Gson gson;
    private BasicAuthorizationProvider authorizationProvider;
    private WebServiceClient webServiceClient;

    public UKERegisterServiceClient(UKERegisterService service, Gson gson,
                                    BasicAuthorizationProvider authorizationProvider, WebServiceClient webServiceClient) {
        this.service = service;
        this.gson = gson;
        this.authorizationProvider = authorizationProvider;
        this.webServiceClient = webServiceClient;
    }

    public void getLoginDetailsForToken(@NonNull String token, WebServiceResponseParser<LoginDetailsForTokenResponse> parser) {
        LoginDetailsForTokenRequest request = new LoginDetailsForTokenRequest(token);
        getLoginDetails(parser, request);
    }

    public void getLoginDetailsForToken(@NonNull String token, ActivationValues activationValues, WebServiceResponseParser<LoginDetailsForTokenResponse> parser) {
        LoginDetailsForTokenRequest request = new LoginDetailsForTokenRequest(token, activationValues);
        getLoginDetails(parser, request);
    }

    private void getLoginDetails(WebServiceResponseParser<LoginDetailsForTokenResponse> parser, LoginDetailsForTokenRequest request) {
        Call<LoginDetailsForTokenResponse> call = service.getLoginDetailsForTokenRequest(request, authorizationProvider.getAuthorization());
        webServiceClient.executeAsynchronousRequest(call, parser);
    }

    public void parseErrorResponse(String body, ErrorResponseHandler errorResponseHandler) {
        try {
            LoginDetailsForTokenErrorResponse response = gson.fromJson(body, LoginDetailsForTokenErrorResponse.class);
            if (response.errors.size() == 0) {
                LoginDetailsForTokenErrorResponse.Error error = gson.fromJson(body, LoginDetailsForTokenErrorResponse.Error.class);
                if (error.code > 0) {
                    errorResponseHandler.parsed(RegistrationFailure.fromCode(error.code));
                } else {
                    errorResponseHandler.noErrorDetails();
                }
            } else {
                int code = response.errors.get(0).code;
                errorResponseHandler.parsed(RegistrationFailure.fromCode(code));
            }
        } catch (JsonParseException e) {
            errorResponseHandler.genericError(e);
        }
    }

    public interface ErrorResponseHandler {
        void genericError(Exception e);
        void noErrorDetails();
        void parsed(RegistrationFailure errorCode);
    }
}
