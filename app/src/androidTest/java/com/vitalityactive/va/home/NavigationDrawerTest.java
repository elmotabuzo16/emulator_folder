package com.vitalityactive.va.home;

import android.view.Gravity;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.vitalityactive.va.testutilities.VitalityActive.Assert.onScreen;

@UITestWithMockedNetwork
public class NavigationDrawerTest {

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
    public void when_profile_image_is_tapped_then_user_is_navigated_to_profile_screen() {
        setupDataAndLaunchHomeActivity();

        onView(withId(R.id.activity_home))
                .check(matches(isClosed(Gravity.START)))
                .perform(open());

        onView(withId(R.id.profile_image_container))
                .perform(click());

        onScreen(R.id.profile_root);

        onView(withId(R.id.activity_home))
                .check(matches(isClosed(Gravity.START)));

    }

    private void setupDataAndLaunchHomeActivity() {
        MockNetworkHandler.enqueueResponseWithoutBody(500);
        MockNetworkHandler.enqueueResponseWithoutBody(500);

        TestHarness.launchActivity(HomeActivity.class);
        TestHarness.waitForLoadingIndicatorToNotDisplay();
    }

}
