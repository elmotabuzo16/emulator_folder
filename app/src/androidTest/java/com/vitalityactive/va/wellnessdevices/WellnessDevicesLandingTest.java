package com.vitalityactive.va.wellnessdevices;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.R;
import com.vitalityactive.va.testutilities.VitalityActive;
import com.vitalityactive.va.wellnessdevices.landing.WellnessDevicesLandingActivity;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.testutilities.VitalityActive.Matcher.childAtPosition;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class WellnessDevicesLandingTest {
    @Rule
    public ActivityTestRule<WellnessDevicesLandingActivity> mActivityTestRule = new ActivityTestRule<>(WellnessDevicesLandingActivity.class);

    @Test
    @Ignore
    public void wd_landingscreen_is_shown_correctly() {
        VitalityActive.Navigate.launchWellnessDeviceOnboardingActivity();
        VitalityActive.Navigate.launchLandingActivityFromOnboarding();

        VitalityActive.Assert.onWellnessDevicesLanding();
    }

    @Test
    @Ignore
    public void click_learn_more_works() {
        onView(withId(R.id.main_recyclerview)).perform(scrollToPosition(2));
        onView(allOf(withId(R.id.recycler_view), childAtPosition(withId(R.id.container), 1)));
        onView(allOf(withId(R.id.label), withText(R.string.learn_more_button_title_104))).perform(click());

        VitalityActive.Assert.onWellnessDevicesLearnMore();
    }

    // TODO:
    // back button -> home screen
    // linked + available to link -> contains full list of devices
    // if all devices linked -> don't show 'available to link'
    // if 0 devices linked -> replace'linked' with 'get_linking'
}
