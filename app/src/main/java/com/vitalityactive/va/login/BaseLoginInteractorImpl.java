package com.vitalityactive.va.login;

import android.support.annotation.NonNull;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.myhealth.vitalityage.VitalityAgePreferencesManager;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.onboarding.LoginRepository;
import com.vitalityactive.va.register.entity.Password;
import com.vitalityactive.va.register.entity.Username;

import javax.inject.Singleton;

@Singleton
public abstract class BaseLoginInteractorImpl implements LoginInteractor, WebServiceResponseParser<LoginServiceResponse> {
    private static final int INCORRECT_USERNAME_OR_PASSWORD = 400;
    private static final int LOCKED_ACCOUNT = 423;
    private final LoginServiceClient loginServiceClient;
    private final EventDispatcher eventDispatcher;
    private final LoginRepository loginRepository;
    private final PartyInformationRepository partyInformationRepository;
    private final DeviceSpecificPreferences deviceSpecificPreferences;
    private LoginRequestResult loginRequestResult = LoginRequestResult.NONE;
    private Username usernameUsedForLastAuthentication;
    private BaseURLSwitcher baseURLSwitcher;
    private VitalityAgePreferencesManager vitalityAgePreferencesManager;


    public BaseLoginInteractorImpl(@NonNull LoginServiceClient loginServiceClient, @NonNull EventDispatcher eventDispatcher, LoginRepository loginRepository, DeviceSpecificPreferences preferences, BaseURLSwitcher baseURLSwitcher, VitalityAgePreferencesManager vitalityAgePreferencesManager,
       PartyInformationRepository partyInformationRepository) {
        this.loginServiceClient = loginServiceClient;
        this.eventDispatcher = eventDispatcher;
        this.loginRepository = loginRepository;
        this.deviceSpecificPreferences = preferences;
        this.baseURLSwitcher = baseURLSwitcher;
        this.vitalityAgePreferencesManager = vitalityAgePreferencesManager;
        this.partyInformationRepository = partyInformationRepository;
    }

    @Override
    public boolean areLoginCredentialsValid(@NonNull Username username, @NonNull Password password) {
        return username.isValid();
    }

    @Override
    public boolean isBusyLoggingIn() {
        return loginServiceClient.isAuthenticating();
    }

    @Override
    public void logIn(@NonNull Username username, @NonNull final Password password) {
        if (!isBusyLoggingIn() && areLoginCredentialsValid(username, password)) {
            username = baseURLSwitcher.switchBaseURL(username);
            usernameUsedForLastAuthentication = username;
            loginServiceClient.authenticate(username, password, this);
        }
    }

    @Override
    public void parseResponse(LoginServiceResponse response) {
        if (!loginRepository.persistLoginResponse(response, usernameUsedForLastAuthentication)) {
            setLoginRequestResult(LoginRequestResult.GENERIC_ERROR);
            eventDispatcher.dispatchEvent(new AuthenticationFailedEvent(loginRequestResult));
        } else {
            setLoginRequestResult(LoginRequestResult.SUCCESSFUL);
            eventDispatcher.dispatchEvent(new AuthenticationSucceededEvent());
            vitalityAgePreferencesManager.onLogin();
        }
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        switch (code) {
            case INCORRECT_USERNAME_OR_PASSWORD:
                setLoginRequestResult(LoginRequestResult.INCORRECT_USERNAME_OR_PASSWORD);
                break;
            case LOCKED_ACCOUNT:
                setLoginRequestResult(LoginRequestResult.LOCKED_ACCOUNT);
                break;
            default:
                setLoginRequestResult(LoginRequestResult.GENERIC_ERROR);
                break;
        }
        eventDispatcher.dispatchEvent(new AuthenticationFailedEvent(loginRequestResult));
    }

    @Override
    public void handleGenericError(Exception exception) {
        setLoginRequestResult(LoginRequestResult.GENERIC_ERROR);
        eventDispatcher.dispatchEvent(new AuthenticationFailedEvent(loginRequestResult));
    }

    @Override
    public void handleConnectionError() {
        setLoginRequestResult(LoginRequestResult.CONNECTION_ERROR);
        eventDispatcher.dispatchEvent(new AuthenticationFailedEvent(LoginRequestResult.CONNECTION_ERROR));
    }

    private synchronized void setLoginRequestResult(LoginRequestResult loginRequestResult) {
        this.loginRequestResult = loginRequestResult;
    }

    @Override
    public synchronized LoginRequestResult getLoginRequestResult() {
        return loginRequestResult;
    }

    @Override
    public void setCurrentUserHasSeenOnboarding() {
        deviceSpecificPreferences.setCurrentUserHasSeenOnboarding();
    }


    @Override
    public String getPartyId() {
        return String.valueOf(partyInformationRepository.getPartyId());
    }

    @Override
    public String getTenantId() {
        return String.valueOf(partyInformationRepository.getTenantId());
    }
}
