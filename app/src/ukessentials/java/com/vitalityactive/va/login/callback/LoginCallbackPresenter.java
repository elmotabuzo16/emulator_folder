package com.vitalityactive.va.login.callback;

import android.net.Uri;
import android.util.Log;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.VitalityActiveApplication;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.login.service.RegistrationFailure;
import com.vitalityactive.va.register.ActivationValues;

public class LoginCallbackPresenter extends BasePresenter<LoginCallbackPresenter.UserInterface> {
    private static final String TAG = "LoginCallback";
    private static final String TOKEN_QUERY_STRING_NAME = "id_token";
    private static final String ERROR_QUERY_STRING_NAME = "error";

    private final UKELoginInteractor interactor;
    private final EventDispatcher eventDispatcher;
    private EventListener<UkeLoginEvent> loginEventEventListener = new EventListener<UkeLoginEvent>() {
        @Override
        public void onEvent(UkeLoginEvent event) {
            userInterface.hideLoadingIndicator();
            switch (event.getRequestResult()) {
                case SUCCESS:
                    userInterface.onSuccessfulLogin();
                    break;
                case CONNECTION_ERROR:
                    userInterface.onConnectionError();
                    break;
                case GENERIC_ERROR:
                    userInterface.onGenericError();
                    break;
                case INVALID_TOKEN:
                    userInterface.onInvalidToken();
                    break;
                case NEED_MORE_INFORMATION:
                    userInterface.onNeedMoreInformation();
                    break;
                case INVALID_DETAILS:
                    userInterface.onActivationValuesValidationError(event.validationError, activationValues);
                    break;
            }
        }
    };
    private String token;
    private String callbackError;
    private ActivationValues activationValues;

    public LoginCallbackPresenter(UKELoginInteractor interactor, EventDispatcher eventDispatcher) {
        this.interactor = interactor;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        super.onUserInterfaceCreated(isNewNavigation);
        decodeLaunchIntentUri();
        activationValues = userInterface.getActivationValues();
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();
        eventDispatcher.addEventListener(UkeLoginEvent.class, loginEventEventListener);

        if (!isNullOrEmpty(callbackError) && isNullOrEmpty(token)) {
            userInterface.onGenericError();
        } else if (isNullOrEmpty(token)) {
            userInterface.onInvalidToken();
        } else if (activationValues != null) {
            loginWithTokenAndActivationValues();
        } else {
            loginWithToken();
        }
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);
        eventDispatcher.removeEventListener(UkeLoginEvent.class, loginEventEventListener);
    }

    private void decodeLaunchIntentUri() {
        Uri uri = userInterface.getLaunchedIntentData();
        if (uri == null) {
            Log.w(TAG, "launched with no launched intent");
            return;
        }

        token = uri.getQueryParameter(TOKEN_QUERY_STRING_NAME);
        callbackError = uri.getQueryParameter(ERROR_QUERY_STRING_NAME);

        if (VitalityActiveApplication.isDebugBuild()) {
            Log.d(TAG, "Launched with token " + token + ", full callback url: " + uri);
            if (callbackError != null && !callbackError.isEmpty()) {
                Log.w(TAG, "got error: " + callbackError);
            }
        }
    }

    private void loginWithToken() {
        userInterface.showLoadingIndicator();
        interactor.register(token);
    }

    private void loginWithTokenAndActivationValues() {
        userInterface.showLoadingIndicator();
        interactor.register(token, activationValues);
    }

    public interface UserInterface {
        void showLoadingIndicator();
        void hideLoadingIndicator();
        ActivationValues getActivationValues();
        Uri getLaunchedIntentData();
        void onInvalidToken();
        void onSuccessfulLogin();
        void onConnectionError();
        void onGenericError();
        void onNeedMoreInformation();
        void onActivationValuesValidationError(RegistrationFailure validationError, ActivationValues activationValues);
    }
}
