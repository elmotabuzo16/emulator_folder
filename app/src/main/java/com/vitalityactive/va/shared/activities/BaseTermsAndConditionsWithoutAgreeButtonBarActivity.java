package com.vitalityactive.va.shared.activities;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsBaseActivity;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsWithoutAgreeButtonBarPresenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsWithoutAgreeButtonBarUserInterface;

import javax.inject.Inject;

public abstract class BaseTermsAndConditionsWithoutAgreeButtonBarActivity
        extends TermsAndConditionsBaseActivity<TermsAndConditionsWithoutAgreeButtonBarUserInterface, TermsAndConditionsWithoutAgreeButtonBarPresenter>
        implements TermsAndConditionsWithoutAgreeButtonBarUserInterface
{
    @Inject
    EventDispatcher eventDispatcher;

    @Override
    public void navigateAfterTermsAndConditionsAccepted() {
        navigationCoordinator.navigateAfterVHCPrivacyPolicyAccepted(this);
    }

    @Override
    public void hideButtonBar() {
        super.hideButtonBar();
    }

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);

        setUpActionBarWithTitle(getActionBarTitle())
                .setDisplayHomeAsUpEnabled(true);
    }

    @Override
    @CallSuper
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        getDependencyInjector().inject(this);
    }

    @StringRes
    protected abstract int getActionBarTitle();

    @Override
    protected EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    @Override
    protected TermsAndConditionsWithoutAgreeButtonBarUserInterface getUserInterface() {
        return this;
    }
}
