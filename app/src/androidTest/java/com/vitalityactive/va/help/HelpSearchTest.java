package com.vitalityactive.va.help;

import android.view.Gravity;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.screens.BaseScreen;
import com.vitalityactive.va.cucumber.screens.HelpScreen;
import com.vitalityactive.va.cucumber.screens.HomeScreen;
import com.vitalityactive.va.cucumber.screens.LoginScreen;
import com.vitalityactive.va.cucumber.screens.SplashScreen;
import com.vitalityactive.va.cucumber.screens.UserPreferencesScreen;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.testutilities.matchers.DrawableMatcher;
import com.vitalityactive.va.vhr.BaseTests;

import org.hamcrest.Matcher;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.vitalityactive.va.cucumber.utils.TestHarness.shortDelay;

/**
 * Created by christian.j.p.capin on 12/06/2017.
 */
public class HelpSearchTest extends BaseTests {

    public static final String USERNAME = "Danika_Brinkman@SITtest.com";
    public static final String PASSWORD = "TestPass123";

    public void launchingActivity() throws Exception {
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        toHomeScreen();
        onView(withId(R.id.activity_home)).perform(open());
        onView(withId(R.id.navigation_view)).perform(navigateTo(R.id.navigation_item_help));
        shortDelay();
    }

    @Test
    public void show_help_screen() throws Exception{
        launchingActivity();
        TestHarness.isOnScreen(HelpScreen.class);

        onView(withId(R.id.activity_home))
                .check(matches(isClosed(Gravity.START)))
                .perform(open());

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
            TestHarness.currentScreen.is(UserPreferencesScreen.class);
        }
        return TestHarness.waitForScreen(HomeScreen.class);
    }

    public static Matcher<View> withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }

    public static Matcher<View> noDrawable() {
        return new DrawableMatcher(-1);
    }
}
