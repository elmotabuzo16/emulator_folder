package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.networking.model.PointsHistoryServiceResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class Metadata extends RealmObject implements Model {
    private String typeKey;
    private String unitOfMeasure;
    private String value;
    private String typeCode;
    private String typeName;

    public Metadata() {

    }

    public Metadata(PointsHistoryServiceResponse.Metadata metadata) {
        typeKey = metadata.typeKey;
        typeCode = metadata.typeCode;
        typeName = metadata.typeName;
        unitOfMeasure = metadata.unitOfMeasure;
        value = metadata.value;
    }

    public String getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }
}
