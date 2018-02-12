package com.vitalityactive.va.dependencyinjection.vhr;

import com.vitalityactive.va.vhr.VHRPrivacyPolicyActivity;
import com.vitalityactive.va.vhr.questions.VHRQuestionnaireActivity;

import dagger.Subcomponent;

@VHRCaptureScope
@Subcomponent(modules = {VHRCaptureModule.class})
public interface VHRCaptureDependencyInjector {
    void inject(VHRPrivacyPolicyActivity activity);

    void inject(VHRQuestionnaireActivity activity);
}
