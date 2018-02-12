package com.vitalityactive.va.userpreferences;

import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.MockJUnitRunner;
import com.vitalityactive.va.dependencyinjection.DefaultModule;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@RepositoryTests
public class DeviceSpecificPreferencesTest {
    private DeviceSpecificPreferences deviceSpecificPreferences;

    @Before
    public void setUp() {
        final DefaultModule defaultModule = new DefaultModule(MockJUnitRunner.getInstance().getApplication());
        deviceSpecificPreferences = defaultModule.provideDeviceSpecificPreferences();
    }

    @Test
    public void remember_me_preferences_persists() throws Exception {
        deviceSpecificPreferences.setRememberMe(true);
        Assert.assertTrue(deviceSpecificPreferences.isRememberMeOn());

        deviceSpecificPreferences.setRememberMe(false);
        Assert.assertFalse(deviceSpecificPreferences.isRememberMeOn());
    }

    @Test
    public void current_username_persists() throws Exception {
        deviceSpecificPreferences.setRememberedUsername("Bob");
        Assert.assertEquals("Bob", deviceSpecificPreferences.getRememberedUsername());
    }

    @Test
    public void analytics_preferences_persists() throws Exception {
        deviceSpecificPreferences.setAnalytics(true);
        Assert.assertTrue(deviceSpecificPreferences.isAnalyticsEnabled());
    }

    @Test
    public void crash_reports_persists() throws Exception {
        deviceSpecificPreferences.setEnableCrashReports(true);
        Assert.assertTrue(deviceSpecificPreferences.isCrashReportsEnabled());
    }

    @Test
    public void has_user_seen_vhc_onboarding_persists() {
        deviceSpecificPreferences.setCurrentUserHasSeenVHCOnboarding();
        Assert.assertTrue(deviceSpecificPreferences.hasCurrentUserSeenVHCOnboarding());
    }

    @Test
    public void has_user_seen_vhr_onboarding_persists() {
        deviceSpecificPreferences.setCurrentUserHasSeenVHROnboarding();
        Assert.assertTrue(deviceSpecificPreferences.hasCurrentUserSeenVHROnboarding());
    }
}