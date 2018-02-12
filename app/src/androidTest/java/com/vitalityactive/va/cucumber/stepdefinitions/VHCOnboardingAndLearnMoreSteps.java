package com.vitalityactive.va.cucumber.stepdefinitions;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.screens.BaseScreen;
import com.vitalityactive.va.cucumber.screens.HomeScreen;
import com.vitalityactive.va.cucumber.screens.LoginScreen;
import com.vitalityactive.va.cucumber.screens.SplashScreen;
import com.vitalityactive.va.cucumber.screens.UserPreferencesScreen;
import com.vitalityactive.va.cucumber.screens.VHCLearnMoreScreen;
import com.vitalityactive.va.cucumber.testData.TestDataForVHC;
import com.vitalityactive.va.cucumber.utils.ScreenMapping;
import com.vitalityactive.va.cucumber.utils.TestHarness;

import org.junit.Test;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class VHCOnboardingAndLearnMoreSteps {
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

    @Given("^I am on the (VHC Onboarding|VHC Learn More|VHC Landing) screen$")
    @SuppressWarnings("unchecked")
    public void iAmOnAScreen(String screenName) throws InstantiationException, IllegalAccessException {
        TestHarness.setup().clearAllData();
        ScreenMapping screenMapping = TestHarness.getScreenMapping(screenName);
        TestHarness.givenIAmOnActivity(screenMapping.activity, screenMapping.screen);
    }

    @Given("^I am on home screen$")
    public void navigateToHome() throws InstantiationException, IllegalAccessException {
        TestHarness.startVitalityActiveWithClearedData();
        TestHarness.currentScreen = TestHarness.isOnScreen(LoginScreen.class)
                .skipOnboardingScreens()
                .enterUsername(TestDataForVHC.USERNAME)
                .enterPassword(TestDataForVHC.PASSWORD)
                .clickLogin();
        TestHarness.waitForScreen(SplashScreen.class)
                .clickOnBackgroundAndIgnoreNextScreen();
        BaseScreen baseScreen = TestHarness.waitForAnyScreen(HomeScreen.class, UserPreferencesScreen.class);

        if (baseScreen.isOfType(UserPreferencesScreen.class)) {
            TestHarness.currentScreen.is(UserPreferencesScreen.class).clickNext();
        }
        TestHarness.waitForScreen(HomeScreen.class).checkIsOnPage();
    }

    @When("^I tap Got it$")
    public void clickGetStarted() {
        TestHarness.currentScreen = TestHarness.currentScreen.clickOnButtonWithText(R.string.generic_got_it_button_title_131);
    }
//    @When("^I tap Learn More$")
//    public void clickLearnMore() {
//        TestHarness.currentScreen = TestHarness.currentScreen.clickOnButtonWithText(R.string.learn_more_button_title_104);
//    }

    //    @When("i tap on the Back button")
//    public void clickOnBack() {
//        TestHarness.currentScreen = TestHarness.currentScreen.pressBack();
//    }
    @When("^ I tap on one of the metrics$")
    public void metrics() {
//        TestHarness.currentScreen = TestHarness.currentScreen.clickOnButton("back");
    }

    @When("^I tap Body Mass Index$")
    public void tapBMI() {
        TestHarness.currentScreen.clickOnButtonWithText(R.string.measurement_body_mass_index_title_134);
    }

    @When("^i tap VHC$")
    public void clickOnButtonWithText() {
        TestHarness.currentScreen = TestHarness.currentScreen.clickOnButtonWithText(R.string.home_card_card_title_125);
    }

    @When("^I tap waist circumference$")
    public void tapWC() {
        TestHarness.currentScreen.clickOnButtonWithText(R.string.measurement_waist_circumference_title_135);
    }

    @When("^I tap glucode$")
    public void tapGlucose() {
        TestHarness.currentScreen.clickOnButtonWithText(R.string.measurement_glucose_title_136);
    }

    @When("^I tap Blood Pressure$")
    public void tapBP() {
        TestHarness.currentScreen.clickOnButtonWithText(R.string.measurement_blood_pressure_title_137);
    }

    @When("^I tap Cholesterol$")
    public void tapCholesterol() {
        TestHarness.currentScreen.clickOnButtonWithText(R.string.measurement_cholesterol_title_138);
    }

    @When("^I tap hba1c$")
    public void taphba1c$() {
        TestHarness.currentScreen.clickOnButtonWithText(R.string.measurement_hba1c_title_139);
    }

    @When("^I tap Cancel$")
    public void tapCancel() {
        TestHarness.currentScreen.clickOnButtonWithText(R.string.cancel_button_title_24);
    }

    @When("^I tap Continue$")
    public void tapContinue() {
        TestHarness.currentScreen.clickOnButtonWithText(R.string.continue_button_title_87);
    }

    @Then("^the (VHC Onboarding|VHC Learn More|VHC Landing) screen is loaded$")
    @SuppressWarnings("unchecked")
    public void checkIsOnScreen(String screenName) throws InstantiationException, IllegalAccessException {
        TestHarness.waitForScreen(TestHarness.getScreenMapping(screenName).screen);
    }

    @Then("^I will read more about (Body Mass Index|Waist Circumference|Blood Glucose|Blood Pressure|Cholesterol|HbA1c)$")
    public void readMore(String name) {
        TestHarness.currentScreen = TestHarness.currentScreen.is(VHCLearnMoreScreen.class).checkTextIsDisplayed(name);
    }
}
