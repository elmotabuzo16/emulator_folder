package com.vitalityactive.va.settings;

import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.screens.BaseScreen;
import com.vitalityactive.va.cucumber.screens.HomeScreen;
import com.vitalityactive.va.cucumber.screens.LoginScreen;
import com.vitalityactive.va.cucumber.screens.SplashScreen;
import com.vitalityactive.va.cucumber.screens.UserPreferencesScreen;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.vhr.BaseTests;

import org.junit.Assume;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.cucumber.screens.BaseScreen.isOnScreen;
import static com.vitalityactive.va.cucumber.utils.TestHarness.shortDelay;

public class LogoutFunctionalityTest extends BaseTests {

    public static final String USERNAME = "Donette_Cason@SITtest.com";
    public static final String PASSWORD = "TestPass123";

    @Override
    public void setUp() throws IllegalAccessException, InstantiationException {
        super.setUp();

        Assume.assumeFalse(BuildConfig.FLAVOR.equals("dev") && BuildConfig.DEBUG);
    }

    @Test
    public void logout_functionality() throws Exception {
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        toHomeScreen();
        onView(withId(R.id.activity_home)).perform(open());
        onView(withId(R.id.navigation_view)).perform(navigateTo(R.id.navigation_item_settings));
        onView(withId(R.id.settings_logout)).perform(click());
        shortDelay();
        onView(withText(R.string.Settings_logout_title_908)).perform(click());
        shortDelay();
        isOnScreen(LoginScreen.class);

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
