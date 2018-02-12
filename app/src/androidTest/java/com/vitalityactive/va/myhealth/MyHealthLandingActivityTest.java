package com.vitalityactive.va.myhealth;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.utils.TestHarness;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.cucumber.utils.TestHarness.shortDelay;

public class MyHealthLandingActivityTest extends BaseTests {

    @Test
    public void should_show_vitality_age_card_on_my_health() throws Exception {
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        toHomeScreen();
        onView(withId(R.id.activity_home)).perform(open());
        onView(withId(R.id.navigation_view)).perform(navigateTo(R.id.navigation_item_my_health));
        shortDelay();
        onView(withId(R.id.my_health_onboarding_button)).perform(click());
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        onView(withId(R.id.vitality_age_title)).check(matches(isDisplayed()));
        onView(withId(R.id.vitality_age_value)).check(matches(isDisplayed()));
        onView(withId(R.id.vitality_age_summary)).check(matches(isDisplayed()));
    }

    @Test
    public void should_show_myhealth_onboarding_screen() throws Exception {
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        toHomeScreen();
        onView(withId(R.id.activity_home)).perform(open());
        onView(withId(R.id.navigation_view)).perform(navigateTo(R.id.navigation_item_my_health));
        shortDelay();
        onView(withId(R.id.my_health_onboarding)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_onboarding_section1_title_628)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_onboarding_section1_description_629)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_onboarding_section2_title_632)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_onboarding_section2_description_633)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_onboarding_section3_title_634)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_onboarding_section3_description_635)).check(matches(isDisplayed()));
        onView(withId(R.id.my_health_onboarding_button)).check(matches(isDisplayed()));
    }

}
