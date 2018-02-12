package com.vitalityactive.va.termsandconditions;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;

import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;

import javax.inject.Inject;

public class TermsAndConditionsActivity extends TermsAndConditionsBaseActivity implements TermsAndConditionsPresenter.UserInterface {

    @Inject
    EventDispatcher eventDispatcher;

    @Inject
    TermsAndConditionsPresenter termsAndConditionsPresenter;

    @Inject
    AppConfigRepository appConfigRepository;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setUpActionBarWithTitle(R.string.terms_and_conditions_screen_title_94);
        setActionBarColor(getGlobalTintColor());
        setStatusBarColor(getGlobalTintColor());
        setTryAgainButtonColor();
    }

    private void setTryAgainButtonColor() {
        getEmptyStateViewHolder().setButtonColor(getGlobalTintColor());
    }

    private @ColorInt int getGlobalTintColor() {
        return Color.parseColor(appConfigRepository.getGlobalTintColorHex());
    }

    @Override
    public void onBackPressed() {
        getPresenter().onBackPressed();
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
    protected TermsAndConditionsPresenter getPresenter() {
        return termsAndConditionsPresenter;
    }

    @Override
    public void navigateAfterTermsAndConditionsAccepted() {
        navigationCoordinator.navigateAfterTermsAndConditionsAccepted(this);
    }

    protected EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    @Override
    public void showLoadingIndicator() {
        showLoadingIndicator(getGlobalTintColor());
    }

    @Override
    public void showDisagreeAlert() {
        getDisagreeAlertDialogFragment()
                .setCustomPrimaryColor(appConfigRepository.getGlobalTintColorHex())
                .show(getSupportFragmentManager(), TERMS_AND_CONDITIONS_DISAGREE_ALERT);
    }

    @Override
    public void showGenericAgreeRequestErrorMessage() {
        getGenericRequestAlertDialogFragment(TERMS_AND_CONDITIONS_AGREE_REQUEST_ERROR_ALERT)
                .setCustomPrimaryColor(appConfigRepository.getGlobalTintColorHex())
                .show(getSupportFragmentManager(), TERMS_AND_CONDITIONS_AGREE_REQUEST_ERROR_ALERT);
    }

    @Override
    public void showConnectionAgreeRequestErrorMessage() {
        getConnectionAlertDialogFragment(TERMS_AND_CONDITIONS_AGREE_REQUEST_ERROR_ALERT)
                .setCustomPrimaryColor(appConfigRepository.getGlobalTintColorHex())
                .show(getSupportFragmentManager(), TERMS_AND_CONDITIONS_AGREE_REQUEST_ERROR_ALERT);
    }

    @Override
    public void showGenericDisagreeRequestErrorMessage() {
        getGenericRequestAlertDialogFragment(TERMS_AND_CONDITIONS_DISAGREE_REQUEST_ERROR_ALERT)
                .setCustomPrimaryColor(appConfigRepository.getGlobalTintColorHex())
                .show(getSupportFragmentManager(), TERMS_AND_CONDITIONS_DISAGREE_REQUEST_ERROR_ALERT);
    }

    @Override
    public void showConnectionDisagreeRequestErrorMessage() {
        getConnectionAlertDialogFragment(TERMS_AND_CONDITIONS_DISAGREE_REQUEST_ERROR_ALERT)
                .setCustomPrimaryColor(appConfigRepository.getGlobalTintColorHex())
                .show(getSupportFragmentManager(), TERMS_AND_CONDITIONS_DISAGREE_REQUEST_ERROR_ALERT);
    }
}
