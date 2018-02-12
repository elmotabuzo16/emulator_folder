package com.vitalityactive.va.eventsfeed.views;

import android.support.annotation.CallSuper;
import android.support.test.espresso.NoMatchingViewException;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.screens.BaseScreen;
import com.vitalityactive.va.cucumber.screens.HomeScreen;
import com.vitalityactive.va.cucumber.screens.LoginScreen;
import com.vitalityactive.va.cucumber.screens.SplashScreen;
import com.vitalityactive.va.cucumber.screens.UserPreferencesScreen;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.myhealth.BaseTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.cucumber.utils.TestHarness.shortDelay;

/**
 * Created by jayellos on 1/8/18.
 */

public class FilterEventsByCategoryTest extends BaseTests{

//        public static final String USERNAME = "test--Donette_Cason@SITtest.com";
//        public static final String PASSWORD = "TestPass123";

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
    public void should_show_events_feed_categories() throws Exception {
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        toHomeScreen();
        onView(withId(R.id.activity_home)).perform(open());

        onView(withId(R.id.navigation_view)).perform(navigateTo(R.id.navigation_item_profile));
        shortDelay();
        onView(withId(R.id.events_feed)).perform(click());
        TestHarness.waitForLoadingIndicatorToNotDisplay();

        onView(withId(R.id.toolbar_title)).perform(click());
    }

    @Test
    public void should_show_filtered_events_feed() throws Exception {
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        toHomeScreen();
        onView(withId(R.id.activity_home)).perform(open());

        onView(withId(R.id.navigation_view)).perform(navigateTo(R.id.navigation_item_profile));
        shortDelay();
        onView(withId(R.id.events_feed)).perform(click());
        TestHarness.waitForLoadingIndicatorToNotDisplay();

        onView(withId(R.id.toolbar_title)).perform(click());
        shortDelay();
        onView(withText("Assessments")).perform(click());
        shortDelay();

        onView(withText("OK")).perform(click());
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        shortDelay();
    }

    protected static HomeScreen toHomeScreen() throws InstantiationException, IllegalAccessException {
        TestHarness.startVitalityActiveWithClearedData();
        TestHarness.currentScreen = TestHarness.isOnScreen(LoginScreen.class)
                .skipOnboardingScreens()
                .enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickLogin();

        try {
            onView(withText("USE PASSWORD")).check(matches(isDisplayed()));
            onView(withText("USE PASSWORD")).perform(click());
        } catch (NoMatchingViewException e) {}

        TestHarness.waitForScreen(SplashScreen.class)
                .clickOnBackgroundAndIgnoreNextScreen();

        BaseScreen baseScreen = TestHarness.waitForAnyScreen(HomeScreen.class, UserPreferencesScreen.class);
        if (baseScreen.isOfType(UserPreferencesScreen.class)) {
            TestHarness.currentScreen.is(UserPreferencesScreen.class).clickNext();
        }
        return TestHarness.waitForScreen(HomeScreen.class);
    }
}
