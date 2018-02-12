package com.vitalityactive.va.myhealth.content;

import com.vitalityactive.va.myhealth.entity.VitalityAge;
import com.vitalityactive.va.shared.content.OnboardingContent;

public interface MyHealthOnboardingContent extends OnboardingContent {

    void setFeedbackTypeKey(int feedbackTypeKey);

    void initialize(VitalityAge vitalityAge);

    int getLearnMoreSection1Icon();

    int getLearnMoreSection2Icon();

    int getLearnMoreSection3Icon();
}
