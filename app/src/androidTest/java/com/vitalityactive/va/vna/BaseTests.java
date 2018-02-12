package com.vitalityactive.va.vna;

import android.support.annotation.CallSuper;

import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.vna.landing.VNALandingActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

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

    protected void setupVnaQuestionnaireDataAndLaunchLandingActivity() {
        MockNetworkHandler.enqueueResponseFromFile(200, "vna/vna_questionnaire_mock_data_not_based_on_real_response.json");
        TestHarness.launchActivity(VNALandingActivity.class);
    }

    protected void clickOnFirstStartButton() {
        onView(allOf(withText("Start"), hasSibling(withText("Food consumption"))))
                .perform(click());
    }
}
