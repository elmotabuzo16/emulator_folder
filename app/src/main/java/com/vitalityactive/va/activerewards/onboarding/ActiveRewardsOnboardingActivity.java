package com.vitalityactive.va.activerewards.onboarding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.uicomponents.LoadingIndicatorFragment;

import javax.inject.Inject;

public class ActiveRewardsOnboardingActivity extends BasePresentedActivity<ActiveRewardsOnboardingPresenter.UserInterface, ActiveRewardsOnboardingPresenter> implements ActiveRewardsOnboardingPresenter.UserInterface, EventListener<AlertDialogFragment.DismissedEvent> {
    public static final String EXTRA_CONTENT_TYPE = "CONTENT_TYPE";
    public static final String CONTENT_TYPE_PROBABILISTIC = "PROBABILISTIC";
    public static final String CONTENT_TYPE_DEFINED = "DEFINED";
    private static final String LOADING_INDICATOR = "LOADING_INDICATOR";
    private static final String ACTIVATION_ERROR_ALERT = "ACTIVATION_ERROR_ALERT";

    @Inject
    ActiveRewardsOnboardingPresenter activeRewardsOnboardingPresenter;

    @Inject
    EventDispatcher eventDispatcher;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_active_rewards_onboarding);
        loadContent();
    }

    @Override
    protected void resume() {
        eventDispatcher.addEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    @Override
    protected void pause() {
        eventDispatcher.removeEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected ActiveRewardsOnboardingPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected ActiveRewardsOnboardingPresenter getPresenter() {
        return activeRewardsOnboardingPresenter;
    }

    private void loadContent() {
        String contentType = getIntent().getStringExtra(EXTRA_CONTENT_TYPE);
        if (CONTENT_TYPE_PROBABILISTIC.equals(contentType)) {
            configureProbabilisticContent();
        } else if (CONTENT_TYPE_DEFINED.equals(contentType)) {
            configureDefinedContent();
        } else {
            hideRewardsSection();
        }
    }

    private void configureProbabilisticContent() {
        setIconImage((ImageView) findViewById(R.id.active_rewards_onboarding_icon4), R.drawable.onboarding_spinner);
        setTextViewText((TextView) findViewById(R.id.active_rewards_onboarding_section_3_content), R.string.AR_onboarding_common_item3_666);
        setTextViewText((TextView) findViewById(R.id.active_rewards_onboarding_section_4_title), R.string.AR_onboarding_probabilistic_item4_heading_699);
        setTextViewText((TextView) findViewById(R.id.active_rewards_onboarding_section_4_content), R.string.AR_onboarding_probabilistic_item4_698);
    }

    private void configureDefinedContent() {
        setIconImage((ImageView) findViewById(R.id.active_rewards_onboarding_icon4), R.drawable.onboarding_reward);
        setTextViewText((TextView) findViewById(R.id.active_rewards_onboarding_section_3_content), R.string.AR_learn_more_claim_reward_content_713);
        setTextViewText((TextView) findViewById(R.id.active_rewards_onboarding_section_4_title), R.string.AR_onboarding_defined_item4_heading_733);
        setTextViewText((TextView) findViewById(R.id.active_rewards_onboarding_section_4_content), R.string.AR_onboarding_defined_item4_732);
    }

    private void hideRewardsSection() {
        findViewById(R.id.active_rewards_onboarding_icon4).setVisibility(View.GONE);
        findViewById(R.id.active_rewards_onboarding_section_4_title).setVisibility(View.GONE);
        findViewById(R.id.active_rewards_onboarding_section_4_content).setVisibility(View.GONE);
    }

    private void setIconImage(ImageView icon, int iconResourceId) {
        icon.setImageResource(iconResourceId);
    }

    private void setTextViewText(TextView textView, int textResourceId) {
        textView.setText(textResourceId);
    }

    public void onGetStartedTapped(View view) {
        presenter.onGetStartedTapped();
    }

    public void onLearnMoreTapped(View view) {
        navigationCoordinator.navigateOnMenuItemFromActiveRewardsLanding(this, MenuItemType.LearnMore);
    }

    @Override
    public void navigateAway() {
        navigationCoordinator.navigateFromActiveRewardsOnboarding(this);
    }

    @Override
    public void showGenericErrorMessage() {
        showActivationErrorAlertIfNotPaused(getString(R.string.active_rewards_terms_and_conditions_activation_failed_alert_title),
                getString(R.string.error_unable_to_load_message_504));
    }

    @Override
    public void showConnectionErrorMessage() {
        showActivationErrorAlertIfNotPaused(getString(R.string.connectivity_error_alert_title_44),
                getString(R.string.connectivity_error_alert_message_45));
    }

    private void showActivationErrorAlertIfNotPaused(@NonNull String title, @NonNull String message) {
        showActivationErrorAlert(title, message);
    }

    private void showActivationErrorAlert(@NonNull String title, @NonNull String message) {
        AlertDialogFragment.create(ACTIVATION_ERROR_ALERT,
                title,
                message,
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43))
                .show(getSupportFragmentManager(), ACTIVATION_ERROR_ALERT);
    }

    public void showLoadingIndicator() {
        showLoadingIndicator(getThemeAccentColor());
    }

    public int getThemeAccentColor() {
        final TypedValue value = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        return value.data;
    }

    public void showLoadingIndicator(@ColorInt int colour) {
        showLoadingIndicator(colour, null);
    }

    public void showLoadingIndicator(String text) {
        showLoadingIndicator(getThemeAccentColor(), text);
    }

    public void showLoadingIndicator(@ColorInt int colour, String text) {
        if (getSupportFragmentManager().findFragmentByTag(LOADING_INDICATOR) != null) {
            return;
        }
        LoadingIndicatorFragment.newInstance(colour, text).show(getSupportFragmentManager(), LOADING_INDICATOR);
    }

    @Override
    public void hideLoadingIndicator() {
        LoadingIndicatorFragment loadingIndicatorFragment = (LoadingIndicatorFragment) getSupportFragmentManager().findFragmentByTag(LOADING_INDICATOR);
        if (loadingIndicatorFragment != null) {
            loadingIndicatorFragment.dismiss();
        }
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        if (event.getType().equals(ACTIVATION_ERROR_ALERT) && event.getClickedButtonType() == AlertDialogFragment.DismissedEvent.ClickedButtonType.Positive) {
            presenter.onGetStartedTapped();
        }
    }
}
