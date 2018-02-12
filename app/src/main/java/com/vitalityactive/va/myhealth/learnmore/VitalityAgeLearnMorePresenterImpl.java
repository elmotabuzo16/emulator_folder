package com.vitalityactive.va.myhealth.learnmore;

import com.vitalityactive.va.myhealth.dto.VitalityAgeHealthAttributeDTO;
import com.vitalityactive.va.myhealth.vitalityageprofile.MyHealthVitalityAgeProfileInteractor;

public class VitalityAgeLearnMorePresenterImpl implements VitalityAgeLearnMorePresenter {

    MyHealthVitalityAgeProfileInteractor myHealthVitalityAgeProfileInteractor;

    public VitalityAgeLearnMorePresenterImpl(MyHealthVitalityAgeProfileInteractor myHealthVitalityAgeProfileInteractor) {
        this.myHealthVitalityAgeProfileInteractor = myHealthVitalityAgeProfileInteractor;
    }

    @Override
    public VitalityAgeHealthAttributeDTO getVitalityAge() {
        return myHealthVitalityAgeProfileInteractor.getVitalityAgeHealthAttribute();
    }

}
