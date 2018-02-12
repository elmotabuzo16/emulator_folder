package com.vitalityactive.va.snv.confirmandsubmit.view;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.uicomponents.OnboardingCirclesAnimator;



public class SNVDataConfirmedActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snv_data_confirmed);

        final ViewGroup onboardingCircles = (ViewGroup) findViewById(R.id.non_smokers_onboarding_circles);
        onboardingCircles.setVisibility(View.INVISIBLE);
        final OnboardingCirclesAnimator circlesAnimator = new OnboardingCirclesAnimator(this, onboardingCircles, R.drawable.onboarding_activated, getResources().getDisplayMetrics().widthPixels);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                circlesAnimator.animateCirclesAndIcon();
                onboardingCircles.setVisibility(View.VISIBLE);
            }
        }, 500);

        injectDependencies(getDependencyInjector());
    }

    public void onVHCMeasurementSubmitButtonTapped(View view) {
        navigationCoordinator.navigateAfterSNVSubmitSuccessful(this, true);
    }

    protected void injectDependencies(DependencyInjector dependencyInjector) {
        getDependencyInjector().inject(this);
    }
}
