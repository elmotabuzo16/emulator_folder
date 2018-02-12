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
import cucumber.api.java.en.When;

public class VHCLandingSteps {
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

    @Given("^I am on the (help|VHC Capture) screen$")
    @SuppressWarnings("unchecked")
    public void iAmOnAScreen(String screenName) throws InstantiationException, IllegalAccessException {
        ScreenMapping screenMapping = TestHarness.getScreenMapping(screenName);
        TestHarness.givenIAmOnActivity(screenMapping.activity, screenMapping.screen);
    }

    @When("^I tap on Capture Result$")
    public void tapCapture() {
        TestHarness.currentScreen.clickOnButtonWithText(R.string.onboarding_section_2_title_128);
    }

    @Then("^the (help|VHC Capture|blood pressure|glucose|HaB1C|BMI|Waist Circumference) screen is loaded$")
    @SuppressWarnings("unchecked")
    public void checkIsOnScreen(String screenName) throws InstantiationException, IllegalAccessException {
        TestHarness.isOnScreen(TestHarness.getScreenMapping(screenName).screen);
    }

}
