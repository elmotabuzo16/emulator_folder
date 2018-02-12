package com.vitalityactive.va.myhealth.vitalityage;


import android.content.Intent;
import android.support.annotation.CallSuper;
import com.vitalityactive.va.R;
import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.myhealth.dto.MyHealthRepositoryImpl;
import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
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
public class VitalityAgeActivityTest {

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
    public void can_load_vitality_age_on_view_when_present() throws Exception {
        persistHealthAttributes("myhealth/health_information_response_with_vitality_age.json");
        Intent intent = new Intent();
        intent.putExtra(VitalityAgeActivity.MODE, VitalityAgeActivity.VHR_MODE);
        TestHarness.launchActivity(VitalityAgeActivity.class, intent);
        shortDelay(5000);
        onView(allOf(withId(R.id.vitality_age_value), notNullValue()));
        onView(allOf(withId(R.id.vitality_age_summary), notNullValue()));
        onView(allOf(withId(R.id.vitality_age_explanation), notNullValue()));
    }

    @Test
    public void can_load_vitality_age_notenough_data_on_view_when_present() throws Exception {
      //  persistHealthAttributes("myhealth/health_information_response_with_vitality_age_notenough_data.json");
        Intent intent = new Intent();
        intent.putExtra(VitalityAgeActivity.MODE, VitalityAgeActivity.VHR_MODE);
        TestHarness.launchActivity(VitalityAgeActivity.class, intent);
        shortDelay(5000);
        onView(allOf(withId(R.id.vitality_age_value), notNullValue()));
        onView(allOf(withId(R.id.vitality_age_summary), notNullValue()));
        onView(allOf(withId(R.id.vitality_age_explanation), notNullValue()));
    }

    @Test
    public void can_handle_vitality_age_display_when_not_present() throws Exception {
        MockNetworkHandler.enqueueResponseFromFile(200, "capture_assessment_response.json");
        Intent intent = new Intent();
        intent.putExtra(VitalityAgeActivity.MODE, VitalityAgeActivity.VHR_MODE);
        TestHarness.launchActivity(VitalityAgeActivity.class, intent);
        shortDelay(5000);
        onView(allOf(withId(R.id.vitality_age_value), notNullValue()));
        onView(allOf(withId(R.id.vitality_age_summary), notNullValue()));
        onView(allOf(withId(R.id.vitality_age_explanation), notNullValue()));
    }

    @Test
    public void can_navigate_to_learnmore() throws Exception {
        persistHealthAttributes("capture_assessment_response.json");
        Intent intent = new Intent();
        intent.putExtra(VitalityAgeActivity.MODE, VitalityAgeActivity.VHR_MODE);
        TestHarness.launchActivity(VitalityAgeActivity.class, intent);
        shortDelay(5000);
        onView(withId(R.id.vitality_age_learnmore)).perform(click());
        shortDelay(3000);
        onView(withId(R.id.activity_learn_more)).check(matches(isDisplayed()));
    }

    @Test
    public void can_load_vitality_age_on_view_when_present_in_vhrmode() throws Exception {
        persistHealthAttributes("myhealth/health_information_response_with_vitality_age.json");
        Intent intent = new Intent();
        intent.putExtra(VitalityAgeActivity.MODE, VitalityAgeActivity.VHC__DONE_VHR_PENDING_MODE);
        TestHarness.launchActivity(VitalityAgeActivity.class, intent);
        shortDelay(5000);
        onView(allOf(withId(R.id.vitality_age_value), notNullValue()));
        onView(allOf(withId(R.id.vitality_age_summary), notNullValue()));
        onView(allOf(withId(R.id.vitality_age_explanation), notNullValue()));
    }

    private void persistHealthAttributes(String file) throws IOException {
        DataStore dataStore = TestHarness.setup().getDefaultDataStore();
        HealthAttributeInformationResponse response = RepositoryTestBase.getResponse(HealthAttributeInformationResponse.class, file);
        new MyHealthRepositoryImpl(dataStore).persistHealthAttributeTipResponse(response);
    }
}
