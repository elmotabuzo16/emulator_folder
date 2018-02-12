package com.vitalityactive.va.myhealth.healthattributes;


import com.vitalityactive.va.myhealth.dto.AttributeDTO;
import com.vitalityactive.va.myhealth.dto.HealthInformationSectionDTO;

public interface MyHealthAttributeDetailInteractor {


    HealthInformationSectionDTO getHealthAttributeSectionDTOByTypeKey(long typeKey);

    AttributeDTO getHealthAttributeBySectionTypeKeyAndAttributeTypeKey(int sectionTypeKey, int attributeTypeKey);
}
