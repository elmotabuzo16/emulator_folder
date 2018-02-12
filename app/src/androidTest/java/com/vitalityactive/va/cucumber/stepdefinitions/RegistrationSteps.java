package com.vitalityactive.va.cucumber.stepdefinitions;

import android.support.test.espresso.ViewInteraction;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.TestData;
import com.vitalityactive.va.cucumber.screens.LoginScreen;
import com.vitalityactive.va.cucumber.screens.RegistrationScreen;
import com.vitalityactive.va.cucumber.screens.SplashScreen;
import com.vitalityactive.va.cucumber.testData.TestDataForRegistration;
import com.vitalityactive.va.cucumber.utils.ScreenMapping;
import com.vitalityactive.va.cucumber.utils.TestHarness;

import org.junit.Test;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class RegistrationSteps {
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

    @Given("^I am on the (Registration) screen$")
    @SuppressWarnings("unchecked")
    public void iAmOnAScreen(String screenName) throws InstantiationException, IllegalAccessException {
        ScreenMapping screenMapping = TestHarness.getScreenMapping(screenName);
        TestHarness.givenIAmOnActivity(screenMapping.activity, screenMapping.screen);
    }

    @Given("^I have entered a correct email address$")
    public void enterCorrectEmailAddress() {
        TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterEmail(TestDataForRegistration.USERNAME);
    }

    @Given("^I have entered an incorrect email address$")
    public void enterIncorrectEmailAddress() {
        TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterEmail(TestData.INCORRECT_USERNAME);
    }

    @Given("^I have entered a correct insurer code$")
    public void enterCorrectInsurerCode() {
        TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterInsurerCode(TestDataForRegistration.INSURER_CODE);
    }

    @Given("^I have entered an incorrect insurer code$")
    public void enterIncorrectInsurerCode() {
        TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterInsurerCode(TestDataForRegistration.INCORRECT_INSURER_CODE);
    }

    @Given("^I have entered a valid password$")
    public void enterValidPassword() {
        TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterPasswordAndConfirm(TestDataForRegistration.PASSWORD);
    }

    @Given("^I have entered an invalid password$")
    public void enterInvalidPassword() {
        TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterPasswordAndConfirm(TestData.INCORRECT_PASSWORD);
    }

    @Given("^I have entered all the registration details$")
    public void enterAllRegistrationDetails() {
        TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterAllDetails(TestDataForRegistration.USERNAME, TestDataForRegistration.PASSWORD, TestDataForRegistration.INSURER_CODE);
    }

    @When("^I have NOT entered an email address$")
    public void enterNoEmailAddress() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterEmail(TestData.EMPTY);
    }

    @When("^I have entered invalid insurer code$")
    public void invalidInsurerCode() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterInsurerCode("insurer");
    }

    @When("^I have entered an email address$")
    public void enterEmailAddress() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterEmail(TestDataForRegistration.USERNAME);
    }

    @When("^I have entered a short code email$")
    public void enterShortCodeEmail() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterEmail(TestDataForRegistration.ShortUSERNAME);
    }

    @When("^I have entered a registered email address$")
    public void enterRegisteredEmailAddress() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterEmail(TestDataForRegistration.REGUSERNAME);
    }

    @When("^I have entered a confirm password$")
    public void enterConfirmPassword() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterConfirmPassword(TestDataForRegistration.PASSWORD);
    }

    @When("^I have entered a password$")
    public void enterPassword() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterPassword(TestDataForRegistration.PASSWORD);
    }

    @When("^I have entered a short insurer code$")
    public void enterShortInsurerCode() {
        TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterInsurerCode(TestDataForRegistration.ShortINSURER_CODE);
    }

    @When("^I have entered an insurer code$")
    public void enterInsurerCode() {
        TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterInsurerCode(TestDataForRegistration.INSURER_CODE);
    }

    @When("^I have already registered with all the correct details$")
    public void registeredUser() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(RegistrationScreen.class)
                .checkRegisteredUser();
    }

    @When("^I have entered password with only alpha characters$")
    public void alphaCharactersOnly() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterAlphaPassword(TestDataForRegistration.ALPHAPASSWORD)
                .focusOnUsername();
    }

    @When("^I have entered password with only numeric characters$")
    public void numericCharactersOnly() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterNumericPassword(TestDataForRegistration.NUMERICPASSWORD).focusOnUsername();
    }

    @When("^I have entered password with only uppercase$")
    public void uppercaseCharactersOnly() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterPassword(TestDataForRegistration.PASSWORD.toUpperCase()).focusOnUsername();
    }

    @When("^I have entered password with only lowercase$")
    public void lowercaseCharactersOnly() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterPassword(TestDataForRegistration.PASSWORD.toLowerCase()).focusOnUsername();
    }

    @When("^I have entered password with less than 7 characters$")
    public void lessThanSevenCharacters() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterPassword("test").focusOnUsername();
    }

    @When("^I have NO confirm password$")
    public void noPasswordEntered() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterPassword(TestData.EMPTY);
    }

    @When("^I have NO password$")
    public void noConfirmPasswordEntered() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterConfirmPassword(TestData.EMPTY);
    }

    @When("^I have NO insurer code$")
    public void noInsurerCode() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(RegistrationScreen.class)
                .enterInsurerCode(TestData.EMPTY);
    }

    @When("^i tap Register$")
    public void clickRegister() {
        TestHarness.currentScreen.clickOnButtonWithText(R.string.login_register_button_title_23);
    }

    @When("^I tap Register$")
    public void clickOnButtonWithText() {
        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.register_button), withText(R.string.login_register_button_title_23),
                        withParent(allOf(withId(R.id.register_button_bar),
                                withParent(withId(R.id.register_content)))),
                        isDisplayed()));
        appCompatButton3.perform(click());
    }

    @Then("^i should get an invalid password error message$")
    public void checkPasswordValidationErrorMessageForRegistration() {
        TestHarness.currentScreen.is(RegistrationScreen.class)
                .checkUserValidationErrorMessageShown();
    }

    @Then("^i should get an invalid insurer code error message$")
    public void checkInsurerCodeValidationErrorMessageForRegistration() {
        TestHarness.currentScreen.is(RegistrationScreen.class)
                .checkUserValidationErrorMessageShown();
    }

    @Then("^The error message should be removed and control should return to Registration screen$")
    public void controlShouldReturnToRegistrationScreen() throws InstantiationException, IllegalAccessException {
        TestHarness.isOnScreen(RegistrationScreen.class);
    }

    @Then("^I should be Registered in successfully$")
    public void checkIsRegistered() throws InstantiationException, IllegalAccessException {
//        TestHarness.currentScreen.checkUnexpectedDialogWithTextIsNotDisplayed(R.string.registration_unable_to_register_alert_message_46);
        TestHarness.waitForScreen(SplashScreen.class).checkIsOnPage();
    }

    @Then("^I should get an invalid insurer code error message$")
    public void checkInsurerCodeValidationErrorMessage() {
        TestHarness.currentScreen.is(LoginScreen.class)
                .checkUserValidationErrorMessageShown();
    }

    @Then("^the register button is enabled$")
    public void checkButtonWithTextIsEnabled() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(RegistrationScreen.class).checkButtonIsEnabled("enabled");
    }

    @Then("^the register button is disabled$")
    public void checkButtonWithTextIsDisabled() {
        TestHarness.currentScreen = TestHarness.currentScreen.is(RegistrationScreen.class).checkButtonIsEnabled("disabled");
    }

    @Then("^I should get an error message that I am already registered$")
    public void registeredUserErrorMessage() {
        TestHarness.currentScreen.checkDialogWithTextIsDisplayed(R.string.registration_unable_to_register_alert_message_46);
    }

    @Then("^I should get an error message about incorrect insurer code$")
    public void checkThatErrorMessageIsDisplayed() {
        TestHarness.currentScreen.checkDialogWithTextIsDisplayed(R.string.registration_invalid_email_or_registration_code_alert_message_39);
    }
}
