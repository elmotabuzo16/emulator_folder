package com.vitalityactive.va.dependencyinjection.mwb;

import com.vitalityactive.va.mwb.MWBPrivacyPolicyActivity;
import com.vitalityactive.va.mwb.questions.MWBQuestionnaireActivity;

import dagger.Subcomponent;

/**
 * Created by christian.j.p.capin on 1/30/2018.
 */
@MWBCaptureScope
@Subcomponent(modules = {MWBCaptureModule.class})
public interface MWBCaptureDependencyInjector{
    void inject(MWBPrivacyPolicyActivity activity);

    void inject(MWBQuestionnaireActivity activity);
}
