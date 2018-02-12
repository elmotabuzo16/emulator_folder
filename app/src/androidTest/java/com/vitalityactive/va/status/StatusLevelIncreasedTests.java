package com.vitalityactive.va.status;

import android.content.res.Resources;
import android.support.annotation.CallSuper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.home.HomeActivity;
import com.vitalityactive.va.testutilities.VitalityActive;
import com.vitalityactive.va.vitalitystatus.VitalityStatusLevelIncreasedActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class StatusLevelIncreasedTests {
    @Rule
    public TestName name = new TestName();
    private Resources resources;

    @Before
    public void setUp() {
        resources = InstrumentationRegistry.getTargetContext().getResources();

        TestHarness.Settings.useMockService = true;
        TestHarness.setup(name.getMethodName())
                .clearEverythingLikeAFreshInstall()
                .withLoggedInUser();
    }

    @After
    @CallSuper
    public void tearDown() {
        TestHarness.tearDown();
    }

    @Test
    public void next_status_footer_is_not_displayed_on_highest_level() {
        enqueueResponseAndShowLevelIncreased("status/platinum_level_status.json");

        onView(withId(R.id.main_recyclerview)).perform(scrollToPosition(2));

        VitalityActive.Matcher.checkViewIsNotDisplayed(withId(R.id.next_status_container));
    }

    @Test
    public void next_status_footer_is_displayed_when_not_on_highest_level() {
        enqueueResponseAndShowLevelIncreased("status/gold_level_status.json");

        onView(withId(R.id.main_recyclerview)).perform(scrollToPosition(2));

        onView(withId(R.id.next_status_container)).check(matches(isDisplayed()));
    }

    @Test
    public void header_contains_correct_content() {
        enqueueResponseAndShowLevelIncreased("status/gold_level_status.json");

        String expectedString = String.format(resources.getString(R.string.Status_increased_status_message_811), resources.getString(R.string.Status_level_gold_title_836));
        onView(withText(expectedString)).check(matches(isDisplayed()));

        expectedString = String.format(resources.getString(R.string.Status_increased_status_title_810), resources.getString(R.string.Status_level_gold_title_836));
        onView(withText(expectedString)).check(matches(isDisplayed()));
    }

    private void enqueueResponseAndShowLevelIncreased(String file) {
        MockNetworkHandler.enqueueResponseFromFile(200, file);

        TestHarness.launchActivity(HomeActivity.class);
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        TestHarness.launchActivity(VitalityStatusLevelIncreasedActivity.class);
    }
}
