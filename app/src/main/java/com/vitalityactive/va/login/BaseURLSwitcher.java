package com.vitalityactive.va.login;

import android.support.annotation.NonNull;

import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.EnvironmentPrefix;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.register.entity.Username;

public class BaseURLSwitcher {

    private final DeviceSpecificPreferences devicePreferences;
    private final ServiceGenerator serviceGenerator;

    public BaseURLSwitcher(DeviceSpecificPreferences devicePreferences, ServiceGenerator serviceGenerator) {
        this.devicePreferences = devicePreferences;
        this.serviceGenerator = serviceGenerator;
    }

    @NonNull
    public Username switchBaseURL(@NonNull Username username) {
        if (switchBaseURL(username, EnvironmentPrefix.DEV, BuildConfig.DEV_BASE_URL, BuildConfig.DEV_BASIC_AUTH_TOKEN)) {
            return getNewUsernameWithoutPrefix(username, EnvironmentPrefix.DEV);
        }

        if (switchBaseURL(username, EnvironmentPrefix.TEST, BuildConfig.TEST_BASE_URL, BuildConfig.TEST_BASIC_AUTH_TOKEN)) {
            return getNewUsernameWithoutPrefix(username, EnvironmentPrefix.TEST);
        }

        if (switchBaseURL(username, EnvironmentPrefix.CA_TEST, BuildConfig.CA_TEST_BASE_URL, BuildConfig.CA_TEST_BASIC_AUTH_TOKEN)) {
            return getNewUsernameWithoutPrefix(username, EnvironmentPrefix.CA_TEST);
        }

        if (switchBaseURL(username, EnvironmentPrefix.QA, BuildConfig.QA_BASE_URL, BuildConfig.QA_BASIC_AUTH_TOKEN)) {
            return getNewUsernameWithoutPrefix(username, EnvironmentPrefix.QA);
        }

        if (switchBaseURL(username, EnvironmentPrefix.QA_CA, BuildConfig.QA_CA_BASE_URL, BuildConfig.QA_CA_BASIC_AUTH_TOKEN)) {
            return getNewUsernameWithoutPrefix(username, EnvironmentPrefix.QA_CA);
        }

        if (switchBaseURL(username, EnvironmentPrefix.EAGLE, BuildConfig.EAGLE_CA_BASE_URL, BuildConfig.EAGLE_CA_BASIC_AUTH_TOKEN)) {
            return getNewUsernameWithoutPrefix(username, EnvironmentPrefix.EAGLE);
        }

        serviceGenerator.setBaseUrl(BuildConfig.PROD_BASE_URL, BuildConfig.PROD_BASIC_AUTH_TOKEN);
        return username;
    }

    @SuppressWarnings("unused")
    public void switchToDevUrl() {
        switchBaseToUrl(BuildConfig.DEV_BASE_URL, BuildConfig.DEV_BASIC_AUTH_TOKEN);
    }

    @SuppressWarnings("unused")
    public void switchToTestUrl() {
        switchBaseToUrl(BuildConfig.TEST_BASE_URL, BuildConfig.TEST_BASIC_AUTH_TOKEN);
    }

    @SuppressWarnings("unused")
    public void switchToQaUrl() {
        switchBaseToUrl(BuildConfig.QA_BASE_URL, BuildConfig.QA_BASIC_AUTH_TOKEN);
    }

    @SuppressWarnings("unused")
    public void switchToProdUrl() {
        switchBaseToUrl(BuildConfig.PROD_BASE_URL, BuildConfig.PROD_BASIC_AUTH_TOKEN);
    }

    @NonNull
    protected Username getNewUsernameWithoutPrefix(@NonNull Username username, @EnvironmentPrefix String prefix) {
        devicePreferences.setCurrentEnvironmentPrefix(prefix);
        return new Username(username.getText().toString().replace(prefix, ""));
    }

    protected boolean switchBaseURL(@NonNull Username username, String prefix, String baseUrl, String basicAuthToken) {
        if (hasPrefix(username, prefix)) {
            switchBaseToUrl(baseUrl, basicAuthToken);
            return true;
        }
        return false;
    }

    public void switchBaseToUrl(String baseUrl, String basicAuthToken) {
        serviceGenerator.setBaseUrl(baseUrl, basicAuthToken);
    }

    private boolean hasPrefix(@NonNull Username username, String desiredPrefix) {
        return BuildConfig.ENABLE_ENVIRONMENT_SWITCH && username.getText().toString().trim().toLowerCase().startsWith(desiredPrefix);
    }
}
