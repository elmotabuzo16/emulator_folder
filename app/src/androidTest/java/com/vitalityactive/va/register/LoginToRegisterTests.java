package com.vitalityactive.va.register;

import com.vitalityactive.va.cucumber.screens.LoginScreen;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

@UITestWithMockedNetwork
public class LoginToRegisterTests {
    private static final String VALID_USERNAME = "random@glucode.com";
    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() {
        TestHarness.Settings.useMockService = true;
        TestHarness.setup(testName.getMethodName())
                .clearEverythingLikeAFreshInstall();
        TestHarness.startVitalityActiveAsAFreshInstall();
    }

    @Test
    public void login_to_register() throws InstantiationException, IllegalAccessException {
        MockNetworkHandler.enqueueResponseFromFile(400, "login/Login_invalid_details.json");
        TestHarness.isOnScreen(LoginScreen.class)
                .skipOnboardingScreens()
                .enterUsername(VALID_USERNAME)
                .clickRegister()
                .assertEnteredUsername(VALID_USERNAME);
    }
}
