package com.vitalityactive.va.screening;

import android.support.annotation.CallSuper;

import com.vitalityactive.va.cucumber.screens.BaseScreen;
import com.vitalityactive.va.cucumber.screens.HomeScreen;
import com.vitalityactive.va.cucumber.screens.LoginScreen;
import com.vitalityactive.va.cucumber.screens.SplashScreen;
import com.vitalityactive.va.cucumber.screens.UserPreferencesScreen;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.snv.learnmore.ScreeningsLearnMoreActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by stephen.rey.w.avila on 12/8/2017.
 */

public class BaseTests {
    public static final String USERNAME = "Danika_Brinkman@SITtest.com";
    public static final String PASSWORD = "TestPass123";

//    @Rule
//    public TestName name = new TestName();
//
//    @Before
//    @CallSuper
//    public void setUp() throws IllegalAccessException, InstantiationException, IOException {
//        TestHarness.Settings.useMockService = true;
//        TestHarness.setup(name.getMethodName())
//                .clearEverythingLikeAFreshInstall()
//                .withLoggedInUser();
//      //  setupMyHealthAndLaunchLandingActivity();
//    }
//
//    @After
//    @CallSuper
//    public void tearDown() {
//        TestHarness.tearDown();
//    }


//    protected void setupMyHealthAndLaunchLandingActivity() {
//        TestHarness.launchActivity(LoginActivity.class);//
//    }

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

        if (baseScreen.isOfType(UserPreferencesScreen.class)){
            TestHarness.currentScreen.is(UserPreferencesScreen.class).clickNext();
        }
        return TestHarness.waitForScreen(HomeScreen.class);
    }


    protected void setupSnvLearnMoreDataAndLaunchLandingActivity() {
        setupSnvlearnMoreDataAndLaunchLandingActivity("snv/screening.json");

    }

    protected void setupSnvlearnMoreDataAndLaunchLandingActivity(String file) {
        MockNetworkHandler.enqueueResponseFromFile(200, file);
        TestHarness.launchActivity(ScreeningsLearnMoreActivity.class);
    }

//    protected void clickOnLearnButton() {
//        TestHarness.waitForLoadingIndicatorToNotDisplay();
//        onView(VitalityActive.Matcher.firstMatched(withText(R.string.learn_more_button_title_104))).perform(click());
//    }
}