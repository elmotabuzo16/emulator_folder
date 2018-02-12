package com.vitalityactive.va.wellnessdevices;


import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork;
import com.vitalityactive.va.wellnessdevices.landing.WellnessDevicesLandingActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.testutilities.VitalityActive.Matcher.withRecyclerView;

@UITestWithMockedNetwork
public class WellnessDevicesSyncStatusTests {

    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() throws IllegalAccessException, InstantiationException {
        TestHarness.Settings.useMockService = true;
        TestHarness.setup(name.getMethodName())
                .clearEverythingLikeAFreshInstall()
                .withLoggedInUser();
    }

    @After
    public void tearDown() {
        TestHarness.tearDown();
    }

    @Test
    public void correct_device_sync_status_is_shown_on_landing_screen() {
        setupDataAndLaunchLandingActivity();

        onView(withRecyclerView(R.id.main_recyclerview).atPositionOnView(0, -1))
                .check(matches(hasDescendant(withText(R.string.WDA_device_detail_not_synced_493))));
    }

    @Test
    public void correct_device_sync_status_is_shown_on_details_screen() {
        setupDataAndLaunchLandingActivity();

        onView(withRecyclerView(R.id.main_recyclerview).atPositionOnView(0, -1))
                .check(matches(hasDescendant(withText(R.string.WDA_device_detail_not_synced_493))))
                .perform(click());

        onView(withText(R.string.WDA_device_detail_not_synced_493))
                .check(matches(isDisplayed()));
    }

    private void setupDataAndLaunchLandingActivity() {
        MockNetworkHandler.enqueueResponseFromFile(200, "wellnessdevices/wda_activity_mapping.json");
        MockNetworkHandler.enqueueResponseFromFile(200, "wellnessdevices/potential_points.json");
        MockNetworkHandler.enqueueResponseFromFile(200, "wellnessdevices/devices.json");

        TestHarness.launchActivity(WellnessDevicesLandingActivity.class);
        TestHarness.waitForLoadingIndicatorToNotDisplay();

    }

}
