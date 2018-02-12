package com.vitalityactive.va.cucumber.stepdefinitions;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.screens.NonSmokersPrivacyPolicyScreen;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.ScreenMapping;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.cucumber.utils.TestNavigator;
import com.vitalityactive.va.testutilities.VitalityActive;

import org.junit.Test;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class NonSmokersDeclarationSteps {
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

    @Given("^I am on the (NonSmokers|NonSmokersDeclaration|privacy policy|nonSmoking completion) screen$")
    @SuppressWarnings("unchecked")
    public void iAmOnAScreen(String screenName) throws InstantiationException, IllegalAccessException {
        ScreenMapping screenMapping = TestHarness.getScreenMapping(screenName);
        TestHarness.givenIAmOnActivity(screenMapping.activity, screenMapping.screen);
    }

    @Given("^I have navigated to the home screen$")
    public void iHaveNavigatedToTheHomeScreen() throws InstantiationException, IllegalAccessException {
        TestHarness.startVitalityActiveWithClearedData();
        TestNavigator.toHomeScreen().checkIsOnPage();
    }

    @Given("^There is an error on the service$")
    public void thereIsAServiceError() {
        MockNetworkHandler.setupMockServerIssues();
    }

    @Given("^The service is working again$")
    public void theServiceIsWorkingAgain() {
        MockNetworkHandler.resetMockIssues();
    }

    public void thereIsNoServiceError() {
        MockNetworkHandler.resetMockIssues();
    }

    @Given("^I have seen an error message with (.+) and (.+) options$")
    public void iHaveSeenErrorMessage(int button1Text, int button2Text) {
        TestHarness.currentScreen.checkDialogWithButtonIsDisplayed(button1Text, button2Text);
    }

    @Given("^I have (an|No) internet connection$")
    public void enableNetwork(String enabled) {
        enableNetwork(enabled.equals("an"));
    }

    private void enableNetwork(boolean enabled) {
        if (enabled) {
            MockNetworkHandler.resetMockIssues();
        } else {
            MockNetworkHandler.shutdown();
        }
    }

    @Given("The non-smokers declaration is loaded")
    public void theNonSmokersDeclarationIsLoaded() {
        MockNetworkHandler.enqueueResponseFromFile(200, "non_smokers_declaration.txt");
    }

    @Given("The privacy policy is loaded")
    public void thePrivacyPolicyIsLoaded() {
        MockNetworkHandler.enqueueResponseFromFile(200, "non_smokers_declaration.txt");
    }

    @Given("the agree response is loaded")
    public void theAgreeResponseIsLoaded() {
        MockNetworkHandler.enqueueResponseWithEmptyJsonObject(201);
    }

    @When("^I tap Get Started$")
    public void clickGetStarted() {
        TestHarness.currentScreen = TestHarness.currentScreen.clickOnButtonWithText(R.string.get_started_button_title_103);
        TestHarness.shortDelay(500);
    }

    @When("^I tap Learn More$")
    public void clickLearnMore() {
        TestHarness.currentScreen = TestHarness.currentScreen.clickOnButtonWithText(R.string.learn_more_button_title_104);
    }

    @When("^I tap Great$")
    public void clickGreat() {
        TestHarness.currentScreen = TestHarness.currentScreen.clickOnButtonWithText(R.string.great_button_title_120);
    }

    @When("^I tap NSD$")
    public void clickOnButtonWithText() {
        TestHarness.currentScreen = TestHarness.currentScreen.clickOnButtonWithText(R.string.home_card_card_title_96);
    }

    @When("i tap on the Back button")
    public void clickOnBack() {
        TestHarness.currentScreen = TestHarness.currentScreen.pressBack();
    }

    @When("^I focus on the (.+) field$")
    public void clickOnField(String field) {
        TestHarness.currentScreen.focusOnField(field);
    }

    @When("i tap on More then I Declare")
    public void iTapOnMoreThenDeclare() {
        VitalityActive.Navigate.clickMoreThen(R.string.declare_button_title_114);
    }

    @When("i tap on More then i Agree")
    public void iTapOnMoreThenAgree() {
        VitalityActive.Navigate.clickMoreThen(R.string.agree_button_title_50);
    }


    @Then("^the (Login|Registration|home|NonSmokers|NonSmokersDeclaration|privacy policy|nonSmoking completion|learn more) screen is loaded$")
    @SuppressWarnings("unchecked")
    public void checkIsOnScreen(String screenName) throws InstantiationException, IllegalAccessException {
        TestHarness.isOnScreen(TestHarness.getScreenMapping(screenName).screen);
    }

    @Then("^The error message should be removed and control should return to Privacy Policy screen$")
    public void controlShouldReturnToPrivacyPolicyScreen() throws InstantiationException, IllegalAccessException {
        TestHarness.isOnScreen(NonSmokersPrivacyPolicyScreen.class);
    }
}
