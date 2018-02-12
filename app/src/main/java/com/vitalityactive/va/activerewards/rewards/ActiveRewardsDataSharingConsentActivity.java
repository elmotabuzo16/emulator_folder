package com.vitalityactive.va.activerewards.rewards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.rewards.presenters.ActiveRewardsDataPrivacyPresenter;
import com.vitalityactive.va.constants.ProcessInstructionType;
import com.vitalityactive.va.constants.RewardId;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsBaseActivity;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;

import javax.inject.Inject;

public class ActiveRewardsDataSharingConsentActivity
        extends TermsAndConditionsBaseActivity<ActiveRewardsDataPrivacyUserInterface, TermsAndConditionsPresenter<ActiveRewardsDataPrivacyUserInterface>>
        implements ActiveRewardsDataPrivacyUserInterface, EventListener<AlertDialogFragment.DismissedEvent> {

    public static final String EXTRA_REWARD_ID = "EXTRA_REWARD_ID";
    public static final String EXTRA_REWARD_UNIQUE_ID = "EXTRA_REWARD_UNIQUE_ID";
    public static final String EXTRA_BUNDLE_KEY = "EXTRA_BUNDLE_KEY";
    private static final String CONNECTION_ERROR_MESSAGE = "CONNECTION_ERROR_MESSAGE";
    private static final String GENERIC_ERROR_MESSAGE = "GENERIC_ERROR_MESSAGE";

    @Inject
    ActiveRewardsDataPrivacyPresenter activeRewardsDataPrivacyPresenter;

    @Inject
    EventDispatcher eventDispatcher;

    private String activationErrorAlertTitle;
    private String activationErrorAlertMessage;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setUpActionBarWithTitle(R.string.AR_parnters_starbucks_data_privacy_title_1065)
                .setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            activationErrorAlertTitle = savedInstanceState.getString("ACTIVATION_ERROR_ALERT_TITLE");
            activationErrorAlertMessage = savedInstanceState.getString("ACTIVATION_ERROR_ALERT_MESSAGE");
        }

        Bundle bundle = getIntent().getBundleExtra(EXTRA_BUNDLE_KEY);
        activeRewardsDataPrivacyPresenter.setRewardUniqueId(bundle.getLong(EXTRA_REWARD_UNIQUE_ID, 0));
        activeRewardsDataPrivacyPresenter.setRewardId(bundle.getInt(EXTRA_REWARD_ID, 0));
        activeRewardsDataPrivacyPresenter.setEventSource(getEventSource(bundle.getInt(EXTRA_REWARD_ID, 0)));

        setupButtonBar(R.id.agree_button_bar, R.id.terms_and_conditions_agree_button, 0)
                .setForwardButtonText(R.string.AR_partners_starbucks_data_privacy_accept_button_1066);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getEventSource(int rewardId) {
        switch (rewardId) {
            case RewardId._AMAZONVOUCHER:
                return String.valueOf(ProcessInstructionType._ARAMAZONDS);
            case RewardId._ZAPPOSVOUCHER:
                return String.valueOf(ProcessInstructionType._ARZAPPOSDS);
        }
        return "-1";
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        if (positiveButtonTapped(event)) {
            presenter.onUserAgreesToTermsAndConditions();
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
    protected EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        navigationCoordinator.getActiveRewardsDependencyInjector().inject(this);
    }

    @Override
    protected ActiveRewardsDataPrivacyUserInterface getUserInterface() {
        return this;
    }

    @Override
    protected TermsAndConditionsPresenter<ActiveRewardsDataPrivacyUserInterface> getPresenter() {
        return activeRewardsDataPrivacyPresenter;
    }

    @Override
    public void navigateAfterRewardChoiceConfirmed(long uniqueID) {
        navigationCoordinator.navigateAfterRewardChoiceConfirmed(this, uniqueID);
    }

    @Override
    public void showConnectionErrorMessage() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                CONNECTION_ERROR_MESSAGE,
                getString(R.string.connectivity_error_alert_title_44),
                getString(R.string.connectivity_error_alert_message_45),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
        alert.show(getSupportFragmentManager(), CONNECTION_ERROR_MESSAGE);
    }

    @Override
    public void showGenericErrorMessage() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                GENERIC_ERROR_MESSAGE,
                getString(R.string.alert_unknown_title_266),
                getString(R.string.alert_unknown_message_267),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
        alert.show(getSupportFragmentManager(), GENERIC_ERROR_MESSAGE);
    }

    @Override
    public void navigateAfterTermsAndConditionsAccepted() {
        // No-op
    }

    @Override
    public void navigateAfterTermsAndConditionsDeclined() {
        navigationCoordinator.navigateAfterActiveRewardsTermsAndConditionsDeclined(this);
    }
}
