package com.vitalityactive.va.myhealth.healthattributes;

import com.vitalityactive.va.myhealth.dto.AttributeDTO;
import com.vitalityactive.va.myhealth.dto.HealthInformationSectionDTO;
import com.vitalityactive.va.myhealth.dto.MyHealthRepository;

public class MyHealthAttributeDetailInteractorImpl implements MyHealthAttributeDetailInteractor {

    MyHealthRepository myHealthRepository;

    public MyHealthAttributeDetailInteractorImpl(MyHealthRepository myHealthRepository) {
        this.myHealthRepository = myHealthRepository;
    }


    @Override
    public HealthInformationSectionDTO getHealthAttributeSectionDTOByTypeKey(long typeKey) {
        return myHealthRepository.getHealthInformationSectionByTypeKey(typeKey);
    }

    @Override
    public AttributeDTO getHealthAttributeBySectionTypeKeyAndAttributeTypeKey(int sectionTypeKey, int attributeTypeKey) {
        return myHealthRepository.getHealthAttributeBySectionTypeKeyAndAttributeTypeKey(sectionTypeKey, attributeTypeKey);
    }

}
