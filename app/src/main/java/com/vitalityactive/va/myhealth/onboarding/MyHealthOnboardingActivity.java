package com.vitalityactive.va.myhealth.onboarding;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.myhealth.content.MyHealthOnboardingContent;
import com.vitalityactive.va.shared.activities.BaseOnboardingActivity;
import com.vitalityactive.va.shared.content.OnboardingContent;

import javax.inject.Inject;


public class MyHealthOnboardingActivity extends BaseOnboardingActivity {

    @Inject
    MyHealthOnboardingContent content;
    @Inject
    AppConfigRepository appConfigRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        setViewsGlobalTintColor();
    }

    @Override
    protected void setupDependencyInjection() {
        getDependencyInjector().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_health_onboarding;
    }

    @NonNull
    @Override
    protected OnboardingContent getContent() {
        return content;
    }

    @Override
    protected void navigateAfterGotItTapped() {
        setResult(RESULT_OK);
        finish();
    }

    private void setViewsGlobalTintColor() {
        try {
            findViewById(R.id.my_health_onboarding_button).setBackgroundColor(globalTintColor());
            ((ImageView) findViewById(R.id.help_icon)).setColorFilter(globalTintColor(), android.graphics.PorterDuff.Mode.MULTIPLY);
            ((ImageView) findViewById(R.id.manage_health_icon)).setColorFilter(globalTintColor(), android.graphics.PorterDuff.Mode.MULTIPLY);
            ((ImageView) findViewById(R.id.health_tips_icon)).setColorFilter(globalTintColor(), android.graphics.PorterDuff.Mode.MULTIPLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setStatusBarColor() {
        try {
            super.setStatusBarColor(globalTintColor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected @ColorInt
    int globalTintColor() {
        return Color.parseColor(appConfigRepository.getGlobalTintColorHex());
    }
}
