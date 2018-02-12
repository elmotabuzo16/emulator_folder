package com.vitalityactive.va.cms.keyvaluecontent;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.vitalityactive.va.nonsmokersdeclaration.NonSmokersDeclarationContent;

public class NonSmokersDeclarationKeyValueContent implements NonSmokersDeclarationContent {
    @NonNull
    @SerializedName("onboardingTitle")
    private String onboardingTitle = "";
    @NonNull
    @SerializedName("onboardingSection1Title")
    private String onboardingSection1Title = "";
    @NonNull
    @SerializedName("onboardingSection1Content")
    private String onboardingSection1Content = "";
    @NonNull
    @SerializedName("onboardingSection2Title")
    private String onboardingSection2Title = "";
    @NonNull
    @SerializedName("onboardingSection2Content")
    private String onboardingSection2Content = "";
    @NonNull
    @SerializedName("learnMoreTitle")
    private String learnMoreTitle = "";
    @NonNull
    @SerializedName("learnMoreContent")
    private String learnMoreContent = "";
    @NonNull
    @SerializedName("learnMoreSection1Title")
    private String learnMoreSection1Title = "";
    @NonNull
    @SerializedName("learnMoreSection1Content")
    private String learnMoreSection1Content = "";
    @NonNull
    @SerializedName("learnMoreSection2Title")
    private String learnMoreSection2Title = "";
    @NonNull
    @SerializedName("learnMoreSection2Content")
    private String learnMoreSection2Content = "";
    @NonNull
    @SerializedName("learnMoreSection3Title")
    private String learnMoreSection3Title = "";
    @NonNull
    @SerializedName("learnMoreSection3Content")
    private String learnMoreSection3Content = "";
    @NonNull
    @SerializedName("learnMoreSection4Title")
    private String learnMoreSection4Title = "";
    @NonNull
    @SerializedName("learnMoreSection4Content")
    private String learnMoreSection4Content = "";

    @NonNull
    @Override
    public String getOnboardingTitle() {
        return onboardingTitle;
    }

    @NonNull
    @Override
    public String getOnboardingSection1Title() {
        return onboardingSection1Title;
    }

    @NonNull
    public String getOnboardingSection1Content() {
        return onboardingSection1Content;
    }

    @NonNull
    @Override
    public String getOnboardingSection2Title() {
        return onboardingSection2Title;
    }

    @NonNull
    public String getOnboardingSection2Content() {
        return onboardingSection2Content;
    }

    @NonNull
    @Override
    public String getLearnMoreTitle() {
        return learnMoreTitle;
    }

    @NonNull
    public String getLearnMoreContent() {
        return learnMoreContent;
    }

    @NonNull
    @Override
    public String getLearnMoreSection1Title() {
        return learnMoreSection1Title;
    }

    @NonNull
    public String getLearnMoreSection1Content() {
        return learnMoreSection1Content;
    }

    @NonNull
    @Override
    public String getLearnMoreSection2Title() {
        return learnMoreSection2Title;
    }

    @NonNull
    public String getLearnMoreSection2Content() {
        return learnMoreSection2Content;
    }

    @NonNull
    @Override
    public String getLearnMoreSection3Title() {
        return learnMoreSection3Title;
    }

    @NonNull
    public String getLearnMoreSection3Content() {
        return learnMoreSection3Content;
    }

}
