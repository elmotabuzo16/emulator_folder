package com.vitalityactive.va.vhc;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.uicomponents.OnboardingCirclesAnimator;
import com.vitalityactive.va.myhealth.vitalityage.VitalityAgePresenter;

import javax.inject.Inject;

public class VHCMeasurementsConfirmedActivity extends BaseActivity {

    @Inject
    VitalityAgePresenter vitalityAgePresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vhc_measurements_confirmed);

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
        navigationCoordinator.navigateAfterVHCSubmitSuccessful(this, true);
    }

    protected void injectDependencies(DependencyInjector dependencyInjector) {
        getDependencyInjector().inject(this);
    }
}
