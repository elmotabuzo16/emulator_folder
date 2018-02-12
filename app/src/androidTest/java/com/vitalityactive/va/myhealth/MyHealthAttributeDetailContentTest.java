package com.vitalityactive.va.myhealth;


import android.support.annotation.CallSuper;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.myhealth.dto.AttributeDTO;
import com.vitalityactive.va.myhealth.dto.MyHealthRepositoryImpl;
import com.vitalityactive.va.myhealth.healthattributes.MyHealthAttributeDetailInteractorImpl;
import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import java.io.IOException;

import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class MyHealthAttributeDetailContentTest {

    @Rule
    public TestName name = new TestName();
    MyHealthAttributeDetailInteractorImpl myHealthAttributeDetailInteractorImpl;
    MyHealthRepositoryImpl myHealthRepository;

    @Before
    @CallSuper
    public void setUp() throws IOException {
        TestHarness.Settings.useMockService = true;
        TestHarness.setup(name.getMethodName())
                .clearEverythingLikeAFreshInstall()
                .withLoggedInUser();
        myHealthRepository = new MyHealthRepositoryImpl(TestHarness.setup().getDefaultDataStore());
        myHealthAttributeDetailInteractorImpl = new MyHealthAttributeDetailInteractorImpl(myHealthRepository);
    }

    @Test
    public void can_fetch_attribute_by_section_and_attribute_keys() throws Exception {
        persistHealthAttributes();
        AttributeDTO attribute = myHealthAttributeDetailInteractorImpl.getHealthAttributeBySectionTypeKeyAndAttributeTypeKey(4, 8);
        assertNotNull(attribute);
    }


    private void persistHealthAttributes() throws Exception {
        HealthAttributeInformationResponse response = RepositoryTestBase.getResponse(HealthAttributeInformationResponse.class, "myhealth/health_information_response.json");
        myHealthRepository.persistHealthAttributeTipResponse(response);
    }
}
