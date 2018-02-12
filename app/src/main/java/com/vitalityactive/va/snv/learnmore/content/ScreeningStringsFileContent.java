package com.vitalityactive.va.snv.learnmore.content;

import android.content.Context;
import android.support.annotation.NonNull;

import com.vitalityactive.va.R;

/**
 * Created by stephen.rey.w.avila on 11/30/2017.
 */

public class ScreeningStringsFileContent implements ScreeningDeclarationContent {

    private final Context context;

    public ScreeningStringsFileContent(Context context) {
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
        return getString(R.string.SV_learn_more_main_title_1025);
    }

    @Override
    public String getLearnMoreTitle() {
        return getOnboardingTitle();
    }

    @Override
    public String getLearnMoreContent() {
        return getString(R.string.SV_learn_more_main_message_1026);
    }

    @Override
    public String getLearnMoreSection1Title() {
        return getString(R.string.SV_learn_more_section_1_title_1027);
    }

    @Override
    public String getLearnMoreSection1Content() {
        return getString(R.string.SV_learn_more_section1_message_1028);
    }

    @Override
    public String getLearnMoreSection2Title() {
        return getString(R.string.SV_learn_more_section_2_title_1030);

    }

    @Override
    public String getLearnMoreSection2Content() {
        return getString(R.string.SV_onboarding_section_2_message_1005);

    }

    @Override
    public String getLearnMoreSection3Title() {
        return getString(R.string.SV_learn_more_section_3_title_1032);

    }

    @Override
    public String getLearnMoreSection3Content() {
        return getString(R.string.SV_learn_more_section_3_message_1033);

    }
    @Override
    public String getLearnMoreSection1LinkContent(){
        return getString(R.string.SV_partner_title_1029);

    }

    @NonNull
    private String getString(int resourceId) {
        return context.getString(resourceId);
    }
}
