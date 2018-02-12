package com.vitalityactive.va.vhr.content;

import android.content.Context;

import com.vitalityactive.va.R;
import com.vitalityactive.va.shared.UnitOfMeasureStringLoader;

public class VHRContentFromStringResource extends UnitOfMeasureStringLoader implements VHRContent {
    public VHRContentFromStringResource(Context context) {
        super(context);
    }

    @Override
    public String getOnboardingTitle() {
        return getString(R.string.home_card_card_section_title_291);
    }

    @Override
    public String getOnboardingSection1Title() {
        return getString(R.string.onboarding_section_1_title_293);
    }

    @Override
    public String getOnboardingSection1Content() {
        return getString(R.string.onboarding_section_1_message_294);
    }

    @Override
    public String getOnboardingSection2Title() {
        return getString(R.string.onboarding_section_2_title_295);
    }

    @Override
    public String getOnboardingSection2Content() {
        return getString(R.string.onboarding_section_2_message_296);
    }

    @Override
    public String getOnboardingSection3Title() {
        return getString(R.string.onboarding_section_3_title_297);
    }

    @Override
    public String getOnboardingSection3Content() {
        return getString(R.string.onboarding_section_3_message_298);
    }

    @Override
    public String getLearnMoreTitle() {
        return getString(R.string.learn_more_heading_1_title_308);
    }

    @Override
    public String getLearnMoreContent() {
        return getString(R.string.learn_more_heading_1_message_309);
    }

    @Override
    public String getLearnMoreSection1Title() {
        return getString(R.string.learn_more_section_1_title_310);
    }

    @Override
    public String getLearnMoreSection1Content() {
        return getString(R.string.learn_more_section_1_message_311);
    }

    @Override
    public String getLearnMoreSection2Title() {
        return getString(R.string.learn_more_section_2_title_312);
    }

    @Override
    public String getLearnMoreSection2Content() {
        return getString(R.string.learn_more_section_2_message_313);
    }

    @Override
    public String getLearnMoreSection3Title() {
        return getString(R.string.learn_more_section_3_title_314);
    }

    @Override
    public String getLearnMoreSection3Content() {
        return getString(R.string.learn_more_section_3_message_315);
    }
}
