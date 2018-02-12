package com.vitalityactive.va.questionnaire;

import android.support.annotation.CallSuper;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.shared.questionnaire.repository.model.AnswerModel;
import com.vitalityactive.va.testutilities.VitalityActive;
import com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.vhr.landing.VHRLandingActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@UITestWithMockedNetwork
public class QuestionVisibilityDependingOnAnotherSectionTests {
    @Rule
    public TestName name = new TestName();

    @Before
    @CallSuper
    public void setUp() throws IllegalAccessException, InstantiationException {
        TestHarness.Settings.useMockService = true;
        TestHarness.setup(name.getMethodName())
                .clearEverythingLikeAFreshInstall()
                .withLoggedInUser();
        MockNetworkHandler.enqueueResponseFromFile(200, "vhr/visibility_based_on_other_section.json");
        TestHarness.launchActivity(VHRLandingActivity.class);
    }

    @Test
    public void hba1c_visible_if_parent_question_is_answered() {
        setupMockAnsweredData();
        clickOnFirstEditButton();

        // first section
        onView(withText(R.string.next_button_title_84)).check(matches(isEnabled())).perform(click());

        // need to click on an item to toggle next button
        // so might as well click on the item we need to set
        onView(withText("Diabetes")).check(matches(isEnabled())).perform(click());
        onView(withText(R.string.next_button_title_84)).check(matches(isEnabled())).perform(click());

        // click next
        onView(withText(R.string.next_button_title_84)).check(matches(isEnabled())).perform(click());

        // then the hba1c is displayed
        onView(withText("Do you know your exact HbA1c level?")).check(matches(isDisplayed()));
    }

    private void setupMockAnsweredData() {
        TestHarness.MockData data = new TestHarness.MockData();
        section1(data);
        // nothing answered in section 2
        section3(data);
        TestHarness.setup().addVHRData(data);
    }

    private void section1(TestHarness.MockData data) {
        data.datum.add(new AnswerModel(5, new Answer("Poor"), TimeUtilities.getCurrentTimestamp()));
    }

    private void section3(TestHarness.MockData data) {
        data.datum.add(new AnswerModel(66, new Answer("Asthma"), TimeUtilities.getCurrentTimestamp()));
    }

    private void clickOnFirstEditButton() {
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        onView(VitalityActive.Matcher.firstMatched(withText(R.string.landing_screen_edit_button_307))).perform(click());
    }
}
