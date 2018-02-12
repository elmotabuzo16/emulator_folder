package com.vitalityactive.va.myhealth;

import android.support.annotation.CallSuper;
import android.support.test.InstrumentationRegistry;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.screens.BaseScreen;
import com.vitalityactive.va.cucumber.screens.HomeScreen;
import com.vitalityactive.va.cucumber.screens.LoginScreen;
import com.vitalityactive.va.cucumber.screens.SplashScreen;
import com.vitalityactive.va.cucumber.screens.UserPreferencesScreen;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.login.LoginActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.vitalityactive.va.cucumber.utils.TestHarness.shortDelay;

public class BaseTests {

    public static final String USERNAME = "Leyla_Richard@SITtest.com";
    public static final String PASSWORD = "TestPass123";

    @Rule
    public TestName name = new TestName();

    protected static HomeScreen toHomeScreen() throws InstantiationException, IllegalAccessException {
        TestHarness.startVitalityActiveWithClearedData();
        TestHarness.currentScreen = TestHarness.isOnScreen(LoginScreen.class)
                .skipOnboardingScreens()
                .enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickLogin();
        TestHarness.waitForScreen(SplashScreen.class)
                .clickOnBackgroundAndIgnoreNextScreen();
        BaseScreen baseScreen = TestHarness.waitForAnyScreen(HomeScreen.class, UserPreferencesScreen.class);

        if (baseScreen.isOfType(UserPreferencesScreen.class)) {
            TestHarness.currentScreen.is(UserPreferencesScreen.class).clickNext();
        }
        return TestHarness.waitForScreen(HomeScreen.class);
    }

    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }

    protected static String getString(int contentResourceId) {
        return InstrumentationRegistry.getTargetContext().getResources().getString(contentResourceId);
    }

    @Before
    @CallSuper
    public void setUp() throws IllegalAccessException, InstantiationException, IOException {
        TestHarness.Settings.useMockService = true;
        TestHarness.setup(name.getMethodName())
                .clearEverythingLikeAFreshInstall()
                .withLoggedInUser();
        setupMyHealthAndLaunchLandingActivity();
    }

    @After
    @CallSuper
    public void tearDown() {
        TestHarness.tearDown();
    }

    protected void setupMyHealthAndLaunchLandingActivity() {
        TestHarness.launchActivity(LoginActivity.class);//
    }

    public void loadVitalityAgeProfileScreen() throws Exception {
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        toHomeScreen();
        onView(withId(R.id.activity_home)).perform(open());
        onView(withId(R.id.navigation_view)).perform(navigateTo(R.id.navigation_item_my_health));
        shortDelay();
        onView(withId(R.id.my_health_onboarding_button)).perform(click());
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        shortDelay(10000);
        onView(withId(R.id.myhealth_landing_card)).perform(click());

    }

}
