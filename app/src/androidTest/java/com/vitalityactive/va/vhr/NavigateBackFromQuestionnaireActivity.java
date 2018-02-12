package com.vitalityactive.va.vhr;

import android.annotation.SuppressLint;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.screens.VHRLandingScreen;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.testutilities.VitalityActive;
import com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@UITestWithMockedNetwork
public class NavigateBackFromQuestionnaireActivity extends BaseTests {
    @Before
    public void setUp() throws InstantiationException, IllegalAccessException {
        super.setUp();
        // enqueue a second response
        MockNetworkHandler.enqueueResponseFromFile(200, "vhr/completed_vhr_questionnaire.json");
    }

    @SuppressLint("MissingSuperCall")
    @After
    public void tearDown()
    {
        TestHarness.tearDown();
    }

    @Test
    public void canNavigate_using_actionbar_up() throws IllegalAccessException, InstantiationException {
        // when questionnaire started, the first question section is visible
        setupVHRCompletedQuestionnaireDataAndLaunchLandingActivity();
        clickOnFirstEditButton();
        onView(withText("Your Eating Habits")).check(matches(isDisplayed()));

        // when clicking on up action (cross)
        onView(VitalityActive.Matcher.withDrawable(R.drawable.close)).perform(click());

        // then the landing activity is shown after the data is loaded
        TestHarness.waitForScreen(VHRLandingScreen.class, 3);
    }

    @Test
    public void canNavigate_using_hardware_back() throws IllegalAccessException, InstantiationException {
        // when questionnaire started, the first question section is visible
        setupVHRCompletedQuestionnaireDataAndLaunchLandingActivity();
        clickOnFirstEditButton();
        onView(withText("Your Eating Habits")).check(matches(isDisplayed()));

        // when navigating using hardware back
        TestHarness.pressHardwareBack();

        // then the landing activity is shown after the data is loaded
        TestHarness.waitForScreen(VHRLandingScreen.class, 3);
    }
}
