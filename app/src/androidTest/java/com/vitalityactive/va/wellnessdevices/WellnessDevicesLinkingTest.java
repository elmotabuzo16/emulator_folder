package com.vitalityactive.va.wellnessdevices;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.testutilities.VitalityActive;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;
import com.vitalityactive.va.wellnessdevices.landing.service.GetFullListResponse;
import com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesDetailsActivity;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class WellnessDevicesLinkingTest extends RepositoryTestBase {
    @Rule
    public ActivityTestRule<WellnessDevicesDetailsActivity> mActivityTestRule = new ActivityTestRule<>(WellnessDevicesDetailsActivity.class);

    @Test
    @Ignore
    public void wd_linkingscreen_is_shown_correctly() throws IOException {
        VitalityActive.Navigate.launchWellnessDeviceOnboardingActivity();
        VitalityActive.Navigate.launchLandingActivityFromOnboarding();

        GetFullListResponse.Partner partner = getResponse(GetFullListResponse.Partner.class, "wellnessdevices/Partner.json");

        VitalityActive.Navigate.launchLinkingActivityFromLanding(new PartnerDto(partner));

        VitalityActive.Assert.onWellnessDevicesLinking();
    }
}
