package com.vitalityactive.va.vna;

import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.shared.questionnaire.repository.model.AnswerModel;
import com.vitalityactive.va.utilities.TimeUtilities;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class SubmissionTests extends BaseTests {
    @Before
    public void setUp() throws InstantiationException, IllegalAccessException {
        super.setUp();
        setupVnaQuestionnaireDataAndLaunchLandingActivity();
    }

    @Test
    public void submit_successfully() {
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        setupMockAnsweredData();
        clickOnFirstStartButton();
        navigateThroughAllTheSections();

        MockNetworkHandler.enqueueResponseWithEmptyJsonObject(200);
        onView(withText("Done")).perform(click());

        TestHarness.waitForLoadingIndicatorToNotDisplay();
        onView(withText("Food consumption Completed!")).check(matches(isDisplayed()));
    }

    protected void navigateThroughAllTheSections() {
        onView(withText("Fruit")).check(matches(isDisplayed()));
        onView(withText("Next")).perform(click());

        onView(withText("Dairy")).check(matches(isDisplayed()));
        onView(withText("Next")).perform(click());

        onView(withText("Vegtables")).check(matches(isDisplayed()));        // also typo in mock data
        onView(withText("Next")).perform(click());

        onView(withText("Meat")).check(matches(isDisplayed()));
    }

    private void setupMockAnsweredData() {
        TestHarness.MockData data = new TestHarness.MockData();
        addAnswers(data);
        TestHarness.setup().addVNAData(data);
    }

    private void addAnswers(TestHarness.MockData data) {
        data.datum.add(new AnswerModel(100, new Answer("2"), TimeUtilities.getCurrentTimestamp()));
        data.datum.add(new AnswerModel(106, new Answer("I do not eat or drink dairy products"), TimeUtilities.getCurrentTimestamp()));
        data.datum.add(new AnswerModel( 99, new Answer("3"), TimeUtilities.getCurrentTimestamp()));
        data.datum.add(new AnswerModel(105, new Answer("Always"), TimeUtilities.getCurrentTimestamp()));
    }
}
