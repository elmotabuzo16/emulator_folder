package com.vitalityactive.va.snv.onboarding;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.snv.onboarding.ScreeningsAndVaccinationsOnboardingActivity;
import com.vitalityactive.va.testutilities.VitalityActive;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by kerry.e.lawagan on 11/30/2017.
 */

public class ScreeningsAndVaccinationsOnboardingActivityTests {
    @Rule
    public TestName testName;

    @Before
    public void setUp() {
        testName = new TestName();
        TestHarness.Settings.useMockService = true;
        TestHarness.setup(testName.getMethodName())
                .clearEverythingLikeAFreshInstall()
                .withLoggedInUser();
    }

    @After
    public void tearDown() {
        TestHarness.tearDown();
    }

    private void loadScreeningsAndVaccinationsActivity() {
        MockNetworkHandler.enqueueResponseFromFile(200, "snv/get_potential_points_and_events_completed_points.json");
        TestHarness.launchActivity(ScreeningsAndVaccinationsOnboardingActivity.class);

    }

    @Test
    public void can_load_landing_activity() {
        loadScreeningsAndVaccinationsActivity();
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        onView(VitalityActive.Matcher.firstMatched(withId(R.id.health_actions_screenings_container))).check(matches(isDisplayed()));
        onView(VitalityActive.Matcher.firstMatched(withId(R.id.health_actions_vaccinations_container))).check(matches(isDisplayed()));
    }

    @Test
    public void transition_to_health_action_screening() {
        loadScreeningsAndVaccinationsActivity();
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        onView(VitalityActive.Matcher.firstMatched(withId(R.id.health_actions_screenings_container))).perform(click());
        onView(withId(R.id.sv_activity_health_actions)).check(matches(isDisplayed()));
        onView(withText(R.string.SV_screenings_detail_message_1014)).check(matches(isDisplayed()));
    }

    @Test
    public void transition_to_health_action_vaccination() {
        loadScreeningsAndVaccinationsActivity();
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        onView(VitalityActive.Matcher.firstMatched(withId(R.id.health_actions_vaccinations_container))).perform(click());
        onView(withId(R.id.sv_activity_health_actions)).check(matches(isDisplayed()));
        onView(withText(R.string.SV_vaccinations_detail_message_1020)).check(matches(isDisplayed()));
    }
}
