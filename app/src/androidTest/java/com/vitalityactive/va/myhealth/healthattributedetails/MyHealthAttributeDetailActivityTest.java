package com.vitalityactive.va.myhealth.healthattributedetails;


import android.content.Intent;

import com.vitalityactive.va.R;
import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.myhealth.BaseTests;
import com.vitalityactive.va.myhealth.dto.MyHealthRepositoryImpl;
import com.vitalityactive.va.myhealth.healthattributes.MyHealthAttributeDetailActivity;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;
import com.vitalityactive.va.persistence.DataStore;

import org.junit.Test;

import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.cucumber.utils.TestHarness.shortDelay;
import static org.hamcrest.Matchers.not;

public class MyHealthAttributeDetailActivityTest extends BaseTests {

    @Override
    public void setUp() throws IllegalAccessException, InstantiationException, IOException {
        super.setUp();
    }

    @Test
    public void can_load_data_on_all_views() throws Exception {
        persistHealthAttributes("myhealth/health_information_response_withattributedetails.json");
        Intent intent = new Intent();
        intent.putExtra(MyHealthAttributeDetailActivity.SECTION_TYPE_KEY, VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_GOOD.intValue());
        intent.putExtra(MyHealthAttributeDetailActivity.ATTRIBUTE_TYPE_KEY, 37);
        TestHarness.launchActivity(MyHealthAttributeDetailActivity.class, intent);
        shortDelay(10000);
        onView(withText(R.string.myhealth_feedback_attribute_recommended_recent_result)).check(matches(isDisplayed()));
        onView(withId(R.id.attribute_value)).check(matches(not(withText(""))));
        common_content();
    }

    @Test
    public void can_load_data_when_result_is_bad() throws Exception {
        persistHealthAttributes("myhealth/health_information_response.json");
        Intent intent = new Intent();
        intent.putExtra(MyHealthAttributeDetailActivity.SECTION_TYPE_KEY, VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_BAD.intValue());
        intent.putExtra(MyHealthAttributeDetailActivity.ATTRIBUTE_TYPE_KEY, 8);
        TestHarness.launchActivity(MyHealthAttributeDetailActivity.class, intent);
        shortDelay(10000);
        onView(withText(R.string.myhealth_feedback_attribute_recommended_recent_result)).check(matches(isDisplayed()));
        onView(withId(R.id.attribute_value)).check(matches(not(withText(""))));
        common_content();
    }

    @Test
    public void can_load_data_when_result_is_good() throws Exception {
        persistHealthAttributes("myhealth/health_information_response.json");
        Intent intent = new Intent();
        intent.putExtra(MyHealthAttributeDetailActivity.SECTION_TYPE_KEY, VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_GOOD.intValue());
        intent.putExtra(MyHealthAttributeDetailActivity.ATTRIBUTE_TYPE_KEY, 50);
        TestHarness.launchActivity(MyHealthAttributeDetailActivity.class, intent);
        shortDelay(10000);
        onView(withText(R.string.myhealth_feedback_attribute_recommended_recent_result)).check(matches(isDisplayed()));
        onView(withId(R.id.attribute_value)).check(matches(not(withText(""))));
        common_content();
    }

    @Test
    public void can_show_placeholder_when_attribute_not_present() throws Exception {
        persistHealthAttributes("myhealth/health_information_response.json");
        Intent intent = new Intent();
        intent.putExtra(MyHealthAttributeDetailActivity.SECTION_TYPE_KEY, VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_UNKNOWN.intValue());
        intent.putExtra(MyHealthAttributeDetailActivity.ATTRIBUTE_TYPE_KEY, 42);
        TestHarness.launchActivity(MyHealthAttributeDetailActivity.class, intent);
        shortDelay(10000);
        onView(withText(R.string.myhealth_feedback_attribute_capture_result)).check(matches(isDisplayed()));
        onView(withId(R.id.feedback_placeholder)).check(matches(not(withText(""))));
        common_content();
    }

    @Test
    public void can_navigate_to_more_tips() throws Exception {
        persistHealthAttributes("myhealth/health_information_response.json");
        Intent intent = new Intent();
        intent.putExtra(MyHealthAttributeDetailActivity.SECTION_TYPE_KEY, VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_GOOD.intValue());
        intent.putExtra(MyHealthAttributeDetailActivity.ATTRIBUTE_TYPE_KEY, 50);
        TestHarness.launchActivity(MyHealthAttributeDetailActivity.class, intent);
        shortDelay(5000);
        onView(withId(R.id.main_recyclerview)).perform(swipeUp());
        onView(withId(R.id.more_tips)).check(matches(isDisplayed()));
        onView(withId(R.id.more_tips)).perform(click());
        shortDelay(5000);
        onView(withId(R.id.activity_more_tips)).check(matches(isDisplayed()));
    }

    @Test
    public void can_navigate_to_tip_detail() throws Exception {
        persistHealthAttributes("myhealth/health_information_response.json");
        Intent intent = new Intent();
        intent.putExtra(MyHealthAttributeDetailActivity.SECTION_TYPE_KEY, VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_GOOD.intValue());
        intent.putExtra(MyHealthAttributeDetailActivity.ATTRIBUTE_TYPE_KEY, 50);
        TestHarness.launchActivity(MyHealthAttributeDetailActivity.class, intent);
        shortDelay(5000);
        onView(withId(R.id.main_recyclerview)).perform(swipeUp());
        onView(withIndex(withId(R.id.feedback_tip_detail), 0)).perform(click());
        shortDelay(5000);
        onView(withId(R.id.toolbar)).check(matches(withText(getString(R.string.my_health_detail_main_title_tip_1068))));
    }

    public void common_content() {
        onView(withText(R.string.detail_screen_why_is_this_important_title_1077)).check(matches(isDisplayed()));
        onView(withId(R.id.recommended_value)).check(matches(not(withText(""))));
        onView(withId(R.id.recommendation_title)).check(matches(not(withText(""))));
        onView(withId(R.id.toolbar)).check(matches(not(withText(""))));
    }


    private void persistHealthAttributes(String file) throws Exception {
        DataStore dataStore = TestHarness.setup().getDefaultDataStore();
        HealthAttributeInformationResponse response = RepositoryTestBase.getResponse(HealthAttributeInformationResponse.class, file);
        new MyHealthRepositoryImpl(dataStore).persistHealthAttributeTipResponse(response);
    }
}
