package com.vitalityactive.va.login;

import android.support.annotation.NonNull;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.myhealth.vitalityage.VitalityAgePreferencesManager;
import com.vitalityactive.va.onboarding.LoginRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LoginInteractorImpl extends BaseLoginInteractorImpl {


    @Inject
    LoginInteractorImpl(@NonNull LoginServiceClient loginServiceClient, @NonNull EventDispatcher eventDispatcher, LoginRepository loginRepository, DeviceSpecificPreferences preferences, BaseURLSwitcher baseURLSwitcher, VitalityAgePreferencesManager vitalityAgePreferencesManager,
                        PartyInformationRepository partyInformationRepository) {
        super(loginServiceClient, eventDispatcher, loginRepository, preferences, baseURLSwitcher, vitalityAgePreferencesManager, partyInformationRepository);
    }
}
