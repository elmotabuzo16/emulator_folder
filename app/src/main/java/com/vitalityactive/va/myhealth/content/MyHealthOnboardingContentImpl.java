package com.vitalityactive.va.myhealth.content;

import android.content.Context;

import com.vitalityactive.va.R;
import com.vitalityactive.va.myhealth.entity.VitalityAge;
import com.vitalityactive.va.shared.UnitOfMeasureStringLoader;

public class MyHealthOnboardingContentImpl extends UnitOfMeasureStringLoader implements MyHealthOnboardingContent {

    public VitalityAge vitalityAge;
    private Integer feedbackTypeKey;

    public MyHealthOnboardingContentImpl(Context activeApplication) {
        super(activeApplication);
    }

    @Override
    public String getOnboardingTitle() {
        return getString(R.string.menu_myhealth_button_7);
    }

    @Override
    public String getOnboardingSection1Title() {
        return getString(R.string.my_health_onboarding_section1_title_628);
    }

    @Override
    public String getOnboardingSection1Content() {
        return getString(R.string.my_health_onboarding_section1_description_629);
    }

    @Override
    public String getOnboardingSection2Title() {
        return getString(R.string.my_health_onboarding_section2_title_632);
    }

    @Override
    public String getOnboardingSection2Content() {
        return getString(R.string.my_health_onboarding_section2_description_633);
    }

    @Override
    public String getOnboardingSection3Title() {
        return getString(R.string.my_health_onboarding_section3_title_634);
    }

    @Override
    public String getOnboardingSection3Content() {
        return getString(R.string.my_health_onboarding_section3_description_635);
    }

    @Override
    public String getLearnMoreTitle() {
        return getString(MyHealthLearnMoreContent.LearnMoreHeader.getTitleResourceId(feedbackTypeKey));
    }

    @Override
    public String getLearnMoreContent() {
        return MyHealthLearnMoreContent.getLearnMoreHeader(context, getVitalityAge());
    }

    @Override
    public String getLearnMoreSection1Title() {
        return getString(MyHealthLearnMoreContent.LearnMoreFirstSection.getTitleResourceId(feedbackTypeKey));
    }

    @Override
    public String getLearnMoreSection1Content() {
        return getString(MyHealthLearnMoreContent.LearnMoreFirstSection.getContentResourceId(feedbackTypeKey));
    }

    @Override
    public String getLearnMoreSection2Title() {
        return getString(MyHealthLearnMoreContent.LearnMoreSecondSection.getTitleResourceId(feedbackTypeKey));
    }

    @Override
    public String getLearnMoreSection2Content() {
        return getString(MyHealthLearnMoreContent.LearnMoreSecondSection.getContentResourceId(feedbackTypeKey));
    }

    @Override
    public String getLearnMoreSection3Title() {
        return getString(R.string.my_health_learn_more_understand_your_health_title_750);
    }

    @Override
    public String getLearnMoreSection3Content() {
        return getString(R.string.my_health_learn_more_understand_your_health_content_751);
    }

    @Override
    public int getLearnMoreSection1Icon() {
        return MyHealthLearnMoreContent.LearnMoreFirstSection.getIconResourceId(feedbackTypeKey);
    }

    @Override
    public int getLearnMoreSection2Icon() {
        return MyHealthLearnMoreContent.LearnMoreSecondSection.getIconResourceId(feedbackTypeKey);
    }

    @Override
    public int getLearnMoreSection3Icon() {
        return R.drawable.lightbulb_med;
    }

    public void setFeedbackTypeKey(int feedbackTypeKey) {
        this.feedbackTypeKey = feedbackTypeKey;
    }

    public void initialize(VitalityAge vitalityAge) {
        this.vitalityAge = vitalityAge;
        this.feedbackTypeKey = vitalityAge.getEffectiveType();
    }

    public VitalityAge getVitalityAge() {
        return vitalityAge;
    }
}
