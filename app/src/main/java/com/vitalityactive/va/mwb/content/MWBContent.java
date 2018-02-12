package com.vitalityactive.va.mwb.content;

import com.vitalityactive.va.shared.content.OnboardingContent;

/**
<<<<<<< Upstream, based on branch 'master' of http://gerrit.devops.ose.discsrv.co.za/dpm/mobile/vitalityactive-android
 * Created by paule.glenn.s.acuin on 1/30/2018.
 */

public interface MWBContent extends OnboardingContent {
    String getBmiSection1Title();
    String getBmiSection1Content();
    String getBmiSection2Title();
    String getBmiSection2Content();
    String getBmiSection3Title();
    String getBmiSection3Content();

    String getWaistCircumferenceSection1Title();
    String getWaistCircumferenceSection1Content();
    String getWaistCircumferenceSection2Title();
    String getWaistCircumferenceSection2Content();
    String getWaistCircumferenceSection3Title();
    String getWaistCircumferenceSection3Content();

    String getGlucoseSection1Title();
    String getGlucoseSection1Content();
    String getGlucoseSection2Title();
    String getGlucoseSection2Content();
    String getGlucoseSection3Title();
    String getGlucoseSection3Content();

    String getBloodPressureSection1Title();
    String getBloodPressureSection1Content();
    String getBloodPressureSection2Title();
    String getBloodPressureSection2Content();
    String getBloodPressureSection3Title();
    String getBloodPressureSection3Content();

    String getCholesterolSection1Title();
    String getCholesterolSection1Content();
    String getCholesterolSection2Title();
    String getCholesterolSection2Content();
    String getCholesterolSection3Title();
    String getCholesterolSection3Content();

    String getHba1cSection1Title();
    String getHba1cSection1Content();
    String getHba1cSection2Title();
    String getHba1cSection2Content();
    String getHba1cSection3Title();
    String getHba1cSection3Content();

    String getLandingTitle();
    String getLandingSubtitle();

    String getMeasurable0CaptureContent();
    String getMeasurable1CaptureContent();
    String getMeasurable2CaptureContent();
    String getMeasurable3CaptureContent();
    String getMeasurable4CaptureContent();
    String getMeasurable5CaptureContent();
    String getMeasurable6CaptureContent();

    String getBmiGroupTitle();
    String getWaistCircumferenceGroupTitle();
    String getGlucoseGroupTitle();
    String getBloodPressureGroupTitle();
    String getCholesterolGroupTitle();
    String getOtherBloodLipidsGroupTitle();
    String getHbA1cGroupTitle();
    String getUrinaryProteinGroupTitle();

    String getUrinaryProteinSection1Title();
    String getUrinaryProteinSection1Content();
    String getUrinaryProteinSection2Title();
    String getUrinaryProteinSection2Content();
    String getUrinaryProteinSection3Title();
    String getUrinaryProteinSection3Content();

    String getFieldName(int eventTypeKey);

    String getFieldPropertyName(int healthAttributeTypeKey);

}
