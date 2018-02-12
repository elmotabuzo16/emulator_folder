package com.vitalityactive.va.myhealth.dto;


import com.vitalityactive.va.persistence.models.myhealth.HealthAttributeMetadata;

public class HealthAttributeMetadataDto {

    Long measurementUnitId;
    String typeCode;
    Integer typeKey;
    String typeName;
    String value;
    String friendlyValue;

    public HealthAttributeMetadataDto(Long measurementUnitId, String typeCode, Integer typeKey, String typeName, String value, String friendlyValue) {
        this.measurementUnitId = measurementUnitId;
        this.typeCode = typeCode;
        this.typeKey = typeKey;
        this.typeName = typeName;
        this.value = value;
        this.friendlyValue = friendlyValue;
    }

    public HealthAttributeMetadataDto(HealthAttributeMetadata healthAttributeMetadata) {
        this.measurementUnitId = healthAttributeMetadata.getMeasurementUnitId();
        this.typeCode = healthAttributeMetadata.getTypeCode();
        this.typeKey = healthAttributeMetadata.getTypeKey();
        this.typeName = healthAttributeMetadata.getTypeName();
        this.value = healthAttributeMetadata.getValue();
        this.friendlyValue = healthAttributeMetadata.getFriendlyValue();
    }

    public Long getMeasurementUnitId() {
        return measurementUnitId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public Integer getTypeKey() {
        return typeKey;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getValue() {
        return value;
    }

    public String getFriendlyValue() {
        return friendlyValue;
    }
}
