package com.vitalityactive.va.login;

import android.support.annotation.NonNull;

import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.EnvironmentPrefix;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.register.entity.Username;

/**
 * Created by peter.ian.t.betos on 31/01/2018.
 */

public class UKEURLSwitcher extends BaseURLSwitcher {

    public UKEURLSwitcher(DeviceSpecificPreferences devicePreferences, ServiceGenerator serviceGenerator) {
        super(devicePreferences, serviceGenerator);
    }

    @NonNull
    public Username switchBaseURL(@NonNull Username username) {
        if (switchBaseURL(username, EnvironmentPrefix.QA_FF, BuildConfig.QA_FF_BASE_URL, BuildConfig.QA_FF_BASIC_AUTH_TOKEN)) {
            return getNewUsernameWithoutPrefix(username, EnvironmentPrefix.QA_FF);
        }
        return super.switchBaseURL(username);
    }

    public void switchToQaFfUrl() {
        switchBaseToUrl(BuildConfig.QA_FF_BASE_URL, BuildConfig.QA_FF_BASIC_AUTH_TOKEN);
    }

}
