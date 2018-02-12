package com.vitalityactive.va.myhealth.vitalityageprofile;

import com.vitalityactive.va.myhealth.dto.HealthInformationSectionDTO;
import com.vitalityactive.va.myhealth.dto.MyHealthRepository;
import com.vitalityactive.va.myhealth.dto.VitalityAgeHealthAttributeDTO;

public class MyHealthVitalityAgeProfileInteractorImpl implements MyHealthVitalityAgeProfileInteractor {

    private final MyHealthRepository myHealthRepository;

    public MyHealthVitalityAgeProfileInteractorImpl(MyHealthRepository myHealthRepository) {
        this.myHealthRepository = myHealthRepository;
    }

    @Override
    public VitalityAgeHealthAttributeDTO getVitalityAgeHealthAttribute() {
        return myHealthRepository.getVitalityAgeHealthAttributeFeedback();
    }

    @Override
    public HealthInformationSectionDTO getHealthAttributeSectionDTOByTypeKey(long typeKey) {
        return myHealthRepository.getHealthInformationSectionByTypeKey(typeKey);
    }

    @Override
    public boolean healthinformationSectionHasSubsections(long typeKey) {
        return myHealthRepository.sectionHasSubsection(typeKey);
    }

}
