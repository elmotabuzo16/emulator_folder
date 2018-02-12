package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class Reference extends RealmObject implements Model {
    private String issuedBy;
    private String type;
    private String typeCode;
    private String typeKey;
    private String value;
    private String effectiveFrom;
    private String effectiveTo;
    private String typeName;

    public Reference() {

    }

    public Reference(LoginServiceResponse.Reference reference) {
        issuedBy = reference.issuedBy.toString();
        type = reference.type;
        typeCode = reference.typeCode;
        typeKey = reference.typeKey;
        value = reference.value;
        effectiveFrom = reference.effectiveFrom;
        effectiveTo = reference.effectiveTo;
        typeName = reference.typeName;
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(String effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public String getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(String effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return typeCode;

    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }
}
