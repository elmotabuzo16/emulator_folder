package com.vitalityactive.va.vhc;

import android.support.annotation.NonNull;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.shared.activities.BaseOnboardingActivity;
import com.vitalityactive.va.shared.content.OnboardingContent;
import com.vitalityactive.va.vhc.content.VHCHealthAttributeContent;

import javax.inject.Inject;

public class VHCOnboardingActivity extends BaseOnboardingActivity {
    @Inject
    VHCHealthAttributeContent content;

    @Override
    protected void setupDependencyInjection() {
        getDependencyInjector().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vhc_onboarding;
    }

    @NonNull
    @Override
    protected OnboardingContent getContent() {
        return content;
    }

    @Override
    protected void navigateAfterGotItTapped() {
        navigationCoordinator.navigateAfterVHCGetStartedTapped(this);
    }

    public void onLearnMoreTapped(View view) {
        navigationCoordinator.navigateAfterVHCLearnMoreTapped(this);
    }
}
