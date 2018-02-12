package com.vitalityactive.va.cucumber.stepdefinitions;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.TestData;
import com.vitalityactive.va.cucumber.screens.ForgotPasswordScreen;
import com.vitalityactive.va.cucumber.screens.LoginScreen;
import com.vitalityactive.va.cucumber.screens.SplashScreen;
import com.vitalityactive.va.cucumber.testData.TestDataForLogin;
import com.vitalityactive.va.cucumber.utils.ScreenMapping;
import com.vitalityactive.va.cucumber.utils.TestHarness;

import org.junit.Test;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginSteps {
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

    @Given("^I am on the (Login|Forgot Password) screen$")
    @SuppressWarnings("unchecked")
    public void iAmOnAScreen(String screenName) throws InstantiationException, IllegalAccessException {
        TestHarness.setup().clearAllData();
        ScreenMapping screenMapping = TestHarness.getScreenMapping(screenName);
        TestHarness.givenIAmOnActivity(screenMapping.activity, screenMapping.screen);
    }

    @Given("^I have seen an error message$")
    public void iHaveSeenErrorMessage() {
        TestHarness.currentScreen.checkDialogWithButtonIsDisplayed(R.string.ok_button_title_40);
    }

    @Given("^I see an incorrect email or password message$")
    public void loginAttemptFailedOnce() throws InstantiationException, IllegalAccessException {
        TestHarness.currentScreen = TestHarness.currentScreen.is(LoginScreen.class).clickLogin();
        checkThatThePromptAboutIncorrectLoginDetailsIsDisplayed();
    }

    @When("^I have entered a correct email$")
    public void enterCorrectUsername() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(LoginScreen.class)
                .enterUsername(TestDataForLogin.USERNAME);
    }

    @When("^I have NOT entered an email$")
    public void enterNoUsername() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(LoginScreen.class)
                .enterUsername(TestDataForLogin.EMPTY);
    }

    @When("^I have entered an invalid email$")
    public void enterInvalidFormattedUsername() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(LoginScreen.class)
                .enterUsername(TestDataForLogin.INVALID_FORMAT_USERNAME)
                .focusOnPassword();
    }

    @When("^I have entered an incorrect email$")
    public void enterIncorrectUsername() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(LoginScreen.class)
                .enterUsername(TestDataForLogin.INCORRECT_USERNAME);
    }

    @When("^I have entered a correct password$")
    public void enterCorrectPassword() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(LoginScreen.class)
                .enterPassword(TestDataForLogin.PASSWORD);
    }

    @When("^I have NOT entered a password$")
    public void enterNoPassword() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(LoginScreen.class)
                .enterPassword(TestDataForLogin.EMPTY);
    }

    @When("^I have entered an incorrect password$")
    public void enterIncorrectPassword() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(LoginScreen.class)
                .enterPassword(TestDataForLogin.INCORRECT_PASSWORD);
    }

    @When("^I click on login again$")
    public void tryToLoginAgain() {
        TestHarness.currentScreen = TestHarness.currentScreen
                .clickOnDialogButtonWithText(R.string.ok_button_title_40)
                .is(LoginScreen.class)
                .clickLogin();
    }

    @When("^I tap login$")
    public void clickOnButtonWithText() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(LoginScreen.class).clickOnButtonWithText(R.string.login_login_button_title_20);
    }

    @When("^I did NOT enter an email address$")
    public void enterNoEmailAddress() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(ForgotPasswordScreen.class)
                .enterEmail(TestData.EMPTY);
    }

    @When("^I enter an email address$")
    public void enterEmailAddress() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(ForgotPasswordScreen.class)
                .enterEmail(TestData.USERNAME);
    }

    @When("^I enter a registered email address$")
    public void enterRegEmailAddress() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(ForgotPasswordScreen.class)
                .enterEmail("ShawnBernard@SITtest.com");
    }

    @When("^I have entered a correct email but not registered$")
    public void enterNotRegEmailAddress() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(ForgotPasswordScreen.class)
                .enterEmail(TestData.USERNAME);
    }

    @When("^I enter an invalid email$")
    public void enterInvalidEmailAddress() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(ForgotPasswordScreen.class)
                .enterUsername(TestData.INVALID_FORMAT_USERNAME)
                .focusOnPassword();
    }

    @When("^I tap Forgot password$")
    public void clickForgotPassword() {
        TestHarness.currentScreen = TestHarness.currentScreen.clickOnButtonWithText(R.string.login_forgot_password_button_title_22);
    }

    @When("^I tap Next$")
    public void clickNext() {
        TestHarness.currentScreen = TestHarness.currentScreen.clickOnButtonWithText(R.string.next_button_title_84);
    }

    @When("^I tap OK")
    public void clickOK() {
        TestHarness.currentScreen = TestHarness.currentScreen.clickOnButtonWithText(R.string.ok_button_title_40);
    }

    @Then("^I should be successfully logged in$")
    public void checkIsLoggedOn() throws InstantiationException, IllegalAccessException {
        TestHarness.waitForScreen(SplashScreen.class).checkIsOnPage();
    }

    @Then("^I should get an invalid email message$")
    public void checkEmailValidationErrorMessage() {
        TestHarness.currentScreen.is(LoginScreen.class).checkUserValidationErrorMessageShown();
    }

    @Then("^I should get an incorrect email or password message$")
    public void checkThatThePromptAboutIncorrectLoginDetailsIsDisplayed() {
        TestHarness.delay();
        TestHarness.currentScreen
                .checkDialogWithTextIsDisplayed(R.string.login_incorrect_email_password_error_message_48);
    }

    @Then("^the message should be removed$")
    public void removeErrorMessage() {
        TestHarness.currentScreen.checkNoDialogIsDisplayed();
    }

    @Then("^The (.+) icon should get highlighted$")
    public void theIconShouldGetHighlighted(String icon) {
        TestHarness.currentScreen.is(LoginScreen.class)
                .checkIconIsHighlighted(icon);
    }

    @Then("^I should get an invalid password error message$")
    public void checkPasswordValidationErrorMessage() {
        TestHarness.currentScreen.is(LoginScreen.class)
                .checkUserValidationErrorMessageShown();
    }

    @Then("^the login button is enabled$")
    public void checkButtonWithTextIsEnabled() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(LoginScreen.class).checkButtonIsEnabled("enabled");
    }

    @Then("^the login button is disabled$")
    public void checkButtonWithTextIsDisabled() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(LoginScreen.class).checkButtonIsEnabled("disabled");
    }

    @Then("^The icon should get highlighted$")
    public void theIconShouldGetHighlighted() {
        TestHarness.currentScreen.is(ForgotPasswordScreen.class)
                .checkIconIsHighlighted();
    }

    @Then("^the (Forgot Password) screen is loaded$")
    @SuppressWarnings("unchecked")
    public void checkIsOnScreen(String screenName) throws InstantiationException, IllegalAccessException {
        TestHarness.isOnScreen(TestHarness.getScreenMapping(screenName).screen);
    }

    @Then("^i should get an invalid email message$")
    public void checkUsernameValidationErrorMessage() {
        TestHarness.currentScreen.is(ForgotPasswordScreen.class)
                .checkUserValidationErrorMessageShown();
    }

    @Then("^pop up with email sent should show$")
    public void emailSentPopup() {
        TestHarness.currentScreen
                .checkDialogWithTextIsDisplayed(R.string.forgot_password_confirmation_screen_email_sent_message_59);
    }

    @Then("^pop up with email not registered should show$")
    public void emailNotRegisteredPopup() {
        TestHarness.currentScreen
                .checkDialogWithTextIsDisplayed(R.string.forgot_password_email_not_registered_alert_message_57);
    }

    @Then("^the Next button is enabled$")
    public void checkButtonIsEnabled() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(ForgotPasswordScreen.class).checkButtonIsEnabled("enabled");
    }

    @Then("^the Next button is disabled$")
    public void checkButtonIsDisabled() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(ForgotPasswordScreen.class).checkButtonIsEnabled("disabled");
    }
//     @Given("^NO Password on (Login)$")
//    public void noPassword(String screenName) throws InstantiationException, IllegalAccessException {
//        TestHarness.setup().clearAllData();
//        ScreenMapping screenMapping = TestHarness.getScreenMapping(screenName);
//        TestHarness.givenIAmOnActivity(screenMapping.activity, screenMapping.screen);
//        TestHarness.currentScreen = TestHarness.currentScreen.is(LoginScreen.class)
//                .enterUsername(TestDataForLogin.USERNAME).enterPassword(TestDataForLogin.EMPTY).checkButtonIsEnabled("disabled");
//        }

}

