package com.vitalityactive.va.cucumber.stepdefinitions;

import com.vitalityactive.va.cucumber.screens.VHCCaptureResultScreen;
import com.vitalityactive.va.cucumber.utils.TestHarness;

import org.junit.Test;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.When;

public class VHCCaptureResultSteps {
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

    //    @Given("^I am on the (help|VHC Capture) screen$")
//    public void iAmOnAScreen(String screenName) throws InstantiationException, IllegalAccessException {
//        ScreenMapping screenMapping = TestHarness.getScreenMapping(screenName);
//        TestHarness.givenIAmOnActivity(screenMapping.activity, screenMapping.screen);
//    }
//
//    @Then("^the (help|VHC Capture) screen is loaded$")
//    public void checkIsOnScreen(String screenName) throws InstantiationException, IllegalAccessException {
//        TestHarness.isOnScreen(TestHarness.getScreenMapping(screenName).screen);
//    }
    @When("^I enter BMI$")
    public void captureBMI() {
        TestHarness.currentScreen.is(VHCCaptureResultScreen.class);
    }

    @When("^I enter WC$")
    public void captureWC() {
        TestHarness.currentScreen.is(VHCCaptureResultScreen.class);
    }

    @When("^I enter BP$")
    public void captureBP() {
        TestHarness.currentScreen.is(VHCCaptureResultScreen.class);
    }

    @When("^I enter BG$")
    public void captureBG() {
        TestHarness.currentScreen.is(VHCCaptureResultScreen.class);
    }

    @When("^I enter HB$")
    public void captureHB() {
        TestHarness.currentScreen.is(VHCCaptureResultScreen.class);
    }
}

