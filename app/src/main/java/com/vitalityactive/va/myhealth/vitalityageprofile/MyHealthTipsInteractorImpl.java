package com.vitalityactive.va.myhealth.vitalityageprofile;


import com.vitalityactive.va.myhealth.dto.HealthInformationSectionDTO;
import com.vitalityactive.va.myhealth.dto.MyHealthRepository;

import java.util.List;

public class MyHealthTipsInteractorImpl implements MyHealthTipsInteractor {

    private final MyHealthRepository myHealthRepository;

    public MyHealthTipsInteractorImpl(MyHealthRepository myHealthRepository) {
        this.myHealthRepository = myHealthRepository;
    }

    @Override
    public List<HealthInformationSectionDTO> getHealthAttributeInformationByParentTypeKey(int parentTypekey) {
        return myHealthRepository.getHealthInformationSectionByParentTypeKey(parentTypekey);
    }

    @Override
    public boolean healthinformationSectionHasSubsections(int typeKey) {
        return myHealthRepository.sectionHasSubsection(typeKey);
    }
}
