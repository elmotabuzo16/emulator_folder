package com.vitalityactive.va.cucumber.stepdefinitions;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.screens.PointsScreen;
import com.vitalityactive.va.cucumber.utils.ScreenMapping;
import com.vitalityactive.va.cucumber.utils.TestHarness;

import org.junit.Test;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class PointsMonitorSteps {

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

    @Given("^I am on the points screen$")
    @SuppressWarnings("unchecked")
    public void iAmOnAScreen(String screenName) throws InstantiationException, IllegalAccessException {
        TestHarness.setup().clearAllData();
        ScreenMapping screenMapping = TestHarness.getScreenMapping(screenName);
        TestHarness.launchActivity(screenMapping.activity, screenMapping.screen);
    }

    @Given("^I have no points$")
    public void noPoints() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(PointsScreen.class);
    }

    @Then("the screen should display message and ‘help’ button")
    public void messageWithHelpButton() {
        TestHarness.currentScreen.checkDialogWithTextIsDisplayed(R.string.login_incorrect_email_password_error_message_48);
    }


}
