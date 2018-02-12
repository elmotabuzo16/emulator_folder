package com.vitalityactive.va.vitalitystatus;

import android.content.Context;
import android.support.annotation.NonNull;

import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.ProductFeatureCategory;
import com.vitalityactive.va.shared.content.OnboardingContent;

public class VitalityStatusContent implements OnboardingContent {
    private final Context context;

    public VitalityStatusContent(Context context) {
        this.context = context;
    }

    @Override
    public String getOnboardingTitle() {
        return getString(R.string.Status_onboarding_title_602);
    }

    @Override
    public String getOnboardingSection1Title() {
        return getString(R.string.Status_onboarding_section_1_title_603);
    }

    @Override
    public String getOnboardingSection1Content() {
        return getString(R.string.Status_onboarding_section_1_message_604);
    }

    @Override
    public String getOnboardingSection2Title() {
        return getString(R.string.Status_onboarding_section_2_title_605);
    }

    @Override
    public String getOnboardingSection2Content() {
        return getString(R.string.Status_onboarding_section_2_message_606);
    }

    @Override
    public String getOnboardingSection3Title() {
        return "";
    }

    @Override
    public String getOnboardingSection3Content() {
        return "";
    }

    @Override
    public String getLearnMoreTitle() {
        return "";
    }

    @Override
    public String getLearnMoreContent() {
        return "";
    }

    @Override
    public String getLearnMoreSection1Title() {
        return "";
    }

    @Override
    public String getLearnMoreSection1Content() {
        return "";
    }

    @Override
    public String getLearnMoreSection2Title() {
        return "";
    }

    @Override
    public String getLearnMoreSection2Content() {
        return "";
    }

    @Override
    public String getLearnMoreSection3Title() {
        return "";
    }

    @Override
    public String getLearnMoreSection3Content() {
        return "";
    }

    @NonNull
    protected String getString(int resourceId) {
        return context.getString(resourceId);
    }
}
