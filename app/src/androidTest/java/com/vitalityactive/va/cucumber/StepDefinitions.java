package com.vitalityactive.va.cucumber;

import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;

import org.junit.Test;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import static junit.framework.Assert.assertEquals;

public class StepDefinitions {
    @Before
    public void setUp(Scenario scenario) throws Exception {
        TestHarness.startVitalityActiveAsAFreshInstall();
        TestHarness.setup(scenario.getId()).withLoggedInUser();
    }

    @After
    public void tearDown() {
        TestHarness.tearDown();
    }

    @Test
    public void emptyTestToAvoidJUnitRunnerIssues() {
    }

    @Given("^I have launched the VA app$")
    public void launchApp() {
        // no-op, done when screen is shown
    }


//    @Given("^I have clicked on (.+)$")
//    public void clickedOnButtonWithText(String buttonText) {
//        clickOnButtonWithText(buttonText);
//    }


//    @When("^I tap (.+)$")
//    public void clickOnButtonWithText(final String text) {
//        TestHarness.currentScreen = TestHarness.currentScreen.clickOnButtonWithText(text);
//    }


    @Then("^I should see a message about Forgot Password$")
    public void checkThatThePromptAboutForgotPasswordIsDisplayed() {
        TestHarness.delay();
        TestHarness.currentScreen.checkDialogWithTextIsDisplayed(R.string.login_forgot_password_button_title_22);
    }

    @Then("^The app should resubmit the registration details$")
    public void checkResubmittedRegistrationDetails() {
        int requestCount = MockNetworkHandler.getRequestCount();
        assertEquals(2, requestCount);
    }

//    @Then("^no error dialog is displayed$")
//    public void noErrorDialogIsDisplayed() {
//        TestHarness.currentScreen.checkErrorOccurredDialogIsNotDisplayed();
////        currentScreen.checkErrorOccurredDialogIsNotDisplayed();
//    }


//    private static class ScreenMapping {
//        public final Class activity;
//        public final Class screen;
//
//        public ScreenMapping(Class activity, Class screen) {
//            this.activity = activity;
//            this.screen = screen;
//        }
//    }
}
