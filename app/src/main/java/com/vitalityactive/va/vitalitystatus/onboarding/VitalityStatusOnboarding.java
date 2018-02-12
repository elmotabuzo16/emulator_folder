package com.vitalityactive.va.vitalitystatus.onboarding;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.shared.activities.BaseOnboardingActivity;
import com.vitalityactive.va.shared.content.OnboardingContent;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.vitalitystatus.VitalityStatusContent;

import javax.inject.Inject;

public class VitalityStatusOnboarding extends BaseOnboardingActivity {
    @Inject
    VitalityStatusContent content;
    @Inject
    AppConfigRepository appConfigRepository;
    private @ColorInt int primaryColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        primaryColor = Color.parseColor(appConfigRepository.getGlobalTintColorHex());

        setIconsToPrimaryColor();
        setGotItButtonToPrimaryColor();
        setStatusBarColor(primaryColor);
    }

    private void setGotItButtonToPrimaryColor() {
        findViewById(R.id.get_started_button).setBackgroundColor(primaryColor);
    }

    private void setIconsToPrimaryColor() {
        ViewUtilities.tintDrawable(getImageViewDrawable(R.id.onboarding_section1_icon), primaryColor);
        ViewUtilities.tintDrawable(getImageViewDrawable(R.id.onboarding_section2_icon), primaryColor);
    }

    private Drawable getImageViewDrawable(int onboarding_section1_icon) {
        return ((ImageView) findViewById(onboarding_section1_icon)).getDrawable();
    }

    @Override
    protected void setupDependencyInjection() {
        getDependencyInjector().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vitality_status_onboarding;
    }

    @NonNull
    @Override
    protected OnboardingContent getContent() {
        return content;
    }

    @Override
    protected void navigateAfterGotItTapped() {
        navigationCoordinator.navigateToVitalityStatusLanding(this);
    }
}
