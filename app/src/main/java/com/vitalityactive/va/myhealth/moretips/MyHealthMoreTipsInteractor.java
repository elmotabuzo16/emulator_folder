package com.vitalityactive.va.myhealth.moretips;

import com.vitalityactive.va.myhealth.dto.AttributeDTO;

public interface MyHealthMoreTipsInteractor {

    AttributeDTO getHealthAttributeBySectionTypeKeyAndAttributeTypeKey(int sectionTypeKey, int attributeTypeKey);
}
