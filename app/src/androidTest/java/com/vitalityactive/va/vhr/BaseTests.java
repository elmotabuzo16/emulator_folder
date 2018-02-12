package com.vitalityactive.va.vhr;

import android.support.annotation.CallSuper;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.testutilities.VitalityActive;
import com.vitalityactive.va.vhr.landing.VHRLandingActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class BaseTests {
    @Rule
    public TestName name = new TestName();

    @Before
    @CallSuper
    public void setUp() throws IllegalAccessException, InstantiationException {
        TestHarness.Settings.useMockService = true;
        TestHarness.setup(name.getMethodName())
                .clearEverythingLikeAFreshInstall()
                .withLoggedInUser();
    }

    @After
    @CallSuper
    public void tearDown() {
        TestHarness.tearDown();
    }

    protected void setupVHRCompletedQuestionnaireDataAndLaunchLandingActivity() {
        setupVHRCompletedQuestionnaireDataAndLaunchLandingActivity("vhr/completed_vhr_questionnaire.json");
    }

    protected void setupVHRIncompleteQuestionnaireDataAndLaunchLandingActivity() {
        setupVHRCompletedQuestionnaireDataAndLaunchLandingActivity("vhr/incomplete_vhr_questionnaire.json");
    }

    protected void setupVHRCompletedQuestionnaireDataAndLaunchLandingActivity(String file) {
        MockNetworkHandler.enqueueResponseFromFile(200, file);
        TestHarness.launchActivity(VHRLandingActivity.class);
    }

    protected void clickOnFirstEditButton() {
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        onView(VitalityActive.Matcher.firstMatched(withText(R.string.landing_screen_edit_button_307))).perform(click());
    }
}
