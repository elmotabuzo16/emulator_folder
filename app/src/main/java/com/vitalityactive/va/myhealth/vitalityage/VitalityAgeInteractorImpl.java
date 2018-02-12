package com.vitalityactive.va.myhealth.vitalityage;

import com.vitalityactive.va.myhealth.dto.MyHealthRepository;
import com.vitalityactive.va.myhealth.dto.VitalityAgeHealthAttributeDTO;
import com.vitalityactive.va.myhealth.dto.VitalityAgeRepository;
import com.vitalityactive.va.myhealth.entity.VitalityAge;

public class VitalityAgeInteractorImpl implements VitalityAgeInteractor {

    private final MyHealthRepository myHealthRepository;

    private final VitalityAgeRepository vitalityAgeRepository;

    public VitalityAgeInteractorImpl(VitalityAgeRepository vitalityAgeRepository, MyHealthRepository myHealthRepository) {
        this.myHealthRepository = myHealthRepository;
        this.vitalityAgeRepository = vitalityAgeRepository;
    }


    @Override
    public VitalityAgeHealthAttributeDTO getVitalityAgeHealthAttribute() {
        return myHealthRepository.getVitalityAgeHealthAttributeFeedback();
    }

    @Override
    public VitalityAge getPersistedVitalityAge() {
        return vitalityAgeRepository.getVitalityAge();
    }

}
