package com.vitalityactive.va.persistence.models.myhealth;

import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class HealthAttributeMetadata extends RealmObject implements Model {

    Long measurementUnitId;
    String typeCode;
    Integer typeKey;
    String typeName;
    String value;
    String friendlyValue;

    public HealthAttributeMetadata() {
    }

    public HealthAttributeMetadata(Long measurementUnitId, String typeCode, Integer typeKey, String typeName, String value, String friendlyValue) {
        this.measurementUnitId = measurementUnitId;
        this.typeCode = typeCode;
        this.typeKey = typeKey;
        this.typeName = typeName;
        this.value = value;
        this.friendlyValue = friendlyValue;
    }

    public HealthAttributeMetadata(HealthAttributeInformationResponse.HealthAttributeMetadata healthAttributeMetadata) {
        this.measurementUnitId = healthAttributeMetadata.measurementUnitId;
        this.typeCode = healthAttributeMetadata.typeCode;
        this.typeKey = healthAttributeMetadata.typeKey;
        this.typeName = healthAttributeMetadata.typeName;
        this.value = healthAttributeMetadata.value;
        this.friendlyValue = healthAttributeMetadata.friendlyValue;
    }

    public Long getMeasurementUnitId() {
        return measurementUnitId;
    }

    public void setMeasurementUnitId(Long measurementUnitId) {
        this.measurementUnitId = measurementUnitId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Integer getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(Integer typeKey) {
        this.typeKey = typeKey;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFriendlyValue() {
        return friendlyValue;
    }
}
