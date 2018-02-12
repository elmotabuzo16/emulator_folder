package com.vitalityactive.va.dependencyinjection.pns;

import com.vitalityactive.va.settings.PasswordChangedActivity;
import com.vitalityactive.va.settings.SettingChangePasswordActivity;
import com.vitalityactive.va.userpreferences.BaseCommunicationPreferencesActivity;
import com.vitalityactive.va.userpreferences.BaseFirstTimeUserPreferencesActivity;
import com.vitalityactive.va.userpreferences.BasePrivacyPreferencesActivity;
import com.vitalityactive.va.userpreferences.BaseSecurityPreferencesActivity;
import com.vitalityactive.va.userpreferences.FirstTimeUserPreferencesActivity;
import com.vitalityactive.va.vhr.VHRPrivacyPolicyActivity;
import com.vitalityactive.va.vhr.questions.VHRQuestionnaireActivity;

import dagger.Subcomponent;

@PNSCaptureScope
@Subcomponent(modules = {PNSCaptureModule.class})
public interface PNSCaptureDependencyInjector {

    void inject(BaseFirstTimeUserPreferencesActivity activity);

    void inject(BaseCommunicationPreferencesActivity activity);

    void inject(BaseSecurityPreferencesActivity activity);

    void inject(BasePrivacyPreferencesActivity activity);

    void inject(SettingChangePasswordActivity activity);

    void inject(PasswordChangedActivity activity);

}
