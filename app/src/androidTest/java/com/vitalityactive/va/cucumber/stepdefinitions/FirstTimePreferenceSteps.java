package com.vitalityactive.va.cucumber.stepdefinitions;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.screens.LoginScreen;
import com.vitalityactive.va.cucumber.screens.SplashScreen;
import com.vitalityactive.va.cucumber.screens.UserPreferencesScreen;
import com.vitalityactive.va.cucumber.testData.TestDataForNSD;
import com.vitalityactive.va.cucumber.utils.ScreenMapping;
import com.vitalityactive.va.cucumber.utils.TestHarness;

import org.junit.Test;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class FirstTimePreferenceSteps {
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

    @Given("^I am on the (terms and conditions|preference) screen$")
    @SuppressWarnings("unchecked")
    public void termsAndConditionScreen(String screenName) throws InstantiationException, IllegalAccessException {
        ScreenMapping screenMapping = TestHarness.getScreenMapping(screenName);
        TestHarness.givenIAmOnActivity(screenMapping.activity, screenMapping.screen);
    }

    @Given("^I have logged in for the first time$")
    public void firstTimeLogin() throws IllegalAccessException, InstantiationException {
        TestHarness.startVitalityActiveAsAFreshInstall();
        TestHarness.currentScreen = TestHarness.isOnScreen(LoginScreen.class)
                .skipOnboardingScreens()
                // todo: need to login with a scenario specific user
                .enterUsername(TestDataForNSD.USERNAME)
                .enterPassword("TestPass123").clickLogin();
        TestHarness.waitForScreen(SplashScreen.class)
                .clickOnBackgroundAndIgnoreNextScreen();
        TestHarness.waitForScreen(UserPreferencesScreen.class).checkIsOnPage();
    }

    @Given("^(email|analytics|crash report|remember me) toggle is off by default$")
    public void checkToggleDefault(String toggle) {
        TestHarness.currentScreen.is(UserPreferencesScreen.class).toggleOff(toggle);
    }

    @Given("^And manage in setting link is visible$")
    public void checkLinkVisibility() {
        TestHarness.currentScreen.checkButtonIsDisplayed(R.string.user_prefs_manage_in_settings_button_title_91);
    }

    @When("^i accept terms and condition$")
    public void accept(final String text) {
        TestHarness.currentScreen = TestHarness.currentScreen.clickOnButtonWithText(text);
    }

    @When("^i toggle (email|analytics|crash report|remember me) on$")
    public void setToggleOn(String text) {
        TestHarness.currentScreen = TestHarness.currentScreen.is(UserPreferencesScreen.class).toggleOn(text);
    }

    @When("^communication instruction is shown$")
    public void communicationInstruction() {
        TestHarness.currentScreen.is(UserPreferencesScreen.class).checkInfoIsDisplayed(R.string.user_prefs_communication_group_header_message_65);
    }

    @When("^i see email header$")
    public void emailHeader() {
        TestHarness.currentScreen.is(UserPreferencesScreen.class).checkInfoIsDisplayed(R.string.user_prefs_email_toggle_message_66);
    }

    @When("^i see app notification header$")
    public void appNotifsHeader() {
        TestHarness.currentScreen.is(UserPreferencesScreen.class).checkInfoIsDisplayed(R.string.user_prefs_notifications_title_89);
    }

    @When("^I tap the link$")
    public void i_tap_the_link() {
        TestHarness.currentScreen.clickOnButtonWithText(R.string.user_prefs_manage_in_settings_button_title_91);
    }

    @When("^privacy instruction is shown$")
    public void privacyInstruction() {
        TestHarness.currentScreen.is(UserPreferencesScreen.class).checkInfoIsDisplayed(R.string.user_prefs_privacy_group_header_title_70);
    }

    @When("^i see analytics header$")
    public void analyticsHeader() {
        TestHarness.currentScreen.is(UserPreferencesScreen.class).checkInfoIsDisplayed(R.string.user_prefs_analytics_toggle_title_73);
    }

    @When("^i see crash report header$")
    public void crashReportHeader() {
        TestHarness.currentScreen.is(UserPreferencesScreen.class).checkInfoIsDisplayed(R.string.user_prefs_crash_reports_toggle_title_75);
    }

    @When("^security instruction is shown$")
    public void securityInstruction() {
        TestHarness.currentScreen.is(UserPreferencesScreen.class).checkInfoIsDisplayed(R.string.user_prefs_security_group_header_message_78);
    }

    @When("^i see remember me header$")
    public void rememberMeHeader() {
        TestHarness.currentScreen.is(UserPreferencesScreen.class).checkInfoIsDisplayed(R.string.user_prefs_remember_me_toggle_title_81);
    }

    @When("^I toggle (email|analytics|crash report|remember me) off$")
    public void setToggleOff(String text) {
        TestHarness.currentScreen = TestHarness.currentScreen.is(UserPreferencesScreen.class).toggle(text);
    }

    @When("^When I tap the link$")
    public void tapLink(String text) {
        TestHarness.currentScreen = TestHarness.currentScreen.clickOnButtonWithText(text);
    }

    @When("i tap Next")
    public void clickOnNext() throws IllegalAccessException, InstantiationException {
        TestHarness.currentScreen = TestHarness.currentScreen.is(UserPreferencesScreen.class).clickNext();
    }

    @When("I scroll down")
    public void scroll() throws IllegalAccessException, InstantiationException {
        TestHarness.currentScreen = TestHarness.currentScreen.is(UserPreferencesScreen.class).scroll();
    }

    @Then("^the (preference) screen is loaded$")
    @SuppressWarnings("unchecked")
    public void loaded(String screenName) throws InstantiationException, IllegalAccessException {
        TestHarness.isOnScreen(TestHarness.getScreenMapping(screenName).screen);
    }

    @Then("^(email|analytics|crash report|remember me) is set off$")
    public void checkToggleOff(String id) {
        TestHarness.currentScreen.is(UserPreferencesScreen.class).toggleOff(id);
    }

    @Then("^(email|analytics|crash report|remember me) is set on$")
    public void checkToggleOn(String id) {
        TestHarness.currentScreen.is(UserPreferencesScreen.class).toggleOn(id);
    }

    @Then("^Then device settings should be shown$")
    public void navigateToSetting() {

    }

    @Then("^a pop up with cancel and continue should show$")
    public void continueConfirmation() {
        TestHarness.currentScreen.checkDialogWithButtonIsDisplayed(R.string.cancel_button_title_24, R.string.continue_button_title_87);
    }

    @Then("^pop up is closed$")
    public void pop_up_is_closed() throws Throwable {
        TestHarness.isOnScreen(UserPreferencesScreen.class);
    }


}
