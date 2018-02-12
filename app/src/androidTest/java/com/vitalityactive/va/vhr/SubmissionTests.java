package com.vitalityactive.va.vhr;

import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.ProductFeature;
import com.vitalityactive.va.cucumber.screens.VHRLandingScreen;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.shared.questionnaire.repository.model.AnswerModel;
import com.vitalityactive.va.testutilities.VitalityActive;
import com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork;
import com.vitalityactive.va.utilities.TimeUtilities;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.testutilities.AssertUtils.assertUrlPathMatch;

@UITestWithMockedNetwork
public class SubmissionTests extends BaseTests {
    @Before
    public void setUp() throws InstantiationException, IllegalAccessException {
        super.setUp();
        setupVHRCompletedQuestionnaireDataAndLaunchLandingActivity();
    }

    @Test
    public void submit_successfully() {
        setupMockAnsweredData();
        clickOnFirstEditButton();

        navigateThroughAllTheSections();

        MockNetworkHandler.enqueueResponseWithEmptyJsonObject(200);
        onView(withText("Done")).perform(click());

        TestHarness.waitForLoadingIndicatorToNotDisplay();
        onView(withText("Great!")).check(matches(isDisplayed()));
    }

    @Test
    public void submit_failed_try_again_works() {
        setupMockAnsweredData();
        clickOnFirstEditButton();

        navigateThroughAllTheSections();

        MockNetworkHandler.enqueueResponseWithEmptyJsonObject(400);
        onView(withText("Done")).perform(click());

        // when try again in dialog clicked
        MockNetworkHandler.enqueueResponseWithEmptyJsonObject(200);
        VitalityActive.Navigate.clickOnDialogButtonWithText(R.string.try_again_button_title_43);

        TestHarness.waitForLoadingIndicatorToNotDisplay();
        onView(withText("Great!")).check(matches(isDisplayed()));
    }

    @Test
    @Ignore("API error")
    public void submit_with_privacy_policy() throws InterruptedException {
        setupRequireVHRPrivacyPolicyConsent();
        setupMockAnsweredData();
        clickOnFirstEditButton();

        navigateThroughAllTheSections();
        MockNetworkHandler.ignoreRecordedRequests();

        // get privacy policy text only
        MockNetworkHandler.enqueueResponseFromText(200, "mock privacy policy text");
        onView(withText("Done")).perform(click());
        assertUrlPathMatch(MockNetworkHandler.takeNextRequestUrl(), "get-article-by-url-title");

        // when privacy policy is agreed

        MockNetworkHandler.enqueueResponseWithEmptyJsonObject(200);
        VitalityActive.Navigate.clickMoreThenAgree();

        TestHarness.waitForLoadingIndicatorToNotDisplay();

        // then agreed
        assertUrlPathMatch(MockNetworkHandler.takeNextRequestUrl(), "processEvents");
        // then submitted
        assertUrlPathMatch(MockNetworkHandler.takeNextRequestUrl(), "captureAssessment");
    }

    @Test
    public void vitality_age_displayed_first_time() throws IllegalAccessException, InstantiationException {
        setupMockAnsweredData();
        clickOnFirstEditButton();
        navigateThroughAllTheSections();
        MockNetworkHandler.enqueueResponseFromFile(200, "capture_assessment_response.json");
        onView(withText("Done")).perform(click());
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        onView(withText("Great!")).check(matches(isDisplayed())).perform(click());
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        onView(withText(R.string.my_health_vitality_age_screen_title_613)).check(matches(isDisplayed()));
    }

    @Test
    @Ignore("API error when getting points")
    public void vitality_age_not_displayed() throws Exception {
        vitality_age_displayed_first_time();
        MockNetworkHandler.enqueueResponseFromFile(200, "vhr_questionnaire_progress_and_points_tracker_1.json");
        onView(VitalityActive.Matcher.withDrawable(R.drawable.close)).perform(click());
        TestHarness.waitForScreen(VHRLandingScreen.class, 3);
        clickOnFirstEditButton();
        navigateThroughAllTheSections();
        MockNetworkHandler.enqueueResponseFromFile(200, "capture_assessment_response.json");
        onView(withText("Done")).perform(click());
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        onView(withText("Great!")).check(matches(isDisplayed())).perform(click());
        TestHarness.waitForScreen(VHRLandingScreen.class, 3);
    }

    private void setupRequireVHRPrivacyPolicyConsent() {
        com.vitalityactive.va.persistence.models.ProductFeature feature = new com.vitalityactive.va.persistence.models.ProductFeature();
        feature.setType(ProductFeature._VHRDSCONSENT);
        TestHarness.MockData data = new TestHarness.MockData();
        data.datum.add(feature);
        TestHarness.setup().addData(data);
    }

    protected void navigateThroughAllTheSections() {
        onView(withText("Your Eating Habits")).check(matches(isDisplayed()));
        onView(withText("Next")).check(matches(isEnabled()));
        onView(withText("Next")).perform(click());

        onView(withText("Your Physical Activity Levels")).check(matches(isDisplayed()));
        onView(withText("Next")).perform(click());

        onView(withText("Your Sleeping Patterns")).check(matches(isDisplayed()));
        onView(withText("Next")).perform(click());

        onView(withText("Your Wellbeing")).check(matches(isDisplayed()));
    }

    protected void setupMockAnsweredData() {
        TestHarness.MockData data = new TestHarness.MockData();
        section1(data);
        section2(data);
        section3(data);
        section4(data);

        TestHarness.setup().addVHRData(data);
    }

    private void section1(TestHarness.MockData data) {
        for (int i = 99; i <= 101; i++) {
            addAnswerModel(data, i, "1");
        }
        for (int i = 107; i <= 109; i++) {
            addAnswerModel(data, i, "1");
        }
        addAnswerModel(data, 102, "Never");
        addAnswerModel(data, 103, "Half a teaspoon");
        addAnswerModel(data, 104, "Never");
        addAnswerModel(data, 105, "Never");
        addAnswerModel(data, 106, "Never");
        addAnswerModel(data, 110, "You are happy with your diet");
    }

    private void section2(TestHarness.MockData data) {
        addAnswerModel(data, 111, "Four");
        addAnswerModel(data, 112, "160");
        addAnswerModel(data, 113, "Moderate");
        addAnswerModel(data, 114, "Rarely or never");
        addAnswerModel(data, 115, "Rarely or never");
        addAnswerModel(data, 116, "1");
        addAnswerModel(data, 117, "1");
        addAnswerModel(data, 118, "1");
        addAnswerModel(data, 119, "I know my fitness level has to improve, bit I don 't really want to exercise more right now");
    }

    private void section3(TestHarness.MockData data) {
        addAnswerModel(data, 120, "Occasionally");
        addAnswerModel(data, 121, "6");
    }

    private void section4(TestHarness.MockData data) {
        addAnswerModel(data, 122, "Never");
        addAnswerModel(data, 123, "Never");
        addAnswerModel(data, 124, "Never");
        addAnswerModel(data, 125, "Never");
        addAnswerModel(data, 126, "Never");
        addAnswerModel(data, 127, "Never");
        addAnswerModel(data, 128, "Never");
        addAnswerModel(data, 129, "Never");
        addAnswerModel(data, 130, "Never");
        addAnswerModel(data, 131, "Never");
        addAnswerModel(data, 132, "I want to manage my stress better and would appreciate assistance");
    }

    private void addAnswerModel(TestHarness.MockData data, int questionId, String value) {
        data.datum.add(new AnswerModel(questionId, new Answer(value), TimeUtilities.getCurrentTimestamp()));
    }
}
