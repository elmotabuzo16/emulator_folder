package com.vitalityactive.va.persistence.models.vhc;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.vhc.service.HealthAttributeResponse;

import io.realm.RealmObject;

public class EventMetaData extends RealmObject implements Model {
    private String note;
    private String code;
    private String unitOfMeasure;
    private String name;
    private String value;

    private int key;

    public EventMetaData() {
    }

    public EventMetaData(HealthAttributeResponse.EventMetaData metaData) {
        note = metaData.note;
        code = metaData.code;
        unitOfMeasure = metaData.unitOfMeasure;
        name = metaData.name;
        value = metaData.value == null ? "" : metaData.value;
        key = metaData.key;
    }

    public String getNote() {
        return note;
    }

    public String getCode() {
        return code;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public int getKey() {
        return key;
    }
}
