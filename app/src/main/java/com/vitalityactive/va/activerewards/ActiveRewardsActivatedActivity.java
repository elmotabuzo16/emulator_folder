package com.vitalityactive.va.activerewards;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.OnboardingCirclesAnimator;
import com.vitalityactive.va.uicomponents.OnboardingContentConfigurator;

public class ActiveRewardsActivatedActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_active_rewards_activated);

        final ViewGroup onboardingCircles = (ViewGroup) findViewById(R.id.onboarding_circles);
        onboardingCircles.setVisibility(View.INVISIBLE);
        final OnboardingCirclesAnimator circlesAnimator = new OnboardingCirclesAnimator(this, onboardingCircles, R.drawable.onboarding_activated, getResources().getDisplayMetrics().widthPixels);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                circlesAnimator.animateCirclesAndIcon();
                onboardingCircles.setVisibility(View.VISIBLE);
            }
        }, 500);

        new OnboardingContentConfigurator((ViewGroup) findViewById(R.id.onboarding_content))
                .setTitleText(R.string.AR_activated_heading_672)
                .setSubtitleText(R.string.AR_activated_message_645)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onGotItTapped(v);
                    }
                });
    }

    public void onGotItTapped(View view) {
        navigationCoordinator.navigateAfterActiveRewardsActivationAcknowledged(this);
    }
}
