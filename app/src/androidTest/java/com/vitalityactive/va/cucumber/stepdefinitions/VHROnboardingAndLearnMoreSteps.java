package com.vitalityactive.va.cucumber.stepdefinitions;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.utils.ScreenMapping;
import com.vitalityactive.va.cucumber.utils.TestHarness;

import org.junit.Test;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class VHROnboardingAndLearnMoreSteps {
    @Before
    public void setUp(Scenario scenario) throws Exception {
        TestHarness.setup(scenario.getId());
    }

    @After
    public void tearDown() {
        TestHarness.tearDown();
    }

    @Test
    public void emptyTestToAvoidJUnitRunnerIssues() {
    }

    @Given("^I am on the (VHR Onboarding|VHR Learn More|VHR Landing) screen$")
    @SuppressWarnings("unchecked")
    public void iAmOnAScreen(String screenName) throws InstantiationException, IllegalAccessException {
        TestHarness.setup().clearAllData();
        ScreenMapping screenMapping = TestHarness.getScreenMapping(screenName);
        TestHarness.givenIAmOnActivity(screenMapping.activity, screenMapping.screen);
    }

    @Then("^the (VHR Onboarding|VHR Learn More|VHR Landing) screen is loaded$")
    @SuppressWarnings("unchecked")
    public void checkIsOnScreen(String screenName) throws InstantiationException, IllegalAccessException {
        TestHarness.isOnScreen(TestHarness.getScreenMapping(screenName).screen);
    }

    @Then("^I will see the Disclaimer button$")
    public void i_will_see_the_Disclaimer_button() {
        TestHarness.currentScreen.checkButtonIsDisplayed(R.string.generic_disclaimer_button_title_265);
    }
}
