package com.vitalityactive.va.myhealth.vitalityageprofile;

import com.vitalityactive.va.myhealth.dto.HealthInformationSectionDTO;
import com.vitalityactive.va.myhealth.dto.VitalityAgeHealthAttributeDTO;

public interface MyHealthVitalityAgeProfileInteractor {

    VitalityAgeHealthAttributeDTO getVitalityAgeHealthAttribute();

    HealthInformationSectionDTO getHealthAttributeSectionDTOByTypeKey(long typeKey);

    boolean healthinformationSectionHasSubsections(long typeKey);
}
