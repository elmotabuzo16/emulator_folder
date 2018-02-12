package com.vitalityactive.va.vhc;

import android.support.test.InstrumentationRegistry;

import com.vitalityactive.va.R;
import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.UnitsOfMeasure;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.login.LoginRepositoryImpl;
import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.register.entity.Username;
import com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork;
import com.vitalityactive.va.vhc.captureresults.models.CapturedGroup;
import com.vitalityactive.va.vhc.captureresults.models.GroupType;
import com.vitalityactive.va.vhc.landing.VHCLandingActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@UITestWithMockedNetwork
public class BloodPressureTests {
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

    private void goToCaptureActivity() {
        onView(withText(R.string.onboarding_section_2_title_128)).perform(click());
    }

    private void persistLoginResponse() throws java.io.IOException {
        DataStore dataStore = TestHarness.setup().getDefaultDataStore();
        LoginServiceResponse response = RepositoryTestBase.getResponse(LoginServiceResponse.class, "login/Login_successful_with_limit_vhc_features.json");
        new LoginRepositoryImpl(dataStore, new AppConfigRepository(dataStore, InstrumentationRegistry.getTargetContext()))
                .persistLoginResponse(response, new Username("Frikkie@Poggenpoel.com"));
    }

    @Test
    public void systolic_and_diastolic_have_different_ranges() {
        setupCapturedBloodPressure();
        goToCaptureActivity();

        onView(withText("Range 40 - 240")).check(matches(isDisplayed()));
        onView(withText("Range 20 - 160")).check(matches(isDisplayed()));
    }

    private void setupCapturedBloodPressure() {
        CapturedGroup bloodPressure = new CapturedGroup(GroupType.BLOOD_PRESSURE, "Blood Pressure");
        bloodPressure.addCapturedField(String.valueOf(EventType._SYSTOLICPRESSURE))
                .setPrimaryValue(1f, true)
                .setSelectedUnitOfMeasure(UnitsOfMeasure.SYSTOLIC_MILLIMETER_OF_MERCURY)
                .setDateTested(123);
        bloodPressure.addCapturedField(String.valueOf(EventType._DIASTOLICPRESSURE))
                .setPrimaryValue(2f, true)
                .setSelectedUnitOfMeasure(UnitsOfMeasure.SYSTOLIC_MILLIMETER_OF_MERCURY)
                .setDateTested(123);

        TestHarness.MockData mockData = new TestHarness.MockData();
        mockData.datum.add(bloodPressure);
        TestHarness.setup().addData(mockData);
    }
}
