package com.vitalityactive.va.persistence.models.vhc;

import com.vitalityactive.va.UnitsOfMeasure;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.vhc.service.HealthAttributeResponse;

import io.realm.RealmList;
import io.realm.RealmObject;

public class HealthAttributeTypeValidValues extends RealmObject implements Model{
    private Float minValue;
    private Float maxValue;
    private RealmList<ValidOption> validOptions;

    private String unitOfMeasureId;

    private int typeTypeKey;
    private String typeTypeName;
    private String typeTypeCode;

    private float upperLimit;
    private float lowerLimit;

    public HealthAttributeTypeValidValues() {
    }

    public HealthAttributeTypeValidValues(HealthAttributeResponse.ValidValue validValue) {
        this.minValue = validValue.minValue;
        this.maxValue = validValue.maxValue;
        this.typeTypeKey = validValue.typeTypeKey;
        this.typeTypeCode = validValue.typeTypeCode;
        this.unitOfMeasureId = validValue.unitOfMeasureId;
        this.typeTypeCode = validValue.typeTypeCode;
        this.upperLimit = validValue.upperLimit;
        this.lowerLimit = validValue.lowerLimit;
        this.typeTypeName = validValue.typeTypeName;
        if (validValue.validValuesList != null) {
            validOptions = new RealmList<>();
            for (String validOption : validValue.validValuesList) {
                validOptions.add(new ValidOption(validOption));
            }
        }
    }

    public HealthAttributeTypeValidValues(UnitsOfMeasure unit, Float min, Float max) {
        unitOfMeasureId = unit.getTypeKey();
        minValue = min;
        maxValue = max;
    }

    public Float getMinValue() {
        return minValue;
    }

    public Float getMaxValue() {
        return maxValue;
    }

    public int getTypeTypeKey() {
        return typeTypeKey;
    }

    public UnitsOfMeasure getUnitOfMeasure() {
        return UnitsOfMeasure.fromValue(unitOfMeasureId);
    }

    public RealmList<ValidOption> getValidOptions() {
        return validOptions;
    }
}
