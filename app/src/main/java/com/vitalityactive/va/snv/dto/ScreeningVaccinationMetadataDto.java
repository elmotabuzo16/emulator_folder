package com.vitalityactive.va.snv.dto;

import com.vitalityactive.va.persistence.models.screeningsandvaccinations.ScreeningVaccinationMetadata;

/**
 * Created by dharel.h.rosell on 12/12/2017.
 */

public class ScreeningVaccinationMetadataDto {

    private String typeCode;
    private int typeKey;
    private String typeName;
    private String unitOfMeasure;
    private String value;

    public ScreeningVaccinationMetadataDto() {
    }

    public ScreeningVaccinationMetadataDto(ScreeningVaccinationMetadata metadataRealm) {
        this.typeCode = metadataRealm.getTypeCode();
        this.typeKey = metadataRealm.getTypeKey();
        this.typeName = metadataRealm.getTypeName();
        this.unitOfMeasure = metadataRealm.getUnitOfMeasure();
        this.value = metadataRealm.getValue();
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(int typeKey) {
        this.typeKey = typeKey;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
}
