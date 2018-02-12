package com.vitalityactive.va.dto;

import com.vitalityactive.va.persistence.models.Metadata;
import com.vitalityactive.va.utilities.TextUtilities;

public class MetadataDTO {
    private int typeKey;
    private String unitOfMeasure;
    private String value;
    private String typeName;

    MetadataDTO(Metadata metadata) {
        typeKey = TextUtilities.getIntegerFromString(metadata.getTypeKey());
        typeName = metadata.getTypeName();
        unitOfMeasure = metadata.getUnitOfMeasure();
        value = metadata.getValue();
    }

    public int getTypeKey() {
        return typeKey;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public String getValue() {
        return value;
    }

    public String getTypeName() {
        return typeName;
    }
}
