package com.vitalityactive.va.login.callback;

import android.support.annotation.NonNull;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.login.AuthenticationFailedEvent;
import com.vitalityactive.va.login.LoginEventListenerWrappers;
import com.vitalityactive.va.login.LoginInteractor;
import com.vitalityactive.va.login.service.RegistrationFailure;
import com.vitalityactive.va.login.service.UKERegisterServiceClient;
import com.vitalityactive.va.login.service.LoginDetailsForTokenResponse;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.register.ActivationValues;
import com.vitalityactive.va.register.entity.Password;
import com.vitalityactive.va.register.entity.Username;

public class UKELoginInteractor implements WebServiceResponseParser<LoginDetailsForTokenResponse>,LoginEventListenerWrappers.Callback {
    private final DeviceSpecificPreferences preferences;
    private final LoginEventListenerWrappers loginEventListenerWrappers;
    private final UKERegisterServiceClient registerServiceClient;
    private final LoginInteractor loginInteractor;
    private final EventDispatcher eventDispatcher;

    public UKELoginInteractor(DeviceSpecificPreferences preferences, UKERegisterServiceClient registerServiceClient, LoginInteractor loginInteractor, EventDispatcher eventDispatcher) {
        this.preferences = preferences;
        this.registerServiceClient = registerServiceClient;
        this.loginInteractor = loginInteractor;
        this.eventDispatcher = eventDispatcher;
        loginEventListenerWrappers = new LoginEventListenerWrappers(eventDispatcher, this);
    }

    public void register(@NonNull String token) {
        registerServiceClient.getLoginDetailsForToken(token, this);
    }

    public void register(@NonNull String token, @NonNull ActivationValues activationValues) {
        registerServiceClient.getLoginDetailsForToken(token, activationValues, this);
    }

    @Override
    public void parseResponse(LoginDetailsForTokenResponse response) {
        onLoginDetailsForTokenLoaded(response);
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        if (code == 500 || code == 404) {
            eventDispatcher.dispatchEvent(UkeLoginEvent.genericError());
            return;
        }

        registerServiceClient.parseErrorResponse(errorBody, new UKERegisterServiceClient.ErrorResponseHandler() {
            @Override
            public void genericError(Exception e) {
                eventDispatcher.dispatchEvent(UkeLoginEvent.genericError());
            }

            @Override
            public void noErrorDetails() {
                eventDispatcher.dispatchEvent(UkeLoginEvent.genericError());
            }

            @Override
            public void parsed(RegistrationFailure error) {
                switch (error) {
                    case NEED_MORE_INFO:
                        eventDispatcher.dispatchEvent(UkeLoginEvent.needMoreInformation());
                        break;
                    case INVALID_TOKEN:
                        eventDispatcher.dispatchEvent(UkeLoginEvent.invalidToken());
                        break;
                    case INVALID_DATE_OF_BIRTH:
                    case INVALID_ENTITY_NUMBER:
                    case INVALID_REGISTRATION_CODE:
                        eventDispatcher.dispatchEvent(UkeLoginEvent.invalidActivationDetails(error));
                        break;
                    default:
                        eventDispatcher.dispatchEvent(UkeLoginEvent.genericError());
                }
            }
        });
    }

    @Override
    public void handleGenericError(Exception exception) {
        eventDispatcher.dispatchEvent(UkeLoginEvent.genericError());
    }

    @Override
    public void handleConnectionError() {
        eventDispatcher.dispatchEvent(UkeLoginEvent.connectionError());
    }

    private void onLoginDetailsForTokenLoaded(LoginDetailsForTokenResponse response) {
        loginEventListenerWrappers.addListeners();
        loginInteractor.logIn(new Username(preferences.getCurrentEnvironmentPrefix() + response.username), new Password(response.password));
    }

    @Override
    public void onAuthenticationSucceeded() {
        eventDispatcher.dispatchEvent(UkeLoginEvent.success());
    }

    @Override
    public void onAuthenticationFailed(AuthenticationFailedEvent event) {
        if (event.getLoginRequestResult() == LoginInteractor.LoginRequestResult.CONNECTION_ERROR) {
            eventDispatcher.dispatchEvent(UkeLoginEvent.connectionError());
        } else {
            eventDispatcher.dispatchEvent(UkeLoginEvent.genericError());
        }
    }
}
