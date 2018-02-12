package com.vitalityactive.va.wellnessdevices;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.testutilities.VitalityActive;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class WellnessDevicesLearnMoreTest {
    @Rule
    public ActivityTestRule<WellnessDevicesLearnMoreActivity> mActivityTestRule = new ActivityTestRule<>(WellnessDevicesLearnMoreActivity.class);
    RecyclerView settingsContainer;

    @Test
    @Ignore
    public void wd_learnmore_content_is_shown() {
        // Fixme actually this tet was moved to LandingPageTest.
        VitalityActive.Navigate.launchWellnessDeviceOnboardingActivity();
        VitalityActive.Navigate.launchLandingActivityFromOnboarding();
        VitalityActive.Navigate.launchWellnessDevicesLearnMoreActivityFromLanding();

        VitalityActive.Assert.onWellnessDevicesLearnMore();
    }

    // TODO:
    // back button -> landing screen
    // back arrow -> landing screen

}