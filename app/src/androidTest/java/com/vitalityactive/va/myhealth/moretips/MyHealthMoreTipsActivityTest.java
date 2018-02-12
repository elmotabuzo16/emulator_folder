package com.vitalityactive.va.myhealth.moretips;

import android.content.Intent;
import android.support.annotation.CallSuper;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.myhealth.BaseTests;
import com.vitalityactive.va.myhealth.dto.MyHealthRepositoryImpl;
import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;
import com.vitalityactive.va.persistence.DataStore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import java.io.IOException;

import static com.vitalityactive.va.cucumber.utils.TestHarness.shortDelay;

@RunWith(AndroidJUnit4.class)
public class MyHealthMoreTipsActivityTest extends BaseTests {

    @Rule
    public TestName name = new TestName();

    @Before
    @CallSuper
    public void setUp() throws IOException, InstantiationException, IllegalAccessException {
        super.setUp();
    }

    @Test
    public void can_launch_tipdetail_screen() throws Exception {
        persistHealthAttributes("myhealth/health_information_response_withattributedetails.json");
        Intent intent = new Intent();
        intent.putExtra(MyHealthMoreTipsActivity.SECTION_TYPE_KEY, 8);
        intent.putExtra(MyHealthMoreTipsActivity.ATTRIBUTE_TYPE_KEY, 50);
        TestHarness.launchActivity(MyHealthMoreTipsActivity.class, intent);
        shortDelay(10000);
    }

    private void persistHealthAttributes(String file) throws Exception {
        DataStore dataStore = TestHarness.setup().getDefaultDataStore();
        HealthAttributeInformationResponse response = RepositoryTestBase.getResponse(HealthAttributeInformationResponse.class, file);
        new MyHealthRepositoryImpl(dataStore).persistHealthAttributeTipResponse(response);
    }
}
