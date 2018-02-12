package com.vitalityactive.va.forgotpassword;

import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.forgotpassword.service.ForgotPasswordServiceClient;
import com.vitalityactive.va.forgotpassword.service.ForgotPasswordSuccessEvent;
import com.vitalityactive.va.login.BaseURLSwitcher;
import com.vitalityactive.va.networking.RequestFailedEvent;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.register.entity.Username;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ForgotPasswordInteractor implements WebServiceResponseParser<String> {
    private final ForgotPasswordServiceClient serviceClient;
    private final EventDispatcher eventDispatcher;
    private BaseURLSwitcher baseURLSwitcher;

    @Inject
    public ForgotPasswordInteractor(ForgotPasswordServiceClient serviceClient, EventDispatcher eventDispatcher, BaseURLSwitcher baseURLSwitcher) {
        this.serviceClient = serviceClient;
        this.eventDispatcher = eventDispatcher;
        this.baseURLSwitcher = baseURLSwitcher;
    }

    public void forgotPassword(String username) {
        if (!isRequestInProgress()) {
            Username newUsername = baseURLSwitcher.switchBaseURL(new Username(username));
            serviceClient.forgotPassword(newUsername.getText().toString(), this);
        }
    }

    private boolean isRequestInProgress() {
        return serviceClient.isRequestInProgress();
    }

    @Override
    public void parseResponse(String body) {
        eventDispatcher.dispatchEvent(new ForgotPasswordSuccessEvent());
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        if (code == 400) {
            eventDispatcher.dispatchEvent(RequestFailedEvent.invalidUsername());
        } else {
            eventDispatcher.dispatchEvent(RequestFailedEvent.genericError());
        }
    }

    @Override
    public void handleGenericError(Exception exception) {
        eventDispatcher.dispatchEvent(RequestFailedEvent.genericError());
    }

    @Override
    public void handleConnectionError() {
        eventDispatcher.dispatchEvent(RequestFailedEvent.connectionError());
    }
}
