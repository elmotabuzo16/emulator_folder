package com.vitalityactive.va.wellnessdevices.dto;

import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.wellnessdevices.WellnessDevicesPointsConditions;
import com.vitalityactive.va.vitalitystatus.repository.ConditionsModel;
import com.vitalityactive.va.vitalitystatus.repository.ProductSubfeatureConditionsModel;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.service.EventType;

public class PointsConditionsDto {
    private int metadataTypeKey;
    private String metadataTypeCode;
    private String metadataTypeName;
    private String greaterThan;
    private String greaterThanOrEqualTo;
    private String lessThan;
    private String lessThanOrEqualTo;
    private String unitOfMeasure;

    public PointsConditionsDto() {
    }

    public PointsConditionsDto(EventType.Conditions src) {
        if(src != null) {
            this.metadataTypeKey = src.metadataTypeKey;
            this.metadataTypeCode = src.metadataTypeCode;
            this.metadataTypeName = src.metadataTypeName;
            this.greaterThan = src.greaterThan;
            this.greaterThanOrEqualTo = src.greaterThanOrEqualTo;
            this.lessThan = src.lessThan;
            this.lessThanOrEqualTo = src.lessThanOrEqualTo;
            this.unitOfMeasure = src.unitOfMeasure;
        }
    }

    public PointsConditionsDto(WellnessDevicesPointsConditions src) {
        if(src != null) {
            this.metadataTypeKey = src.getMetadataTypeKey();
            this.metadataTypeCode = src.getMetadataTypeCode();
            this.metadataTypeName = src.getMetadataTypeName();
            this.greaterThanOrEqualTo = src.getGreaterThanOrEqualTo();
            this.greaterThan = src.getGreaterThan();
            this.lessThan = src.getLessThan();
            this.lessThanOrEqualTo = src.getLessThanOrEqualTo();
            this.unitOfMeasure = src.getUnitOfMeasure();
        }
    }

    public PointsConditionsDto(ConditionsModel src) {
        if(src != null) {
            this.metadataTypeKey = src.getMetadataTypeKey();
            this.metadataTypeCode = src.getMetadataTypeCode();
            this.metadataTypeName = src.getMetadataTypeName();
            this.greaterThanOrEqualTo = String.valueOf(src.getGreaterThanOrEqualTo());
            this.greaterThan = String.valueOf(src.getGreaterThan());
            this.lessThan = String.valueOf(src.getLessThan());
            this.lessThanOrEqualTo = String.valueOf(src.getLessThanOrEqualTo());
            this.unitOfMeasure = String.valueOf(src.getUnitOfMeasure());
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

    public String getGreaterThan() {
        return greaterThan;
    }

    public String getLessThanOrEqualTo() {
        return lessThanOrEqualTo;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public static class Mapper implements DataStore.ModelMapper<WellnessDevicesPointsConditions, PointsConditionsDto> {
        @Override
        public PointsConditionsDto mapModel(WellnessDevicesPointsConditions conditions) {
            return new PointsConditionsDto(conditions);
        }
    }
}
