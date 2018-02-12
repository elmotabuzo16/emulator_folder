package com.vitalityactive.va.vhc;


import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.ProductFeatureType;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork;
import com.vitalityactive.va.vhc.landing.VHCLandingActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@MediumTest
@UITestWithMockedNetwork
public class HealthCareBenefitTests {
    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() {
        TestHarness.Settings.useMockService = true;

        TestHarness.setup(testName.getMethodName())
                .clearEverythingLikeAFreshInstall()
                .withLoggedInUser();

        MockNetworkHandler.enqueueResponseFromFile(200, "vhc/vhc_landing_urinary_protein_response.json");
    }

    private TestHarness.MockData getVHCTemplateFeatureType() {
        com.vitalityactive.va.persistence.models.ProductFeature feature = new com.vitalityactive.va.persistence.models.ProductFeature();
        feature.setFeatureType(ProductFeatureType._VHCTEMPLATE);
        TestHarness.MockData data = new TestHarness.MockData();
        data.datum.add(feature);

        return data;
    }

    @After
    public void tearDown() {
        TestHarness.tearDown();
    }

    @Test
    public void health_care_menu_item_is_shown_if_present_in_config() throws Exception {
        TestHarness.setup().addData(getVHCTemplateFeatureType());

        TestHarness.launchActivity(VHCLandingActivity.class);

        onView(withText("Learn More")).check(matches(isDisplayed()));
        onView(withText(R.string.landing_screen_healthcare_pdf_label_248)).check(matches(isDisplayed()));
    }

    @Test
    public void health_care_menu_item_is_not_shown_if_not_present_in_config() throws Exception {
        TestHarness.launchActivity(VHCLandingActivity.class);

        TestHarness.waitForLoadingIndicatorToNotDisplay();

        onView(withText("Learn More")).check(matches(isDisplayed()));
        onView(withText(R.string.landing_screen_healthcare_pdf_label_248)).check(doesNotExist());
    }
}
