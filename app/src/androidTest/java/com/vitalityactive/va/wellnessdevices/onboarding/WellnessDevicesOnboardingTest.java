package com.vitalityactive.va.wellnessdevices.onboarding;

import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.R;
import com.vitalityactive.va.testutilities.VitalityActive;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class WellnessDevicesOnboardingTest {
    @Test
    public void wd_onboarding_content_is_shown_when_navigation_from_home_screen() {
        VitalityActive.Navigate.launchWellnessDeviceOnboardingActivity();

            onView(withId(R.id.tv_wd_title)).check(matches(withText(R.string.WDA_onboarding_heading_416)));
            onView(withId(R.id.tv_wd_onboarding_link_title)).check(matches(withText(R.string.WDA_onboarding_item1_heading_417)));
            onView(withId(R.id.tv_wd_onboarding_link_text)).check(matches(withText(R.string.WDA_onboarding_item1_418)));
            onView(withId(R.id.tv_wd_onboarding_track_title)).check(matches(withText(R.string.WDA_onboarding_item2_heading_419)));
            onView(withId(R.id.tv_wd_onboarding_track_text)).check(matches(withText(R.string.WDA_onboarding_item2_420)));
            onView(withId(R.id.tv_wd_onboarding_earn_title)).check(matches(withText(R.string.WDA_onboarding_item3_heading_421)));
            onView(withId(R.id.tv_wd_onboarding_earn_text)).check(matches(withText(R.string.WDA_onboarding_item3_422)));
        }

    @Test
    @Ignore
    // Ignored because screen is now launching from stub
    public void pressing_back_from_wd_onboarding_navigates_back_to_home_screen() {
        VitalityActive.Navigate.launchWellnessDeviceOnboardingActivity();

        Espresso.pressBack();

        VitalityActive.Assert.onHomeScreen();
    }

//        @Test
//        public void pressing_get_started_navigates_to_terms_and_conditions()
//        {
//            VitalityActive.Navigate.fromHomeScreenToProbabilisticOnboarding();
//
//            onView(withId(R.id.btn_wd_got_it)).perform(scrollTo());
//            onView(withId(R.id.btn_wd_got_it)).perform(click());
//
//            VitalityActive.Assert.onWellnessDevicesLandingScreen(); // TODO implement
//        }

    // TODO implement test for button "I need a device or app"

}
