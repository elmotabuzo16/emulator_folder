package com.vitalityactive.va.wellnessdevices.linking.privacypolicy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dependencyinjection.DependencyNames;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsBaseActivity;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;
import com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesDetailsActivity;
import com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesLinkingInteractor;

import javax.inject.Inject;
import javax.inject.Named;

import static com.vitalityactive.va.wellnessdevices.linking.web.WellnessDevicesWebActivity.KEY_WEB_EXTRA;

public class WellnessDevicesPrivacyPolicyActivity
        extends TermsAndConditionsBaseActivity<PrivacyPolicyPresenter.UserInterface, PrivacyPolicyPresenter>
        implements PrivacyPolicyPresenter.UserInterface {
    private static final String WD_TRY_AGAIN_ERROR_ALERT = "LINK_TRY_AGAIN_ERROR_ALERT";
    private Bundle inputBundle;

    @Inject
    EventDispatcher eventDispatcher;

    @Inject
    @Named(DependencyNames.WD_PRIVACY_POLICY)
    PrivacyPolicyPresenter privacyPolicyPresenter;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setUpActionBarWithTitle(R.string.data_and_privacy_policy_title_123)
                .setDisplayHomeAsUpEnabled(true);

        inputBundle = getIntent().getBundleExtra(KEY_WEB_EXTRA);
        presenter.setPartner((PartnerDto) inputBundle.getParcelable(WellnessDevicesDetailsActivity.KEY_DEVICE));
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        navigationCoordinator.getWDALinkingDependencyInjector().inject(this);
    }

    @Override
    protected WellnessDevicesPrivacyPolicyActivity getUserInterface() {
        return this;
    }

    @Override
    protected EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    @Override
    protected PrivacyPolicyPresenter getPresenter() {
        return privacyPolicyPresenter;
    }

    @Override
    public void navigateAfterTermsAndConditionsAccepted() {
        // NOP
    }

    @Override
    public void redirectToPartnerWebSite(String redirectUrl) {
        navigationCoordinator.navigateToWellnessDevicesWebLinkFlowFromPrivacyPolicy(this, redirectUrl,
                WellnessDevicesLinkingInteractor.WD_LINK_DEVICE);
    }

    @Override
    public void navigateToPreviousScreenWithSuccessStatus(){
        Intent intent = NavUtils.getParentActivityIntent(this);
        intent.putExtra(KEY_WEB_EXTRA, inputBundle);
        setResult(Activity.RESULT_OK, intent);
        NavUtils.navigateUpTo(this, intent);
    }

    @Override
    public void showGenericLinkErrorMessage() {
        AlertDialogFragment.create(WD_TRY_AGAIN_ERROR_ALERT,
                getString(R.string.WDA_device_detail_sync_failed_dialog_title_512),
                getString(R.string.WDA_device_detail_sync_failed_dialog_message_513),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43))
                .show(getSupportFragmentManager(), WD_TRY_AGAIN_ERROR_ALERT);
    }

    @Override
    public void showConnectionLinkErrorMessage() {
        AlertDialogFragment.create(WD_TRY_AGAIN_ERROR_ALERT,
                getString(R.string.connectivity_error_alert_title_44),
                getString(R.string.connectivity_error_alert_message_45),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43))
                .show(getSupportFragmentManager(), WD_TRY_AGAIN_ERROR_ALERT);
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        super.onEvent(event);
        if(event.getType().equals(WD_TRY_AGAIN_ERROR_ALERT) &&
                event.getClickedButtonType() == AlertDialogFragment.DismissedEvent.ClickedButtonType.Positive){
            presenter.linkDevice();
        }
    }

    @Override
    @SuppressWarnings("ResourceType")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setResult(requestCode, data);
        finish();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
