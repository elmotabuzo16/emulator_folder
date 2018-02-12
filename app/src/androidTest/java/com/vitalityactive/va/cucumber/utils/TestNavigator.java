package com.vitalityactive.va.cucumber.utils;

import com.vitalityactive.va.cucumber.TestData;
import com.vitalityactive.va.cucumber.screens.BaseScreen;
import com.vitalityactive.va.cucumber.screens.ForgotPasswordScreen;
import com.vitalityactive.va.cucumber.screens.HomeScreen;
import com.vitalityactive.va.cucumber.screens.LoginScreen;
import com.vitalityactive.va.cucumber.screens.SplashScreen;
import com.vitalityactive.va.cucumber.screens.TermsAndConditionsScreen;
import com.vitalityactive.va.cucumber.screens.UserPreferencesScreen;
import com.vitalityactive.va.cucumber.testData.TestDataForFirstTimePreferences;
import com.vitalityactive.va.cucumber.testData.TestDataForNSD;

public class TestNavigator {
    public static TermsAndConditionsScreen loginWithDefaultCredentialsForTheFirstTimeAndClickOnSplashScreen() throws IllegalAccessException, InstantiationException {
        TestHarness.currentScreen = TestHarness.isOnScreen(LoginScreen.class)
                .skipOnboardingScreens()
                .enterUsername(TestData.USERNAME)
                .enterPassword(TestData.PASSWORD)
                .clickLogin()
                .is(SplashScreen.class)
                .clickOnBackground();
        return TestHarness.currentScreen.is(TermsAndConditionsScreen.class);
    }

    public static UserPreferencesScreen toUserPreferenceScreen() throws InstantiationException, IllegalAccessException {
        loginWithDefaultCredentialsForTheFirstTimeAndClickOnSplashScreen();

        MockNetworkHandler.enqueueResponseWithEmptyJsonObject(201);
        TestHarness.currentScreen = TestHarness.currentScreen
                .is(TermsAndConditionsScreen.class)
                .clickOnMoreAndThenOnAgreeToAcceptTerms();

        return TestHarness.currentScreen.is(UserPreferencesScreen.class);
    }

    public static UserPreferencesScreen toUserPreferenceScreenFromLogin() throws InstantiationException, IllegalAccessException {
        TestHarness.currentScreen = TestHarness.isOnScreen(LoginScreen.class)
                .skipOnboardingScreens()
                .enterUsername(TestDataForFirstTimePreferences.USERNAME)
                .enterPassword(TestDataForFirstTimePreferences.PASSWORD)
                .clickLogin()
                .is(SplashScreen.class)
                .clickOnBackground();
        return TestHarness.currentScreen.is(UserPreferencesScreen.class);
    }

    public static ForgotPasswordScreen toForgotPasswordScreen() throws InstantiationException, IllegalAccessException {
        TestHarness.currentScreen = TestHarness.isOnScreen(LoginScreen.class)
                .skipOnboardingScreens()
                .clickForgetPassword();
        return TestHarness.currentScreen.is(ForgotPasswordScreen.class);
    }

    public static HomeScreen toHomeScreen() throws InstantiationException, IllegalAccessException {
        TestHarness.currentScreen = TestHarness.isOnScreen(LoginScreen.class)
                .skipOnboardingScreens()
                // todo: need to login with a scenario specific user
                .enterUsername(TestDataForNSD.USERNAME)
                .enterPassword("TestPass123")
                .clickLogin();
        TestHarness.waitForScreen(SplashScreen.class)
                .clickOnBackgroundAndIgnoreNextScreen();
        BaseScreen baseScreen = TestHarness.waitForAnyScreen(HomeScreen.class, UserPreferencesScreen.class);

        if (baseScreen.isOfType(UserPreferencesScreen.class)) {
            TestHarness.currentScreen.is(UserPreferencesScreen.class).clickNext();
        }
        return TestHarness.waitForScreen(HomeScreen.class);
    }
}
