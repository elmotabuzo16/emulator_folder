package com.vitalityactive.va.dependencyinjection.vna;

import com.vitalityactive.va.vna.VNAPrivacyPolicyActivity;
import com.vitalityactive.va.vna.VNAQuestionnaireActivity;

import dagger.Subcomponent;

@VNACaptureScope
@Subcomponent(modules = {VNACaptureModule.class})
public interface VNACaptureDependencyInjector {
    void inject(VNAQuestionnaireActivity activity);

    void inject(VNAPrivacyPolicyActivity activity);
}
