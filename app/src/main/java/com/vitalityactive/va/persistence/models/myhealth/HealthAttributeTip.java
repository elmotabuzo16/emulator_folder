package com.vitalityactive.va.persistence.models.myhealth;

import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class HealthAttributeTip extends RealmObject implements Model {

    private String note;
    private Integer sortOrder;
    private String typeCode;
    private Integer typeKey;
    private String typeName;

    public HealthAttributeTip() {
    }

    public HealthAttributeTip(String note, Integer sortOrder, String typeCode, Integer typeKey, String typeName) {
        this.note = note;
        this.sortOrder = sortOrder;
        this.typeCode = typeCode;
        this.typeKey = typeKey;
        this.typeName = typeName;
    }

    public HealthAttributeTip(HealthAttributeInformationResponse.FeedbackTip feedbackTip){
        this.note = feedbackTip.note;
        this.sortOrder = feedbackTip.sortOrder;
        this.typeCode = feedbackTip.typeCode;
        this.typeKey = feedbackTip.typeKey!=null?feedbackTip.typeKey:0;
        this.typeName = feedbackTip.typeName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Integer getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(Integer typeKey) {
        this.typeKey = typeKey;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


}
