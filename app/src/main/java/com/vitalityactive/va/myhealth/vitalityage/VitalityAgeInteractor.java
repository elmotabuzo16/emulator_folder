package com.vitalityactive.va.myhealth.vitalityage;


import com.vitalityactive.va.myhealth.dto.VitalityAgeHealthAttributeDTO;
import com.vitalityactive.va.myhealth.entity.VitalityAge;

public interface VitalityAgeInteractor {

    VitalityAgeHealthAttributeDTO getVitalityAgeHealthAttribute();

    VitalityAge getPersistedVitalityAge();
}
