package com.vitalityactive.va.activerewards.rewards;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.rewards.presenters.StarbucksDataPrivacyPresenter;
import com.vitalityactive.va.constants.ProcessInstructionType;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsBaseActivity;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;

import javax.inject.Inject;

import static com.vitalityactive.va.activerewards.rewards.StarbucksPartnerRegistrationActivity.OUTGOING_BUNDLE_KEY;
import static com.vitalityactive.va.activerewards.rewards.StarbucksPartnerRegistrationActivity.PARTNER_REGISTRATION_EMAIL;
import static com.vitalityactive.va.activerewards.rewards.StarbucksPartnerRegistrationActivity.REWARD_UNIQUE_ID;

public class StarbucksDataSharingConsentActivity
        extends TermsAndConditionsBaseActivity<StarbucksDataPrivacyUserInterface, TermsAndConditionsPresenter<StarbucksDataPrivacyUserInterface>>
        implements StarbucksDataPrivacyUserInterface, EventListener<AlertDialogFragment.DismissedEvent> {

    private static final String CONNECTION_STARBUCKS_EMAIL_SELECT_VOUCHER_ERROR_MESSAGE = "CONNECTION_STARBUCKS_EMAIL_SELECT_VOUCHER_ERROR_MESSAGE";
    private static final String GENERIC_STARBUCKS_EMAIL_SELECT_VOUCHER_ERROR_MESSAGE = "GENERIC_STARBUCKS_EMAIL_SELECT_VOUCHER_ERROR_MESSAGE";

    @Inject
    StarbucksDataPrivacyPresenter starbucksDataPrivacyPresenter;

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

        Bundle bundle = getIntent().getBundleExtra(OUTGOING_BUNDLE_KEY);
        starbucksDataPrivacyPresenter.setRewardUniqueId(bundle.getLong(REWARD_UNIQUE_ID, 0));
        starbucksDataPrivacyPresenter.setUserEmailAddress(bundle.getString(PARTNER_REGISTRATION_EMAIL));
        starbucksDataPrivacyPresenter.setEventSource(String.valueOf(ProcessInstructionType._ARSTARBUCKSDS));

        setupButtonBar(R.id.agree_button_bar, R.id.terms_and_conditions_agree_button, 0)
                .setForwardButtonText(R.string.AR_partners_starbucks_data_privacy_accept_button_1066);
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
    protected StarbucksDataPrivacyUserInterface getUserInterface() {
        return this;
    }

    @Override
    protected TermsAndConditionsPresenter<StarbucksDataPrivacyUserInterface> getPresenter() {
        return starbucksDataPrivacyPresenter;
    }

    @Override
    public void navigateAfterStarbucksRewardConfirmed(long uniqueID) {
        navigationCoordinator.navigateAfterStarbucksRewardConfirmed(this, uniqueID);
    }

    @Override
    public void showConnectionErrorMessage() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                CONNECTION_STARBUCKS_EMAIL_SELECT_VOUCHER_ERROR_MESSAGE,
                getString(R.string.connectivity_error_alert_title_44),
                getString(R.string.connectivity_error_alert_message_45),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
        alert.show(getSupportFragmentManager(), CONNECTION_STARBUCKS_EMAIL_SELECT_VOUCHER_ERROR_MESSAGE);
    }

    @Override
    public void showGenericErrorMessage() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                GENERIC_STARBUCKS_EMAIL_SELECT_VOUCHER_ERROR_MESSAGE,
                getString(R.string.alert_unknown_title_266),
                getString(R.string.alert_unknown_message_267),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
        alert.show(getSupportFragmentManager(), GENERIC_STARBUCKS_EMAIL_SELECT_VOUCHER_ERROR_MESSAGE);
    }

    @Override
    public void navigateAfterTermsAndConditionsAccepted() {
        //NO-OP
    }

    @Override
    public void navigateAfterTermsAndConditionsDeclined() {
        navigationCoordinator.navigateAfterActiveRewardsTermsAndConditionsDeclined(this);
    }
}
