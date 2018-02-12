package com.vitalityactive.va.activerewards.termsandconditions;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsBaseActivity;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;

import javax.inject.Inject;

public class ActiveRewardsMedicallyFitAgreementActivity
        extends TermsAndConditionsBaseActivity<ActiveRewardsTermsAndConditionsUserInterface, TermsAndConditionsPresenter<ActiveRewardsTermsAndConditionsUserInterface>>
        implements ActiveRewardsTermsAndConditionsUserInterface, EventListener<AlertDialogFragment.DismissedEvent> {
    private static final String ACTIVE_REWARDS_TERMS_AND_CONDITIONS_ACTIVATION_FAILED_ALERT = "ACTIVE_REWARDS_TERMS_AND_CONDITIONS_ACTIVATION_FAILED_ALERT";

    @Inject
    TermsAndConditionsPresenter<ActiveRewardsTermsAndConditionsUserInterface> activeRewardsTermsAndConditionsPresenter;

    @Inject
    EventDispatcher eventDispatcher;

    private boolean isPaused;
    private String activationErrorAlertTitle;

    private String activationErrorAlertMessage;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setUpActionBarWithTitle(R.string.active_rewards_terms_and_conditions_screen_title)
                .setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            activationErrorAlertTitle = savedInstanceState.getString("ACTIVATION_ERROR_ALERT_TITLE");
            activationErrorAlertMessage = savedInstanceState.getString("ACTIVATION_ERROR_ALERT_MESSAGE");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (activationErrorAlertTitle != null) {
            outState.putString("ACTIVATION_ERROR_ALERT_TITLE", activationErrorAlertTitle);
        }
        if (activationErrorAlertMessage != null) {
            outState.putString("ACTIVATION_ERROR_ALERT_MESSAGE", activationErrorAlertMessage);
        }
    }

    @Override
    public void onBackPressed() {
        getPresenter().onBackPressed();
    }

    @Override
    protected void resume() {
        super.resume();
        isPaused = false;
    }

    @Override
    protected void resumeFragments() {
        super.resumeFragments();
        showActivationErrorAlertIfWeWereToldToAfterAppWasBackgrounded();
    }

    private void showActivationErrorAlertIfWeWereToldToAfterAppWasBackgrounded() {
        if (activationErrorAlertTitle != null) {
            showActivationErrorAlert(activationErrorAlertTitle, activationErrorAlertMessage);
            activationErrorAlertTitle = null;
            activationErrorAlertMessage = null;
        }
    }

    @Override
    protected void pause() {
        super.pause();
        isPaused = true;
    }

    @Override
    protected EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected ActiveRewardsTermsAndConditionsUserInterface getUserInterface() {
        return this;
    }

    @Override
    protected TermsAndConditionsPresenter<ActiveRewardsTermsAndConditionsUserInterface> getPresenter() {
        return activeRewardsTermsAndConditionsPresenter;
    }

    @Override
    public void navigateAfterTermsAndConditionsAccepted() {
        navigationCoordinator.navigateAfterActiveRewardsTermsAndConditionsAccepted(this);
    }

    @Override
    public void navigateAfterSkippingTermsAndConditions() {
        navigationCoordinator.navigateAfterActiveRewardsActivationAcknowledged(this);
    }

    @Override
    public void showGenericActivationErrorMessage() {
        showActivationErrorAlertIfNotPaused(getString(R.string.active_rewards_terms_and_conditions_activation_failed_alert_title),
                getString(R.string.error_unable_to_load_message_504));
    }

    @Override
    public void showConnectionActivationErrorMessage() {
        showActivationErrorAlertIfNotPaused(getString(R.string.connectivity_error_alert_title_44),
                getString(R.string.connectivity_error_alert_message_45));
    }

    private void showActivationErrorAlertIfNotPaused(@NonNull String title, @NonNull String message) {
        if (isPaused) {
            activationErrorAlertTitle = title;
            activationErrorAlertMessage = message;
        } else {
            showActivationErrorAlert(title, message);
        }
    }

    private void showActivationErrorAlert(@NonNull String title, @NonNull String message) {
        AlertDialogFragment alert = AlertDialogFragment.create(ACTIVE_REWARDS_TERMS_AND_CONDITIONS_ACTIVATION_FAILED_ALERT, title, message, getString(R.string.try_again_button_title_43), null, getString(R.string.ok_button_title_40));
        alert.show(getSupportFragmentManager(), ACTIVE_REWARDS_TERMS_AND_CONDITIONS_ACTIVATION_FAILED_ALERT);
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        super.onEvent(event);
        if (eventIsOfType(event, ACTIVE_REWARDS_TERMS_AND_CONDITIONS_ACTIVATION_FAILED_ALERT) && event.getClickedButtonType() == AlertDialogFragment.DismissedEvent.ClickedButtonType.Negative) {
            presenter.onUserAgreesToTermsAndConditions();
        }
    }

    @Override
    public void navigateAfterTermsAndConditionsDeclined() {
        navigationCoordinator.navigateAfterActiveRewardsTermsAndConditionsDeclined(this);
    }
}
