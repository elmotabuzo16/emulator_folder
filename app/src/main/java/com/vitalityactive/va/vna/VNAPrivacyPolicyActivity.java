package com.vitalityactive.va.vna;

import android.os.Bundle;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsBaseActivity;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;

import javax.inject.Inject;
import javax.inject.Named;

public class VNAPrivacyPolicyActivity
        extends TermsAndConditionsBaseActivity<VNAPrivacyPolicyUserInterface, TermsAndConditionsPresenter<VNAPrivacyPolicyUserInterface>>
        implements VNAPrivacyPolicyUserInterface {

    private static final String CONNECTION_SUBMISSION_REQUEST_ERROR_MESSAGE = "CONNECTION_SUBMISSION_REQUEST_ERROR_MESSAGE";
    private static final String GENERIC_SUBMISSION_REQUEST_ERROR_MESSAGE = "GENERIC_SUBMISSION_REQUEST_ERROR_MESSAGE";

    @Inject
    EventDispatcher eventDispatcher;

    @Inject
    @Named(DependencyNames.VNA_PRIVACY_POLICY)
    VNAPrivacyPolicyPresenter vnaPrivacyPolicyPresenter;
    private long questionnaireTypeKey;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setUpActionBarWithTitle(R.string.data_and_privacy_policy_title_123)
                .setDisplayHomeAsUpEnabled(true);

        questionnaireTypeKey = Long.parseLong(getIntent().getStringExtra("questionnaireTypeKey"));
        vnaPrivacyPolicyPresenter.setQuestionnaireTypeKey(questionnaireTypeKey);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        navigationCoordinator.getVNACaptureDependencyInjector().inject(this);
    }

    @Override
    protected VNAPrivacyPolicyUserInterface getUserInterface() {
        return this;
    }

    @Override
    protected EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    @Override
    protected TermsAndConditionsPresenter<VNAPrivacyPolicyUserInterface> getPresenter() {
        return vnaPrivacyPolicyPresenter;
    }

    @Override
    public void navigateAfterTermsAndConditionsAccepted() {
        navigationCoordinator.navigateAfterTermsAndConditionsAccepted(this, questionnaireTypeKey);
    }

    @Override
    public void showGenericSubmissionRequestErrorMessage() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                GENERIC_SUBMISSION_REQUEST_ERROR_MESSAGE,
                getString(R.string.privacy_policy_unable_to_complete_alert_title_115),
                getString(R.string.vhr_unable_to_submit_error_alert_message),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
        alert.show(getSupportFragmentManager(), GENERIC_SUBMISSION_REQUEST_ERROR_MESSAGE);
    }

    @Override
    public void showConnectionSubmissionRequestErrorMessage() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                CONNECTION_SUBMISSION_REQUEST_ERROR_MESSAGE,
                getString(R.string.connectivity_error_alert_title_44),
                getString(R.string.connectivity_error_alert_message_45),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
        alert.show(getSupportFragmentManager(), CONNECTION_SUBMISSION_REQUEST_ERROR_MESSAGE);
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        super.onEvent(event);
        if (wasTryAgainButtonTapped(event)) {
            vnaPrivacyPolicyPresenter.onUserTriesToSubmitAgain();
        }
    }

    private boolean wasTryAgainButtonTapped(AlertDialogFragment.DismissedEvent event) {
        return wasTryAgainButtonTapped(event, GENERIC_SUBMISSION_REQUEST_ERROR_MESSAGE) ||
                wasTryAgainButtonTapped(event, CONNECTION_SUBMISSION_REQUEST_ERROR_MESSAGE);
    }
}
