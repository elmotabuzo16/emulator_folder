package com.vitalityactive.va.vna;

import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@UITestWithMockedNetwork
public class QuestionnaireActivityTests extends BaseTests {
    @Before
    public void setUp() throws InstantiationException, IllegalAccessException {
        super.setUp();
        setupVnaQuestionnaireDataAndLaunchLandingActivity();
    }

    @Test
    public void loads_successfully() {
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        clickOnFirstStartButton();

        onView(withText("Fruit")).check(matches(isDisplayed()));
    }
}
