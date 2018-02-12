package com.vitalityactive.va.myhealth.landing;


import com.vitalityactive.va.myhealth.dto.MyHealthRepository;
import com.vitalityactive.va.myhealth.dto.VitalityAgeHealthAttributeDTO;

public class MyHealthLandingInteractorImpl implements MyHealthLandingInteractor {

    private final MyHealthRepository myHealthRepository;

    public MyHealthLandingInteractorImpl(MyHealthRepository myHealthRepository) {
        this.myHealthRepository = myHealthRepository;
    }


    @Override
    public VitalityAgeHealthAttributeDTO getVitalityAgeHealthAttribute() {
        return myHealthRepository.getVitalityAgeHealthAttributeFeedback();
    }
}
