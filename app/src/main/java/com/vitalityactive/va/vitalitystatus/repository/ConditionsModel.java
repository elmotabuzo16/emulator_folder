package com.vitalityactive.va.vitalitystatus.repository;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.utilities.InvalidModelLogger;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.service.EventType;

import io.realm.RealmObject;

public class ConditionsModel extends RealmObject implements Model{
    private String greaterThan;
    private String greaterThanOrEqualTo;
    private String lessThan;
    private String lessThanOrEqualTo;
    private String metadataTypeCode;
    private Integer metadataTypeKey;
    private String metadataTypeName;
    private String unitOfMeasure;

    public ConditionsModel() {}

    public static ConditionsModel create(EventType.Conditions response) {
            if (response.metadataTypeKey == 0) {
        InvalidModelLogger.warn(response, 0, "required fields are null");
        return null;
    }

    ConditionsModel instance = new ConditionsModel();

    instance.greaterThan = response.greaterThan;
    instance.greaterThanOrEqualTo = response.greaterThanOrEqualTo;
    instance.lessThan = response.lessThan;
    instance.lessThanOrEqualTo = response.lessThanOrEqualTo;
    instance.metadataTypeCode = response.metadataTypeCode;
    instance.metadataTypeKey = response.metadataTypeKey;
    instance.metadataTypeName = response.metadataTypeName;
    instance.unitOfMeasure = response.unitOfMeasure;

        return instance;
}

    public Integer getMetadataTypeKey() {
        return metadataTypeKey;
    }

    public String getMetadataTypeCode() {
        return metadataTypeCode;
    }

    public String getMetadataTypeName() {
        return metadataTypeName;
    }

    public String getGreaterThanOrEqualTo() {
        return greaterThanOrEqualTo == null ? "" : greaterThanOrEqualTo;
    }

    public String getGreaterThan() {
        return greaterThan == null ? "" : greaterThan;
    }

    public String getLessThan() {
        return lessThan == null ? "" : lessThan;
    }

    public String getLessThanOrEqualTo() {
        return lessThanOrEqualTo == null ? "" : lessThanOrEqualTo;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure == null ? "" : unitOfMeasure;
    }
}
