package com.vitalityactive.va.myhealth.vitalityageprofile;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;

import com.vitalityactive.va.R;
import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.myhealth.BaseTests;
import com.vitalityactive.va.myhealth.dto.MyHealthRepositoryImpl;
import com.vitalityactive.va.myhealth.moretips.MyHealthMoreTipsActivity;
import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.vitalityactive.va.cucumber.utils.TestHarness.shortDelay;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.notNullValue;

@UITestWithMockedNetwork
public class MyHealthVitalityAgeProfileActivityTest extends BaseTests {

    @Before
    public void setUp() throws InstantiationException, IllegalAccessException, IOException {
        super.setUp();
        MockNetworkHandler.enqueueResponseFromFile(200, "myhealth/health_information_response.json");
    }


    @Test
    public void can_populate_all_sections_of_profile_screen() throws Exception {
        persistHealthAttributes("myhealth/health_information_response_with_vitality_age.json");
        TestHarness.launchActivity(MyHealthVitalityAgeProfileActivity.class);
        shortDelay(10000);
        onView(allOf(withId(R.id.vitality_age_value), notNullValue()));
        shortDelay(30000);
        onView(allOf(withId(R.id.vitality_age_card))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.vitality_age_profile_tips_card))).check(matches(isDisplayed()));
    }

    @Test
    public void can_navigate_to_vitality_age_profile() throws Exception {
        loadVitalityAgeProfileScreen();
        shortDelay(10000);
        onView(allOf(withId(R.id.section_title), notNullValue()));
        onView(allOf(withId(R.id.section_icon), notNullValue()));
        onView(allOf(withId(R.id.myhealth_vitalityage_profile_item1), notNullValue()));
        onView(allOf(withId(R.id.myhealth_vitalityage_profile_item2), notNullValue()));
        onView(allOf(withId(R.id.myhealth_vitalityage_profile_feedback_name), notNullValue()));
        onView(allOf(withId(R.id.myhealth_vitalityage_whattodo_title), notNullValue()));
        onView(allOf(withId(R.id.myhealth_vitalityage_whattodo), notNullValue()));
    }

    @Test
    public void can_navigate_to_lean_more() throws Exception{
        persistHealthAttributes("myhealth/health_information_response_with_vitality_age.json");
        TestHarness.launchActivity(MyHealthVitalityAgeProfileActivity.class);
        shortDelay(10000);
        onView(allOf(withId(R.id.vitality_age_value), notNullValue()));
        shortDelay(3000);
        onView(withId(R.id.vitality_age_learnmore)).perform(click());
        shortDelay(3000);
        onView(withId(R.id.activity_learn_more)).check(matches(isDisplayed()));
    }

    private void persistHealthAttributes(String file) throws IOException {
        DataStore dataStore = TestHarness.setup().getDefaultDataStore();
        HealthAttributeInformationResponse response = RepositoryTestBase.getResponse(HealthAttributeInformationResponse.class, file);
        new MyHealthRepositoryImpl(dataStore).persistHealthAttributeTipResponse(response);
    }

    @Test
    public void should_load_more_tips() throws Exception {
        persistHealthAttributes("myhealth/health_information_response.json");
        TestHarness.launchActivity(MyHealthMoreTipsActivity.class);
    }

    @Test
    public void can_navigate_to_more_tips_screen_from_more_results_button() throws Exception {
        loadVitalityAgeProfileScreen();
        shortDelay(10000);
        onView(allOf(withId(R.id.section_title), notNullValue()));
        onView(allOf(withId(R.id.myhealth_vitalityage_profile_item1), notNullValue()));
        onView(allOf(withId(R.id.myhealth_vitalityage_profile_item2), notNullValue()));
        onView(allOf(withId(R.id.myhealth_vitalityage_profile_feedback_name), notNullValue()));
        onView(allOf(withId(R.id.myhealth_vitalityage_whattodo_title), notNullValue()));
        onView(allOf(withId(R.id.myhealth_vitalityage_whattodo), notNullValue()));
        onView(ViewMatchers.withId(R.id.main_recyclerview)).perform(ViewActions.swipeUp());
        shortDelay(4000);
        onView(withIndex(withId(R.id.more_tips), 1)).perform(click());
        shortDelay(5000);
        onView(withId(R.id.activity_more_tips)).check(matches(isDisplayed()));
    }


}
