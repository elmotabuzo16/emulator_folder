package com.vitalityactive.va.persistence.models.wellnessdevices;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.service.EventType;

import io.realm.RealmObject;

public class WellnessDevicesPointsConditions extends RealmObject implements Model {
    private int metadataTypeKey;
    private String metadataTypeCode;
    private String metadataTypeName;
    private String greaterThanOrEqualTo;
    private String lessThan;
    private String unitOfMeasure;
    private String greaterThan;
    private String lessThanOrEqualTo;

    public WellnessDevicesPointsConditions() {
    }

    public WellnessDevicesPointsConditions(EventType.Conditions src) {
        if(src != null) {
            this.metadataTypeKey = src.metadataTypeKey;
            this.metadataTypeCode = src.metadataTypeCode;
            this.metadataTypeName = src.metadataTypeName;
            this.greaterThanOrEqualTo = src.greaterThanOrEqualTo;
            this.lessThan = src.lessThan;
            this.greaterThan = src.greaterThan;
            this.lessThanOrEqualTo = src.lessThanOrEqualTo;
            this.unitOfMeasure = src.unitOfMeasure;
        }
    }

    public int getMetadataTypeKey() {
        return metadataTypeKey;
    }

    public String getMetadataTypeCode() {
        return metadataTypeCode;
    }

    public String getMetadataTypeName() {
        return metadataTypeName;
    }

    public String getGreaterThanOrEqualTo() {
        return greaterThanOrEqualTo;
    }

    public String getLessThan() {
        return lessThan;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public String getGreaterThan() {
        return greaterThan;
    }

    public String getLessThanOrEqualTo() {
        return lessThanOrEqualTo;
    }
}
