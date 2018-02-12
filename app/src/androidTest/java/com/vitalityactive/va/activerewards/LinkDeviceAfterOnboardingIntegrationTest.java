package com.vitalityactive.va.activerewards;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.testutilities.BaseNavigationFlowTests;
import com.vitalityactive.va.testutilities.VitalityActive;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LinkDeviceAfterOnboardingIntegrationTest extends BaseNavigationFlowTests {

    @Test
    @Ignore("fails on medically fit terms and conditions acceptance")
    public void active_rewards_link_device_after_onboarding() {
        launchActivity();
        registerAndAcceptApplicationTermsAndConditions();

        VitalityActive.Navigate.fromHomeScreenToProbabilisticOnboarding();
        clickGetStartedOnOnboardingScreen();
        acceptActiveRewardsTermsAndConditions();
        clickGotItOnActivatedScreen();

        linkDevicesAfterActiveRewardsOnboarding();
        VitalityActive.Assert.onLinkWellnessDeviceScreen();
        // TODO: link one of the devices
//        clickNextOnLinkWellnessButtonBar();

        VitalityActive.Assert.onActiveRewardsLanding();
    }
}
