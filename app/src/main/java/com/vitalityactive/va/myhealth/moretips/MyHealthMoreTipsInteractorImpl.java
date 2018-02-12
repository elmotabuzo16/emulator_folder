package com.vitalityactive.va.myhealth.moretips;

import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.myhealth.dto.AttributeDTO;
import com.vitalityactive.va.myhealth.dto.MyHealthRepository;

public class MyHealthMoreTipsInteractorImpl implements MyHealthMoreTipsInteractor {
    MyHealthRepository myHealthRepository;
    EventDispatcher eventDispatcher;

    public MyHealthMoreTipsInteractorImpl(MyHealthRepository myHealthRepository) {
        this.myHealthRepository = myHealthRepository;
    }


    @Override
    public AttributeDTO getHealthAttributeBySectionTypeKeyAndAttributeTypeKey(int sectionTypeKey, int attributeTypeKey) {
        return myHealthRepository.getHealthAttributeBySectionTypeKeyAndAttributeTypeKey(sectionTypeKey, attributeTypeKey);
    }
}
