package com.vitalityactive.va.shared.content;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("FieldCanBeLocal")
public class BaseKeyValueContent implements OnboardingContent {
    @SerializedName("onboardingTitle")
    private String onboardingTitle = "";
    @SerializedName("onboardingSection1Title")
    private String onboardingSection1Title = "";
    @SerializedName("onboardingSection1Content")
    private String onboardingSection1Content = "";
    @SerializedName("onboardingSection2Title")
    private String onboardingSection2Title = "";
    @SerializedName("onboardingSection2Content")
    private String onboardingSection2Content = "";
    @SerializedName("onboardingSection3Title")
    private String onboardingSection3Title = "";
    @SerializedName("onboardingSection3Content")
    private String onboardingSection3Content = "";
    @SerializedName("learnMoreTitle")
    private String learnMoreTitle = "";
    @SerializedName("learnMoreContent")
    private String learnMoreContent = "";
    @SerializedName("learnMoreSection1Title")
    private String learnMoreSection1Title = "";
    @SerializedName("learnMoreSection1Content")
    private String learnMoreSection1Content = "";
    @SerializedName("learnMoreSection2Title")
    private String learnMoreSection2Title = "";
    @SerializedName("learnMoreSection2Content")
    private String learnMoreSection2Content = "";
    @SerializedName("learnMoreSection3Title")
    private String learnMoreSection3Title = "";
    @SerializedName("learnMoreSection3Content")
    private String learnMoreSection3Content = "";

    @Override
    public String getOnboardingTitle() {
        return onboardingTitle;
    }

    @Override
    public String getOnboardingSection1Title() {
        return onboardingSection1Title;
    }

    @Override
    public String getOnboardingSection1Content() {
        return onboardingSection1Content;
    }

    @Override
    public String getOnboardingSection2Title() {
        return onboardingSection2Title;
    }

    @Override
    public String getOnboardingSection2Content() {
        return onboardingSection2Content;
    }

    @Override
    public String getOnboardingSection3Title() {
        return onboardingSection3Title;
    }

    @Override
    public String getOnboardingSection3Content() {
        return onboardingSection3Content;
    }

    @Override
    public String getLearnMoreTitle() {
        return learnMoreTitle;
    }

    @Override
    public String getLearnMoreContent() {
        return learnMoreContent;
    }

    @Override
    public String getLearnMoreSection1Title() {
        return learnMoreSection1Title;
    }

    @Override
    public String getLearnMoreSection1Content() {
        return learnMoreSection1Content;
    }

    @Override
    public String getLearnMoreSection2Title() {
        return learnMoreSection2Title;
    }

    @Override
    public String getLearnMoreSection2Content() {
        return learnMoreSection2Content;
    }

    @Override
    public String getLearnMoreSection3Title() {
        return learnMoreSection3Title;
    }

    @Override
    public String getLearnMoreSection3Content() {
        return learnMoreSection3Content;
    }
}
