package com.vitalityactive.va.myhealth;


import android.support.annotation.CallSuper;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.R;
import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.myhealth.dto.MyHealthRepositoryImpl;
import com.vitalityactive.va.myhealth.vitalityageprofile.MyHealthVitalityAgeProfileActivity;
import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;
import com.vitalityactive.va.persistence.DataStore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.cucumber.utils.TestHarness.shortDelay;
import static com.vitalityactive.va.myhealth.BaseTests.withIndex;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MyHealthVitalityAgeProfileViewholderTest {

    @Rule
    public TestName name = new TestName();

    @Before
    @CallSuper
    public void setUp() throws IOException {
        TestHarness.Settings.useMockService = true;
        TestHarness.setup(name.getMethodName())
                .clearEverythingLikeAFreshInstall()
                .withLoggedInUser();
    }

    @Test
    public void can_load_placeholder_for_whattoimprove_section() throws Exception {
        persistHealthAttributes("myhealth/health_information_empty_whattoimprove_section_response.json");
        TestHarness.launchActivity(MyHealthVitalityAgeProfileActivity.class);
        shortDelay(10000);
        onView(withId(R.id.main_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(1, scrollTo()));
        onView(withIndex(withId(R.id.feedback_content_view_placeholder), 0)).check(matches(withText(containsString(getString(R.string.feedback_tips_whattoimprove_missing)))));
        hidden_content(1);
    }

    @Test
    public void can_load_placeholder_for_whatyouaredoingwell_section() throws Exception {
        persistHealthAttributes("myhealth/health_information_empty_whatyouaredoingwell_section_response.json");
        TestHarness.launchActivity(MyHealthVitalityAgeProfileActivity.class);
        shortDelay(10000);
        onView(withId(R.id.main_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(2, scrollTo()));
        onView(withIndex(withId(R.id.feedback_content_view_placeholder), 1)).check(matches(withText(containsString(getString(R.string.feedback_tips_whatdoingwell_missing)))));
        hidden_content(2);
    }

    @Test
    public void can_load_placeholder_for_whatyouhavenotprovided_section() throws Exception {
        persistHealthAttributes("myhealth/health_information_empty_whatnotprovided_section_response.json");
        TestHarness.launchActivity(MyHealthVitalityAgeProfileActivity.class);
        shortDelay(10000);
        onView(withId(R.id.main_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(3, scrollTo()));
        onView(withIndex(withId(R.id.feedback_content_view_placeholder), 2)).check(matches(withText(containsString(getString(R.string.feedback_tips_whatnotprovided_missing)))));
        hidden_content(3);
    }

    private void hidden_content(int recyclerIndex) {
        onView(withId(R.id.main_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(recyclerIndex, scrollTo()));
        onView(withIndex(withId(R.id.more_tips), recyclerIndex - 1)).check(matches(not(isDisplayed())));
    }

    private void persistHealthAttributes(String file) throws IOException {
        DataStore dataStore = TestHarness.setup().getDefaultDataStore();
        HealthAttributeInformationResponse response = RepositoryTestBase.getResponse(HealthAttributeInformationResponse.class, file);
        new MyHealthRepositoryImpl(dataStore).persistHealthAttributeTipResponse(response);
    }

    private String getString(int resourceId) {
        return InstrumentationRegistry.getTargetContext().getResources().getString(resourceId);
    }

}
