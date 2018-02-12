package com.vitalityactive.va.rateus;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.screens.BaseScreen;
import com.vitalityactive.va.cucumber.screens.HomeScreen;
import com.vitalityactive.va.cucumber.screens.LoginScreen;
import com.vitalityactive.va.cucumber.screens.SplashScreen;
import com.vitalityactive.va.cucumber.screens.UserPreferencesScreen;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.home.HomeActivity;
import com.vitalityactive.va.vhr.BaseTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class RateUsActivityTest extends BaseTests {

    public static final String USERNAME = "Donette_Cason@SITtest.com";
    public static final String PASSWORD = "TestPass123";

    @Rule
    public ActivityTestRule<HomeActivity> intentsTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Before
    public void initializeIntent() {
        Intents.init();
    }


    @Test
    public void show_rating_page_and_calling_google_play() throws Exception {
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        toHomeScreen();
        onView(withId(R.id.activity_home)).perform(open());
        onView(withId(R.id.navigation_view)).perform(navigateTo(R.id.navigation_item_rate_us));
        intending(toPackage("com.android.vending"));
    }

    @After
    public void intentRelease(){
        Intents.release();
    }


    private static HomeScreen toHomeScreen() throws InstantiationException, IllegalAccessException {
        TestHarness.startVitalityActiveWithClearedData();
        TestHarness.currentScreen = TestHarness.isOnScreen(LoginScreen.class)
                .skipOnboardingScreens()
                .enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickLogin();
        TestHarness.waitForScreen(SplashScreen.class)
                .clickOnBackgroundAndIgnoreNextScreen();
        BaseScreen baseScreen = TestHarness.waitForAnyScreen(HomeScreen.class, UserPreferencesScreen.class);

        if (baseScreen.isOfType(UserPreferencesScreen.class)){
            TestHarness.currentScreen.is(UserPreferencesScreen.class).clickNext();
        }
        return TestHarness.waitForScreen(HomeScreen.class);
    }
}
