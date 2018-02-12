package com.vitalityactive.va.myhealth.vitalityage;

import android.content.Intent;
import android.support.annotation.CallSuper;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;

import com.vitalityactive.va.R;
import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.myhealth.BaseTests;
import com.vitalityactive.va.myhealth.dto.MyHealthRepositoryImpl;
import com.vitalityactive.va.myhealth.learnmore.VitalityAgeLearnMoreActivity;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork;
import com.vitalityactive.va.testutilities.matchers.RecyclerViewMatcher;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@UITestWithMockedNetwork
public class VitalityAgeLearnMoreActivityTest extends BaseTests {

    @Rule
    public TestName name = new TestName();

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Before
    @CallSuper
    public void setUp() throws IllegalAccessException, InstantiationException, IOException {
        super.setUp();
        persistHealthAttributes("myhealth/health_information_response.json");
    }

    @Test
    public void learn_more_content_is_correct_for_vitalityage_toohigh() {
        String expectedVitalityAgeValue = "35.23";
        String expectedVariance = "4";
        String expectedContent = getString(R.string.my_health_learn_more_too_high_content_753, expectedVariance);
        Intent intent = new Intent();
        intent.putExtra(VitalityAgeLearnMoreActivity.VITALITY_AGE_EFFECTIVE_TYPE, VitalityAgeConstants.VA_ABOVE);
        intent.putExtra(VitalityAgeLearnMoreActivity.VITALITY_AGE_VARIANCE, expectedVariance);
        intent.putExtra(VitalityAgeLearnMoreActivity.VITALITY_AGE_VALUE, expectedVitalityAgeValue);
        TestHarness.launchActivity(VitalityAgeLearnMoreActivity.class, intent);
        shows_how_vitalityage_calculated_section();
        onView(withText(expectedContent)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_learn_more_how_do_I_lower_title_754)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_learn_more_how_do_I_lower_content_755)).check(matches(isDisplayed()));
    }

    @Test
    public void learn_more_content_is_correct_for_vitalityage_below() {
        String expectedVitalityAgeValue = "35.23";
        String expectedVariance = "4";
        String expectedContent = getString(R.string.my_health_vitality_age_younger_description_long_631, expectedVariance);
        Intent intent = new Intent();
        intent.putExtra(VitalityAgeLearnMoreActivity.VITALITY_AGE_EFFECTIVE_TYPE, VitalityAgeConstants.VA_BELOW);
        intent.putExtra(VitalityAgeLearnMoreActivity.VITALITY_AGE_VARIANCE, expectedVariance);
        intent.putExtra(VitalityAgeLearnMoreActivity.VITALITY_AGE_VALUE, expectedVitalityAgeValue);
        TestHarness.launchActivity(VitalityAgeLearnMoreActivity.class, intent);
        onView(withText(expectedContent)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_learn_more_good_title_756)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_learn_more_how_do_I_maintain_title_758)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_learn_more_how_do_I_maintain_content_759)).check(matches(isDisplayed()));
    }

    @Test
    public void learn_more_content_is_correct_for_not_enough_data() {
        Intent intent = new Intent();
        intent.putExtra(VitalityAgeLearnMoreActivity.VITALITY_AGE_EFFECTIVE_TYPE, VitalityAgeConstants.VA_NOT_ENOUGH_DATA);
        TestHarness.launchActivity(VitalityAgeLearnMoreActivity.class, intent);
        shows_how_vitalityage_calculated_section();
        onView(withText(R.string.my_health_learn_more_title_744)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_learn_more_subtitle_745)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_learn_more_find_your_vitality_age_title_748)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_learn_more_find_your_vitality_age_content_749)).check(matches(isDisplayed()));
        onView(withId(R.id.main_recyclerview)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText(R.string.my_health_learn_more_understand_your_health_title_750)).check(matches(isDisplayingAtLeast(1)));
        onView(withText(R.string.my_health_learn_more_understand_your_health_content_751)).check(matches(isDisplayingAtLeast(1)));
    }

    @Test
    public void learn_more_content_is_correct_for_unknown() {
        Intent intent = new Intent();
        intent.putExtra(VitalityAgeLearnMoreActivity.VITALITY_AGE_EFFECTIVE_TYPE, VitalityAgeConstants.VA_UNKNOWN);
        TestHarness.launchActivity(VitalityAgeLearnMoreActivity.class, intent);
        shows_how_vitalityage_calculated_section();
        onView(withText(R.string.my_health_learn_more_title_744)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_learn_more_subtitle_745)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_learn_more_find_your_vitality_age_title_748)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_learn_more_find_your_vitality_age_content_749)).check(matches(isDisplayed()));
        onView(withId(R.id.main_recyclerview)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText(R.string.my_health_learn_more_understand_your_health_title_750)).check(matches(isDisplayingAtLeast(1)));
        onView(withText(R.string.my_health_learn_more_understand_your_health_content_751)).check(matches(isDisplayingAtLeast(1)));
    }

    @Test
    public void learn_more_content_is_correct_for_outdated_where_feedback_history_not_present() throws Exception {
        Intent intent = new Intent();
        intent.putExtra(VitalityAgeLearnMoreActivity.VITALITY_AGE_EFFECTIVE_TYPE, VitalityAgeConstants.VA_OUTDATED);
        TestHarness.launchActivity(VitalityAgeLearnMoreActivity.class, intent);
        onView(withText(R.string.my_health_learn_more_outdated_title)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_learn_more_find_your_vitality_age_title_748)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_learn_more_find_your_vitality_age_content_749)).check(matches(isDisplayed()));
        onView(withId(R.id.main_recyclerview)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText(R.string.my_health_learn_more_understand_your_health_title_750)).check(matches(isDisplayingAtLeast(1)));
        onView(withText(R.string.my_health_learn_more_understand_your_health_content_751)).check(matches(isDisplayingAtLeast(1)));
        shows_how_vitalityage_calculated_section();
    }

    private void shows_how_vitalityage_calculated_section() {
        onView(withId(R.id.main_recyclerview)).perform(RecyclerViewActions.scrollToPosition(0));
        onView(withText(R.string.my_health_learn_more_how_is_this_calculated_title_746)).check(matches(isDisplayed()));
        onView(withText(R.string.my_health_learn_more_how_is_this_calculated_content_747)).check(matches(isDisplayed()));
    }

    private void persistHealthAttributes(String file) throws IOException {
        DataStore dataStore = TestHarness.setup().getDefaultDataStore();
        HealthAttributeInformationResponse response = RepositoryTestBase.getResponse(HealthAttributeInformationResponse.class, file);
        new MyHealthRepositoryImpl(dataStore).persistHealthAttributeTipResponse(response);
    }

    private String getString(int resourceId, String var) {
        if (var != null) {
            return InstrumentationRegistry.getTargetContext().getResources().getString(resourceId, var);
        } else {
            return InstrumentationRegistry.getTargetContext().getResources().getString(resourceId);
        }

    }
}
