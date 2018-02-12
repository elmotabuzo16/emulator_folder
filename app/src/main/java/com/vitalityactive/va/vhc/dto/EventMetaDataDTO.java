package com.vitalityactive.va.vhc.dto;

import android.support.annotation.NonNull;

import com.vitalityactive.va.persistence.models.vhc.EventMetaData;

class EventMetaDataDTO {
    private String value;
    private String unitOfMeasureTypeKey;

    public EventMetaDataDTO(EventMetaData metaData) {
        value = metaData.getValue();
        unitOfMeasureTypeKey = metaData.getUnitOfMeasure();
    }

    public EventMetaDataDTO() {
        value = "";
    }

    @NonNull
    public String getValue() {
        return value;
    }

    public String getUnitOfMeasureTypeKey() {
        return unitOfMeasureTypeKey;
    }
}
