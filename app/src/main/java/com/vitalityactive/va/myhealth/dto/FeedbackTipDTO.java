package com.vitalityactive.va.myhealth.dto;

import com.vitalityactive.va.persistence.models.myhealth.HealthAttributeTip;

public class FeedbackTipDTO {
    private String note;
    private Integer sortOrder;
    private String typeCode;
    private Integer typeKey;
    private String typeName;

    public FeedbackTipDTO(HealthAttributeTip healthAttributeTip) {
        this.note = healthAttributeTip.getNote();
        this.sortOrder = healthAttributeTip.getSortOrder();
        this.typeCode = healthAttributeTip.getTypeCode();
        this.typeKey = healthAttributeTip.getTypeKey();
        this.typeName = healthAttributeTip.getTypeName();
    }

    public FeedbackTipDTO(String note, Integer sortOrder, String typeCode, Integer typeKey, String typeName) {
        this.note = note;
        this.sortOrder = sortOrder;
        this.typeCode = typeCode;
        this.typeKey = typeKey;
        this.typeName = typeName;
    }

    public String getNote() {
        return note;
    }

    public Integer getSortOrder() {
        return sortOrder;
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
}
