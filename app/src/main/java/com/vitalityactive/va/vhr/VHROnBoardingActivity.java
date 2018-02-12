package com.vitalityactive.va.vhr;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.R;
import com.vitalityactive.va.shared.activities.BaseOnboardingActivity;
import com.vitalityactive.va.shared.content.OnboardingContent;
import com.vitalityactive.va.vhr.content.VHRContent;

import javax.inject.Inject;

public class VHROnBoardingActivity extends BaseOnboardingActivity {
    @Inject
    VHRContent content;

    @Inject
    InsurerConfigurationRepository insurerConfigurationRepository;

    @Override
    protected void setupDependencyInjection() {
        getDependencyInjector().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vhr_onboarding;
    }

    @NonNull
    @Override
    protected OnboardingContent getContent() {
        return content;
    }

    @Override
    protected void navigateAfterGotItTapped() {
        navigationCoordinator.navigateAfterVHRGotItTapped(this);
    }

    public void onDisclaimerButtonTapped(View view) {
        navigationCoordinator.navigateAfterVHRDisclaimerTapped(this);
    }

    @Override
    protected void setupSecondaryButton(Button button) {
        button.setVisibility(insurerConfigurationRepository.shouldShowVHRDisclaimer() ? View.VISIBLE : View.GONE);
    }
}
