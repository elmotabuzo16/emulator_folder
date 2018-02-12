package com.vitalityactive.va;

import android.app.Activity;
import android.net.Uri;

import com.vitalityactive.va.dependencyinjection.BaseUrl;
import com.vitalityactive.va.launch.UkeNavigator;
import com.vitalityactive.va.login.service.RegistrationFailure;
import com.vitalityactive.va.register.ActivationValues;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UkeNavigationCoordinator {
    @Inject
    protected UkeNavigator navigator;

    @Inject
    public UkeNavigationCoordinator() {
    }

    public void navigateOnUKEAuthenticationCodeRequired(Activity activity, Uri launchedIntentData) {
        navigator.showUKEActivation(activity, launchedIntentData);
    }

    public void navigateOnActivationValuesValidationError(Activity activity, Uri data, RegistrationFailure validationError, ActivationValues values) {
        navigator.showUKEActivation(activity, data, validationError, values);
    }

    public void showLoginGetStarted(Activity activity, String errorMessage, BaseUrl baseUrl) {
        navigator.showLoginAndClearTasks(activity, errorMessage, baseUrl);
    }

    public void showLoginGetStarted(Activity activity, String errorMessage) {
        navigator.showLoginAndClearTasks(activity, errorMessage, null);
    }

    public void navigateAfterActivationValuesCaptured(Activity activity, Uri data, ActivationValues values) {
        navigator.returnToCallbackActivity(activity, data, values);
    }

    public void navigateToBasicLogin(Activity activity) {
        navigator.navigateToBasicLogin(activity);
    }

    public void navigateToShareVitalityLearnMore(Activity activity) {
        navigator.showShareVitalityStatusLearnmore(activity);
    }
}
