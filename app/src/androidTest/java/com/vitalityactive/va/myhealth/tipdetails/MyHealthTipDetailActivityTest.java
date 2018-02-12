package com.vitalityactive.va.myhealth.tipdetails;

import android.content.Intent;
import android.support.annotation.CallSuper;
import android.support.test.espresso.contrib.RecyclerViewActions;

import com.vitalityactive.va.R;
import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.myhealth.BaseTests;
import com.vitalityactive.va.myhealth.dto.MyHealthRepositoryImpl;
import com.vitalityactive.va.myhealth.moretips.MyHealthMoreTipsActivity;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.myhealth.tipdetail.MyHealthTipDetailActivity;
import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;
import com.vitalityactive.va.persistence.DataStore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.vitalityactive.va.cucumber.utils.TestHarness.shortDelay;

public class MyHealthTipDetailActivityTest extends BaseTests {


    @Rule
    public TestName name = new TestName();

    @Before
    @CallSuper
    public void setUp() throws IOException, InstantiationException, IllegalAccessException {
        super.setUp();
    }

    @Test
    public void can_launch_tipdetail_screen() {
        Intent intent = new Intent();
        intent.putExtra(MyHealthTipDetailActivity.TIP_TYPE_KEY, 8);
        intent.putExtra(MyHealthTipDetailActivity.TIP_CODE, "test");
        TestHarness.launchActivity(MyHealthTipDetailActivity.class, intent);
        shortDelay(10000);
    }

    @Test
    public void can_navigate_to_tip_detail_from_moretipsscreen() throws Exception {
        persistHealthAttributes("myhealth/health_information_response.json");
        Intent intent = new Intent();
        intent.putExtra(MyHealthMoreTipsActivity.SECTION_TYPE_KEY, VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_BAD.intValue());
        intent.putExtra(MyHealthMoreTipsActivity.ATTRIBUTE_TYPE_KEY, 8);
        TestHarness.launchActivity(MyHealthMoreTipsActivity.class, intent);
        shortDelay(10000);
        onView(withId(R.id.main_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0, scrollTo()));
        shortDelay(5000);
        onView(withId(R.id.feedback_tip_detail)).check(matches(isDisplayed()));
    }


    private void persistHealthAttributes(String file) throws Exception {
        DataStore dataStore = TestHarness.setup().getDefaultDataStore();
        HealthAttributeInformationResponse response = RepositoryTestBase.getResponse(HealthAttributeInformationResponse.class, file);
        new MyHealthRepositoryImpl(dataStore).persistHealthAttributeTipResponse(response);
    }
}
