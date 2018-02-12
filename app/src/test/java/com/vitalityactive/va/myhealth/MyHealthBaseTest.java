package com.vitalityactive.va.myhealth;

import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.myhealth.dto.VitalityAgeHealthAttributeDTO;
import com.vitalityactive.va.networking.model.HealthAttributeFeedbackResponse;
import com.vitalityactive.va.persistence.models.myhealth.VitalityAgeHealthAttribute;
import com.vitalityactive.va.persistence.models.vhc.HealthAttributeFeedback;

import java.util.Collections;

import static org.mockito.Mockito.mock;

public class MyHealthBaseTest extends BaseTest {


    public VitalityAgeHealthAttributeDTO createHealthAttributeResponse() {
        HealthAttributeFeedbackResponse.HealthAttributeFeedback healthAttributeFeedback = mock(HealthAttributeFeedbackResponse.HealthAttributeFeedback.class);
        HealthAttributeFeedbackResponse.HealthAttribute healthAttribute = new HealthAttributeFeedbackResponse.HealthAttribute();
        healthAttribute.healthAttributeFeedbacks = Collections.singletonList(healthAttributeFeedback);
        VitalityAgeHealthAttribute vitalityAgeHealthAttribute = new VitalityAgeHealthAttribute(healthAttribute);
        vitalityAgeHealthAttribute.addHealthAttributeFeedbacks(Collections.singletonList(new HealthAttributeFeedback()));
        return new VitalityAgeHealthAttributeDTO(vitalityAgeHealthAttribute);
    }
}
