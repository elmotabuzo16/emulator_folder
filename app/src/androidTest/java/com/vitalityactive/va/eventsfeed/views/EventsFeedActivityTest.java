package com.vitalityactive.va.eventsfeed.views;

import android.support.annotation.CallSuper;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.myhealth.BaseTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.vitalityactive.va.cucumber.utils.TestHarness.shortDelay;

/**
 * Created by jayellos on 11/24/17.
 */
public class EventsFeedActivityTest extends BaseTests {
    //    public static final String USERNAME = "Donette_Cason@SITtest.com";
    //    public static final String PASSWORD = "TestPass123";

    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() {
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
    public void should_show_vitality_age_card_on_my_health() throws Exception {
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        toHomeScreen();
        onView(withId(R.id.activity_home)).perform(open());


        onView(withId(R.id.navigation_view)).perform(navigateTo(R.id.navigation_item_profile));
        shortDelay();
        onView(withId(R.id.events_feed)).perform(click());
        TestHarness.waitForLoadingIndicatorToNotDisplay();

//
//        onView(withId(R.id.vitality_age_title)).check(matches(isDisplayed()));
//        onView(withId(R.id.vitality_age_value)).check(matches(isDisplayed()));
//        onView(withId(R.id.vitality_age_summary)).check(matches(isDisplayed()));
    }

}