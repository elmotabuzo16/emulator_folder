package com.vitalityactive.va.dto;


import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.models.Reference;

public class ReferenceDTO {
    private String issuedBy;
    private String type;
    private String typeCode;
    private String typeKey;
    private String value;
    private String effectiveTo;
    private String effectiveFrom;
    private String typeName;

    public ReferenceDTO(LoginServiceResponse.Reference reference) {
        issuedBy = reference.issuedBy.toString();
        type = reference.type;
        typeCode = reference.typeCode;
        typeKey = reference.typeKey;
        value = reference.value;
        effectiveTo = reference.effectiveTo;
        effectiveFrom = reference.effectiveFrom;
        typeName = reference.typeName;
    }

    public ReferenceDTO(Reference reference) {
        issuedBy = reference.getIssuedBy();
        type = reference.getType();
        typeCode = reference.getTypeCode();
        typeKey = reference.getTypeKey();
        value = reference.getValue();
        effectiveTo = reference.getEffectiveTo();
        effectiveFrom = reference.getEffectiveFrom();
        typeName = reference.getTypeName();
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getValue() {
        return value;
    }

    public String getEffectiveTo() {
        return effectiveTo;
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public String getType() {
        return type;
    }

    public String getTypeKey() {
        return typeKey;
    }
}
