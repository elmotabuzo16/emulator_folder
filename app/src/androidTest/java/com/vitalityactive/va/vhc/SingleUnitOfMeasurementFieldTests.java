package com.vitalityactive.va.vhc;

import android.support.test.InstrumentationRegistry;

import com.vitalityactive.va.R;
import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.login.LoginRepositoryImpl;
import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.register.entity.Username;
import com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork;
import com.vitalityactive.va.vhc.landing.VHCLandingActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@UITestWithMockedNetwork
public class SingleUnitOfMeasurementFieldTests {
    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() throws IOException {
        TestHarness.Settings.useMockService = true;
        TestHarness.setup(name.getMethodName())
                .clearEverythingLikeAFreshInstall()
                .withLoggedInUser();
        persistLoginResponse();

        setupDataAndStartVHCLandingActivity();
    }

    @After
    public void tearDown() {
        TestHarness.tearDown();
    }

    private void setupDataAndStartVHCLandingActivity() {
        MockNetworkHandler.enqueueResponseFromFile(201, "vhc/landing_blood_pressure.json");
        TestHarness.launchActivity(VHCLandingActivity.class);
        TestHarness.waitForLoadingIndicatorToNotDisplay();
    }

    private void persistLoginResponse() throws java.io.IOException {
        DataStore dataStore = TestHarness.setup().getDefaultDataStore();
        LoginServiceResponse response = RepositoryTestBase.getResponse(LoginServiceResponse.class, "login/Login_successful_with_limit_vhc_features.json");
        new LoginRepositoryImpl(dataStore, new AppConfigRepository(dataStore, InstrumentationRegistry.getTargetContext()))
                .persistLoginResponse(response, new Username("Frikkie@Poggenpoel.com"));
    }

    private void goToCaptureActivity() {
        onView(withText(R.string.onboarding_section_2_title_128)).perform(click());
    }

    @Test
    public void displays_only_abbreviation() {
        goToCaptureActivity();

        // the abbreviation is displayed
        onView(withText("mmHg")).check(matches(isDisplayed()));

        // when clicking on it, the full text is not displayed
        onView(withText("mmHg")).perform(click());
        onView(withText("Millimeter of mercury")).check(doesNotExist());
    }
}
