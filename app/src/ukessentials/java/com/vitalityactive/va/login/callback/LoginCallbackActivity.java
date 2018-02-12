package com.vitalityactive.va.login.callback;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.vitalityactive.va.BaseUkePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.login.service.RegistrationFailure;
import com.vitalityactive.va.register.ActivationValues;

import javax.inject.Inject;

public class LoginCallbackActivity
        extends BaseUkePresentedActivity<LoginCallbackPresenter.UserInterface, LoginCallbackPresenter>
        implements LoginCallbackPresenter.UserInterface
{
    @Inject
    LoginCallbackPresenter presenter;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.in_content_loading_indicator);
    }

    @Override
    public void showLoadingIndicator() {
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        final View view = findViewById(R.id.loadingPanel);
        view.post(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected LoginCallbackPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected LoginCallbackPresenter getPresenter() {
        return presenter;
    }

    @Override
    public ActivationValues getActivationValues() {
        return getIntent().getParcelableExtra(ActivationValues.class.getSimpleName());
    }

    @Override
    public Uri getLaunchedIntentData() {
        return getIntent().getData();
    }

    @Override
    public void onInvalidToken() {
        ukeNavigationCoordinator.showLoginGetStarted(this, "Invalid token. Please login again");
    }

    @Override
    public void onSuccessfulLogin() {
        navigationCoordinator.navigateAfterSuccessfulLogin(this);
    }

    @Override
    public void onConnectionError() {
        ukeNavigationCoordinator.showLoginGetStarted(this, getString(R.string.connectivity_error_alert_message_45));
    }

    @Override
    public void onGenericError() {
        ukeNavigationCoordinator.showLoginGetStarted(this, getString(R.string.generic_unkown_error_message_267));
    }

    @Override
    public void onNeedMoreInformation() {
        ukeNavigationCoordinator.navigateOnUKEAuthenticationCodeRequired(this, getLaunchedIntentData());
    }

    @Override
    public void onActivationValuesValidationError(RegistrationFailure validationError, ActivationValues activationValues) {
        ukeNavigationCoordinator.navigateOnActivationValuesValidationError(this, getLaunchedIntentData(), validationError, activationValues);
    }
}
