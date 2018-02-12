package com.vitalityactive.va.launch;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.vitalityactive.va.dependencyinjection.BaseUrl;
import com.vitalityactive.va.login.LoginActivity;
import com.vitalityactive.va.login.LoginFragment;
import com.vitalityactive.va.login.basic.BasicLoginActivity;
import com.vitalityactive.va.login.callback.LoginCallbackActivity;
import com.vitalityactive.va.login.service.RegistrationFailure;
import com.vitalityactive.va.register.ActivationValues;
import com.vitalityactive.va.register.view.ActivateActivity;
import com.vitalityactive.va.userpreferences.learnmore.ShareVitalityStatusActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UkeNavigator extends NavigatorImpl {
    @Inject
    public UkeNavigator() {
        super();
    }

    public void showUKEActivation(Activity activity, Uri launchedIntentData) {
        navigateToActivity(activity, ActivateActivity.class, true, launchedIntentData);
    }

    public void showUKEActivation(Activity activity, Uri data, RegistrationFailure validationError, ActivationValues values) {
        navigateToActivity(activity, ActivateActivity.class, true, data, values,
                "validationError", validationError.toString());
    }

    public void showLoginAndClearTasks(Activity activity, String errorMessage, BaseUrl baseUrl) {
        Bundle bundle = new Bundle();
        bundle.putString(LoginFragment.EXTRA_FAILURE, errorMessage);
        if (baseUrl != null) {
            bundle.putString(LoginFragment.URL_SWITCH, baseUrl.toString());
        }
        navigateToActivityInNewTask(activity, LoginActivity.class, bundle);
    }

    public void returnToCallbackActivity(Activity activity, Uri data, ActivationValues values) {
        navigateToActivity(activity, LoginCallbackActivity.class, true, data, values);
    }

    public void navigateToBasicLogin(Activity activity) {
        navigateToActivity(activity, BasicLoginActivity.class, false);
    }

    public void showShareVitalityStatusLearnmore(Activity activity) {
        navigateToActivity(activity, ShareVitalityStatusActivity.class);
    }
}
