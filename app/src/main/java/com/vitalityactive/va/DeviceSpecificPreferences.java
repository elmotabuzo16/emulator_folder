package com.vitalityactive.va;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

import com.vitalityactive.va.utilities.ObjectSerializerHelper;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.crypto.SecretKey;

public class DeviceSpecificPreferences {
    public static final String CURRENT_ENVIRONMENT_PREFIX = "CURRENT_ENVIRONMENT_PREFIX";
    public static final String CURRENT_BASE_URL = "CURRENT_BASE_URL";
    public static final String HAS_CURRENT_USER_SEEN_VITALITY_AGE = "HAS_CURRENT_USER_SEEN_VITALITY_AGE";
    public static final String HAS_CURRENT_USER_COMPLETED_VHC_BEFORE = "HAS_CURRENT_USER_COMPLETED_VHC_BEFORE";
    public static final String HAS_CURRENT_USER_COMPLETED_SNV_BEFORE = "HAS_CURRENT_USER_COMPLETED_SNV_BEFORE";
    public static final String CURRENT_MEMBERSHIP_PERIOD = "CURRENT_MEMBERSHIP_PERIOD";
    public static final String HAS_CURRENT_USER_SEEN_MY_HEALTH_ONBOARDING_SCREEN = "HAS_CURRENT_USER_SEEN_MY_HEALTH_ONBOARDING_SCREEN";
    private static final String HAS_CURRENT_USER_SEEN_ONBOARDING = "HAS_CURRENT_USER_SEEN_ONBOARDING";
    private static final String HAS_CURRENT_USER_SEEN_VHC_ONBOARDING = "HAS_CURRENT_USER_SEEN_VHC_ONBOARDING";
    private static final String HAS_CURRENT_USER_SEEN_VHR_ONBOARDING = "HAS_CURRENT_USER_SEEN_VHR_ONBOARDING";
    private static final String HAS_CURRENT_USER_SEEN_SNV_ONBOARDING = "HAS_CURRENT_USER_SEEN_SNV_ONBOARDING";
    private static final String HAS_CURRENT_USER_SEEN_MWB_ONBOARDING = "HAS_CURRENT_USER_SEEN_MWB_ONBOARDING";
    private static final String CURRENT_USER_HAS_SEEN_FIRST_TIME_PREFERENCES = "CURRENT_USER_HAS_SEEN_FIRST_TIME_PREFERENCES";
    private static final String HAS_CURRENT_USER_SEEN_WELLNESS_DEVICES_ONBOARDING = "HAS_CURRENT_USER_SEEN_WELLNESS_DEVICES_ONBOARDING";
    private static final String REMEMBER_ME = "REMEMBER_ME";
    private static final String FINGERPRINT = "FINGERPRINT";
    private static final String REMEMBERED_USERNAME_ENCRYPTED = "REMEMBERED_USERNAME_ENCRYPTED";
    private static final String REMEMBERED_PASSWORD_ENCRYPTED = "REMEMBERED_PASSWORD_ENCRYPTED";
    private static final String REMEMBERED_ENTITY_NUMBER_ENCRYPTED = "REMEMBERED_ENTITY_NUMBER_ENCRYPTED";
    private static final String SEND_ANALYTICS = "ANALYTICS_ENABLED";
    private static final String CRASH_REPORTS = "CRASH_REPORTS";
    private static final String HAS_CURRENT_USER_SEEN_VNA_ONBOARDING = "HAS_CURRENT_USER_SEEN_VNA_ONBOARDING";
    private static final String CURRENT_BASIC_AUTH_TOKEN = "CURRENT_BASIC_AUTH_TOKEN";
    private static final String HAS_CURRENT_USER_SEEN_VITALITY_STATUS_ONBOARDING = "HAS_CURRENT_USER_SEEN_VITALITY_STATUS_ONBOARDING";
    private static final String LATEST_STATUS_LEVEL = "LATEST_STATUS_LEVEL";
    private static final String ENCRYPTION_KEY = "ENCRYPTION_KEY";
    private static final String SHARED_STATUS = "SHARED_STATUS";
    private static final String HAS_CURRENT_USER_SEEN_LOGIN_FINGERPRINT_ENROLLMENT = "HAS_CURRENT_USER_SEEN_LOGIN_FINGERPRINT_ENROLLMENT";
    private static final String FINGERPRINT_SECRET_KEY = "SECRET_KEY";

    private final SharedPreferences sharedPreferences;
    private KeyStoreWrapper keyStoreWrapper;

    public DeviceSpecificPreferences(SharedPreferences sharedPreferences, Context context) {
        this(sharedPreferences, new KeyStoreWrapper(context));
    }

    DeviceSpecificPreferences(SharedPreferences sharedPreferences, KeyStoreWrapper keyStoreWrapper) {
        this.sharedPreferences = sharedPreferences;
        this.keyStoreWrapper = keyStoreWrapper;
    }

    public boolean hasCurrentUserSeenOnboarding() {
        return sharedPreferences.getBoolean(HAS_CURRENT_USER_SEEN_ONBOARDING, false);
    }

    public void setCurrentUserHasSeenOnboarding() {
        setSharedPreference(HAS_CURRENT_USER_SEEN_ONBOARDING, true);
    }

    public void setCurrentUserHasSeenVHCOnboarding() {
        setSharedPreference(HAS_CURRENT_USER_SEEN_VHC_ONBOARDING, true);
    }

    public void setCurrentUserHasSeenVHROnboarding() {
        setSharedPreference(HAS_CURRENT_USER_SEEN_VHR_ONBOARDING, true);
    }

    public void setCurrentUserHasSeenSNVOnboarding() {
        setSharedPreference(HAS_CURRENT_USER_SEEN_SNV_ONBOARDING, true);
    }

    public boolean hasCurrentUserSeenSNVOnboarding() {
        return sharedPreferences.getBoolean(HAS_CURRENT_USER_SEEN_SNV_ONBOARDING, false);
    }
    public void setCurrentUserHasSeenMWBOnboarding() {
        setSharedPreference(HAS_CURRENT_USER_SEEN_MWB_ONBOARDING, true);
    }

    public boolean hasCurrentUserSeenMWBOnboarding() {
        return sharedPreferences.getBoolean(HAS_CURRENT_USER_SEEN_MWB_ONBOARDING, false);
    }

    public void setCurrentUserHasSeenLoginFingerprintEnrollment() {
        setSharedPreference(HAS_CURRENT_USER_SEEN_LOGIN_FINGERPRINT_ENROLLMENT, true);
    }

    public boolean isCurrentUserHasSeenLoginFingerprintEnrollment() {
        return sharedPreferences.getBoolean(HAS_CURRENT_USER_SEEN_LOGIN_FINGERPRINT_ENROLLMENT, false);
    }

    private void setSharedPreference(String key, boolean value) {
        getEditor().putBoolean(key, value).apply();
    }

    @VisibleForTesting
    void setSharedPreference(String key, String value) {
        getEditor().putString(key, value).apply();
    }

    private void setSharedPreference(String key, Set<String> values) {
        getEditor().putStringSet(key, values).apply();
    }

    private void setSharedPreference(String key, int value) {
        getEditor().putInt(key, value).apply();
    }

    public void removeSharedPreference(String key) {
        getEditor().remove(key);
    }

    private SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    public boolean currentUserHasSeenFirstTimePreferences() {
        return sharedPreferences.getBoolean(CURRENT_USER_HAS_SEEN_FIRST_TIME_PREFERENCES, false);
    }

    public void setCurrentUserHasSeenFirstTimePreferences() {
        setSharedPreference(CURRENT_USER_HAS_SEEN_FIRST_TIME_PREFERENCES, true);
    }

    public boolean isRememberMeOn() {
        return sharedPreferences.getBoolean(REMEMBER_ME, true);
    }

    public boolean isFingerprint() {
            return sharedPreferences.getBoolean(FINGERPRINT, false)
                    && !TextUtils.isEmpty(getRememberedUsername())
                    && !TextUtils.isEmpty(getRememberedPassword());
    }

    public void setFingerprint(boolean bool) {
        setSharedPreference(FINGERPRINT, bool);
    }

    public void setRememberMe(boolean bool) {
        setSharedPreference(REMEMBER_ME, bool);
    }

    public void clearRememberedUsername() {
        sharedPreferences.edit().remove(REMEMBERED_USERNAME_ENCRYPTED).apply();
    }

    public String getRememberedUsername() {
        String encryptedRememberedUsername = sharedPreferences.getString(REMEMBERED_USERNAME_ENCRYPTED, "");

        return keyStoreWrapper.decryptString(encryptedRememberedUsername);
    }

    public void setRememberedUsername(String rememberedUsername) {
        String rememberedUsernameEncrypted = keyStoreWrapper.encryptString(rememberedUsername);

        setSharedPreference(REMEMBERED_USERNAME_ENCRYPTED, rememberedUsernameEncrypted);
    }

    public String getRememberedPassword() {
        String encryptedRememberedUsername = sharedPreferences.getString(REMEMBERED_PASSWORD_ENCRYPTED, "");

        return keyStoreWrapper.decryptString(encryptedRememberedUsername);
    }

    public void setRememberedPassword(String rememberedUsername) {
        String rememberedUsernameEncrypted = keyStoreWrapper.encryptString(rememberedUsername);

        setSharedPreference(REMEMBERED_PASSWORD_ENCRYPTED, rememberedUsernameEncrypted);
    }

    public String getRememberedEntity() {
        String encryptedRememberedEntity = sharedPreferences.getString(REMEMBERED_ENTITY_NUMBER_ENCRYPTED, "");

        return keyStoreWrapper.decryptString(encryptedRememberedEntity);
    }

    public void setRememberedEntity(String rememberedEntity) {
        String rememberedEntityEncrypted = keyStoreWrapper.encryptString(rememberedEntity);

        setSharedPreference(REMEMBERED_ENTITY_NUMBER_ENCRYPTED, rememberedEntityEncrypted);
    }



    @SuppressLint("ApplySharedPref")
    public void clearAll() {
        sharedPreferences.edit().clear().commit();
    }

    public void setAnalytics(boolean value) {
        setSharedPreference(SEND_ANALYTICS, value);
    }

    public boolean isAnalyticsEnabled() {
        return sharedPreferences.getBoolean(SEND_ANALYTICS, true);
    }

    public void setEnableCrashReports(boolean value) {
        setSharedPreference(CRASH_REPORTS, value);
    }

    public boolean isCrashReportsEnabled() {
        return sharedPreferences.getBoolean(CRASH_REPORTS, true);
    }

    public void setSharedStatus(boolean value) {
        setSharedPreference(SHARED_STATUS, value);
    }

    public boolean isSharedStatus() {
        return sharedPreferences.getBoolean(SHARED_STATUS, true);
    }

    public boolean hasCurrentUserSeenVHCOnboarding() {
        return sharedPreferences.getBoolean(HAS_CURRENT_USER_SEEN_VHC_ONBOARDING, false);
    }

    public boolean hasCurrentUserSeenVHROnboarding() {
        return sharedPreferences.getBoolean(HAS_CURRENT_USER_SEEN_VHR_ONBOARDING, false);
    }

    public boolean hasCurrentUserSeenVNAOnboarding() {
        return sharedPreferences.getBoolean(HAS_CURRENT_USER_SEEN_VNA_ONBOARDING, false);
    }

    public void setCurrentUserHasSeenVNAOnboarding() {
        setSharedPreference(HAS_CURRENT_USER_SEEN_VNA_ONBOARDING, true);
    }

    public String getCurrentEnvironmentPrefix() {
        return sharedPreferences.getString(CURRENT_ENVIRONMENT_PREFIX, EnvironmentPrefix.TEST);
    }

    public void setCurrentEnvironmentPrefix(@EnvironmentPrefix String newEnvPrefix) {
        setSharedPreference(CURRENT_ENVIRONMENT_PREFIX, newEnvPrefix);
    }

    public void setCurrentBaseURL(String baseUrl) {
        setSharedPreference(CURRENT_BASE_URL, baseUrl);
    }

    public String getCurrentBasicAuthToken() {
        return sharedPreferences.getString(CURRENT_BASIC_AUTH_TOKEN, BuildConfig.TEST_BASIC_AUTH_TOKEN);
    }

    public void setCurrentBasicAuthToken(String basicAuthToken) {
        setSharedPreference(CURRENT_BASIC_AUTH_TOKEN, basicAuthToken);
    }

    public void setCurrentUserHasSeenVitalityAge() {
        setSharedPreference(HAS_CURRENT_USER_SEEN_VITALITY_AGE, true);
    }

    public boolean hasCurrentUserSeenVitalityAge() {
        return sharedPreferences.getBoolean(HAS_CURRENT_USER_SEEN_VITALITY_AGE, false);
    }

    public void setCurrentUserHasCompletedVHC() {
        setSharedPreference(HAS_CURRENT_USER_COMPLETED_VHC_BEFORE, true);
    }

    public boolean hasCurrentUserCompletedVHC() {
        return sharedPreferences.getBoolean(HAS_CURRENT_USER_COMPLETED_VHC_BEFORE, false);
    }
    public void setCurrentUserHasCompletedSNV() {
        setSharedPreference(HAS_CURRENT_USER_COMPLETED_SNV_BEFORE, true);
    }
    public boolean hasCurrentUserCompletedSNV() {
        return sharedPreferences.getBoolean(HAS_CURRENT_USER_COMPLETED_SNV_BEFORE, false);
    }


    public void setCurrentUserMembershipPeriod(String membershipStart, String membershipEnd) {
        Set<String> membershipDates = new LinkedHashSet<>(Arrays.asList(membershipStart, membershipEnd));
        setSharedPreference(CURRENT_MEMBERSHIP_PERIOD, membershipDates);
    }

    public Set<String> getCurrentUserMembershipPeriod() {
        LinkedHashSet<String> membershipDates = new LinkedHashSet<>();
        return sharedPreferences.getStringSet(CURRENT_MEMBERSHIP_PERIOD, membershipDates);
    }

    public void setCurrentUserHasSeenMyHealthOnboarding() {
        setSharedPreference(HAS_CURRENT_USER_SEEN_MY_HEALTH_ONBOARDING_SCREEN, true);
    }

    public boolean hasCurrentUserHasSeenMyHealthOnboarding() {
        return sharedPreferences.getBoolean(HAS_CURRENT_USER_SEEN_MY_HEALTH_ONBOARDING_SCREEN, false);
    }


    public boolean hasCurrentUserSeenWellnessDevicesOnboarding() {
        return sharedPreferences.getBoolean(HAS_CURRENT_USER_SEEN_WELLNESS_DEVICES_ONBOARDING, false);
    }

    public void setCurrentUserHasSeenWellnessDevicesOnboarding() {
        setSharedPreference(HAS_CURRENT_USER_SEEN_WELLNESS_DEVICES_ONBOARDING, true);
    }

    public boolean hasCurrentUserHasSeenVitalityStatusOnboarding() {
        return sharedPreferences.getBoolean(HAS_CURRENT_USER_SEEN_VITALITY_STATUS_ONBOARDING, false);
    }

    public void setCurrentUserHasSeenVitalityStatusOnboarding() {
        setSharedPreference(HAS_CURRENT_USER_SEEN_VITALITY_STATUS_ONBOARDING, true);
    }

    public void setFingerprintSecretKey(SecretKey secretKey) {
        getEditor().putString(FINGERPRINT_SECRET_KEY, ObjectSerializerHelper.objectToString(secretKey)).apply();
    }

    public SecretKey getFingerprintSecretKey() {
        String secretKeyStr = sharedPreferences.getString(FINGERPRINT_SECRET_KEY, "");
        SecretKey secretKey = (SecretKey) ObjectSerializerHelper.stringToObject(secretKeyStr);
        return secretKey;
    }

    public int getLatestStatusLevelKey() {
        return sharedPreferences.getInt(LATEST_STATUS_LEVEL, 0);
    }

    public void setLatestStatusLevelKey(int statusLevelKey) {
        setSharedPreference(LATEST_STATUS_LEVEL, statusLevelKey);
    }

    public byte[] getSecretKey() {
        if (!sharedPreferences.contains(ENCRYPTION_KEY)) {
            byte[] key = new byte[64];
            new SecureRandom().nextBytes(key);
            setSharedPreference(ENCRYPTION_KEY, keyStoreWrapper.encryptBytes(key));
            return key;
        }
        String encryptedKey = sharedPreferences.getString(ENCRYPTION_KEY, null);
        return keyStoreWrapper.decrypt(encryptedKey);
    }


}
