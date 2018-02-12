package com.vitalityactive.va.authentication;

import com.vitalityactive.va.DeviceSpecificPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BasicAuthorizationProvider {

    private DeviceSpecificPreferences preferences;

    @Inject
    public BasicAuthorizationProvider(DeviceSpecificPreferences preferences) {
        this.preferences = preferences;
    }

    public String getAuthorization() {
        return "Basic " + preferences.getCurrentBasicAuthToken();
    }
}
