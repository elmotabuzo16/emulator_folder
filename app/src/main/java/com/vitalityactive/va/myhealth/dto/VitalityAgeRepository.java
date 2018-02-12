package com.vitalityactive.va.myhealth.dto;


import com.vitalityactive.va.myhealth.entity.VitalityAge;

public interface VitalityAgeRepository {

    VitalityAge getVitalityAge();

    void deleteVitalityAge();

    void saveVitalityAgeValue(VitalityAge vitalityAge);
}
