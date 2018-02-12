package com.vitalityactive.va.persistence.models.myhealth;

import com.vitalityactive.va.networking.model.HealthAttributeFeedbackResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.persistence.models.vhc.HealthAttributeFeedback;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class VitalityAgeHealthAttribute extends RealmObject implements Model {

    public String attributeTypeCode;
    public Integer attributeTypeKey;
    public String attributeTypeName;
    public RealmList<HealthAttributeFeedback> healthAttributeFeedbacks = new RealmList<>();
    public String measuredOn;
    public Integer sourceEventId;
    public String unitofMeasure;
    public String value;

    public VitalityAgeHealthAttribute() {
    }

    public VitalityAgeHealthAttribute(HealthAttributeFeedbackResponse.HealthAttribute healthAttribute) {
        this.attributeTypeCode = healthAttribute.attributeTypeCode;
        this.attributeTypeKey = healthAttribute.attributeTypeKey;
        this.attributeTypeName = healthAttribute.attributeTypeName;
        this.measuredOn = healthAttribute.measuredOn;
        this.sourceEventId = healthAttribute.sourceEventId;
        this.unitofMeasure = healthAttribute.unitofMeasure;
        this.value = healthAttribute.value;
    }


    public void addHealthAttributeFeedbacks(List<HealthAttributeFeedback> healthAttributeFeedbacks){
        this.healthAttributeFeedbacks.addAll(healthAttributeFeedbacks);
    }
}
