package com.vitalityactive.va.snv.onboarding.partners;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.snv.partners.SnvParticipatingPartnersActivity;
import com.vitalityactive.va.snv.partners.SnvParticipatingPartnersDetailActivity;
import com.vitalityactive.va.testutilities.VitalityActive;
import com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by edinel.k.f.degollado on 1/22/2018.
 */

public class SnvParticipatingPartnersActivityTest {
    @Rule
    public TestName testName;

    @Before
    public void setUp() {
        testName = new TestName();
        TestHarness.Settings.useMockService = true;
        TestHarness.setup(testName.getMethodName())
                .clearEverythingLikeAFreshInstall()
                .withLoggedInUser();
    }

    @After
    public void tearDown() {
        TestHarness.tearDown();
    }

    private void loadSnvParticipatingPartnersActivity() {
        TestHarness.launchActivity(SnvParticipatingPartnersActivity.class);

    }

    @Test
    public void canLoadPage() {
        loadSnvParticipatingPartnersActivity();
        TestHarness.waitForLoadingIndicatorToNotDisplay();
        onView(withId(R.id.snvPartnerOverviewTitleTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.snvPartnerOverviewDescriptionTextView)).check(matches(isDisplayed()));
        onView(withText(R.string.SV_partner_title_1029)).check(matches(isDisplayed()));
        onView(withId(R.id.snvPartnersRecyclerView)).check(matches(isDisplayed()));
    }
}
