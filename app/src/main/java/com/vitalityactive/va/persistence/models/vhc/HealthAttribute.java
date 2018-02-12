package com.vitalityactive.va.persistence.models.vhc;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.vhc.service.HealthAttributeResponse;

import io.realm.RealmList;
import io.realm.RealmObject;

public class HealthAttribute extends RealmObject implements Model {
    private int typeKey;
    private String typeName;
    private String typeCode;

    private RealmList<HealthAttributeTypeValidValues> validValues;

    public HealthAttribute() {
    }

    public HealthAttribute(HealthAttributeResponse.HealthAttribute response) {
        typeCode = response.typeCode;
        typeKey = response.typeKey;
        typeName = response.typeName;

        validValues = new RealmList<>();
        if (response.validValues != null) {
            for (HealthAttributeResponse.ValidValue validValue : response.validValues) {
                this.validValues.add(new HealthAttributeTypeValidValues(validValue));
            }
        }
    }

    public int getTypeKey() {
        return typeKey;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public RealmList<HealthAttributeTypeValidValues> getValidValues() {
        return validValues;
    }
}
