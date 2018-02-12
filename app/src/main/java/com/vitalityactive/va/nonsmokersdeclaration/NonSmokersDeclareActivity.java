package com.vitalityactive.va.nonsmokersdeclaration;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsBaseActivity;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;

import javax.inject.Inject;
import javax.inject.Named;

import static com.vitalityactive.va.dependencyinjection.DependencyNames.NON_SMOKERS_DECLARATION;

public class NonSmokersDeclareActivity extends TermsAndConditionsBaseActivity implements TermsAndConditionsPresenter.UserInterface {

    @Inject
    EventDispatcher eventDispatcher;

    @Inject
    @Named(NON_SMOKERS_DECLARATION)
    TermsAndConditionsPresenter termsAndConditionsPresenter;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setUpActionBarWithTitle(R.string.home_card_card_title_96)
                .setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected TermsAndConditionsPresenter.UserInterface getUserInterface() {
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
        navigationCoordinator.navigateAfterDeclaringNonSmokerStatus(this);
    }

    @NonNull
    @Override
    protected String getAgreeActionTitle() {
        return getString(R.string.declare_button_title_114);
    }
}
