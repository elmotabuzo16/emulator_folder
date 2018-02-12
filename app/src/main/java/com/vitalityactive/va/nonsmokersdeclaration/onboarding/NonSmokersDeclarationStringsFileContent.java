package com.vitalityactive.va.nonsmokersdeclaration.onboarding;

import android.content.Context;
import android.support.annotation.NonNull;

import com.vitalityactive.va.R;
import com.vitalityactive.va.nonsmokersdeclaration.NonSmokersDeclarationContent;

public class NonSmokersDeclarationStringsFileContent implements NonSmokersDeclarationContent {
    private final Context context;

    public NonSmokersDeclarationStringsFileContent(Context context) {
        this.context = context;
    }

    @Override
    public String getOnboardingSection1Title() {
        return getString(R.string.onboarding_section_1_title_99);
    }

    @Override
    public String getOnboardingSection1Content() {
        return getString(R.string.onboarding_section_1_message_100);
    }

    @Override
    public String getOnboardingSection2Title() {
        return getString(R.string.onboarding_section_2_title_101);
    }

    @Override
    public String getOnboardingSection2Content() {
        return getString(R.string.onboarding_section_2_message_102);
    }

    @Override
    public String getOnboardingTitle() {
        return getString(R.string.home_card_card_title_96);
    }

    @Override
    public String getLearnMoreTitle() {
        return getOnboardingTitle();
    }

    @Override
    public String getLearnMoreContent() {
        return getString(R.string.learn_more_group_message_105);
    }

    @Override
    public String getLearnMoreSection1Title() {
        return getString(R.string.learn_more_section_1_title_106);
    }

    @Override
    public String getLearnMoreSection1Content() {
        return getString(R.string.learn_more_section_1_message_107);
    }

    @Override
    public String getLearnMoreSection2Title() {
        return getString(R.string.learn_more_section_2_title_108);
    }

    @Override
    public String getLearnMoreSection2Content() {
        return getString(R.string.learn_more_section_2_message_109);
    }

    @Override
    public String getLearnMoreSection3Title() {
        return getString(R.string.learn_more_section_3_title_110);
    }

    @Override
    public String getLearnMoreSection3Content() {
        return getString(R.string.learn_more_section_3_message_111);
    }

    @NonNull
    private String getString(int resourceId) {
        return context.getString(resourceId);
    }
}
