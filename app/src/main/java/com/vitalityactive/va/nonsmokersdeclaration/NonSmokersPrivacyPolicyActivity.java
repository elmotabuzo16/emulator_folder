package com.vitalityactive.va.nonsmokersdeclaration;

import android.os.Bundle;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsBaseActivity;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;

import javax.inject.Inject;
import javax.inject.Named;

public class NonSmokersPrivacyPolicyActivity extends TermsAndConditionsBaseActivity implements TermsAndConditionsPresenter.UserInterface {

    @Inject
    EventDispatcher eventDispatcher;

    @Inject
    @Named(DependencyNames.NON_SMOKERS_PRIVACY_POLICY)
    TermsAndConditionsPresenter termsAndConditionsPresenter;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setUpActionBarWithTitle(R.string.data_and_privacy_policy_title_123)
                .setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected NonSmokersPrivacyPolicyActivity getUserInterface() {
        return this;
    }

    @Override
    protected EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    @Override
    protected TermsAndConditionsPresenter getPresenter() {
        return termsAndConditionsPresenter;
    }

    @Override
    public void navigateAfterTermsAndConditionsAccepted() {
        navigationCoordinator.navigateAfterNonSmokersPrivacyPolicyAccepted(this);
    }

}
