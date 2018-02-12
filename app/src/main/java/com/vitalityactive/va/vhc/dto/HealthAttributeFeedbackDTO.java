package com.vitalityactive.va.vhc.dto;

import com.vitalityactive.va.myhealth.dto.AttributeFeedbackDTO;
import com.vitalityactive.va.persistence.models.vhc.HealthAttributeFeedback;

public class HealthAttributeFeedbackDTO {
    private Integer feedbackTypeKey;
    private String feedbackTypeName;
    private String feedbackTypeCode;
    private int feedbackTypeTypeKey;
    private String feedbackTypeTypeName;
    private String feedbackTypeTypeCode;
    private String whyIsThisImportant;

    private int typeKey;

    public HealthAttributeFeedbackDTO(HealthAttributeFeedback healthAttributeFeedback) {
        this.feedbackTypeKey = healthAttributeFeedback.getFeedbackTypeKey();
        this.feedbackTypeName = healthAttributeFeedback.getFeedbackTypeName();
        this.feedbackTypeCode = healthAttributeFeedback.getFeedbackTypeCode();
        this.feedbackTypeTypeKey = healthAttributeFeedback.getFeedbackTypeTypeKey();
        this.feedbackTypeTypeName = healthAttributeFeedback.getFeedbackTypeTypeName();
        this.feedbackTypeTypeCode = healthAttributeFeedback.getFeedbackTypeTypeCode();
        this.whyIsThisImportant = healthAttributeFeedback.getWhyIsThisImportant();
        this.typeKey = healthAttributeFeedback.getTypeKey();
    }

    public HealthAttributeFeedbackDTO(Integer feedbackTypeKey, String feedbackTypeName, String feedbackTypeCode, int feedbackTypeTypeKey, String feedbackTypeTypeName, String feedbackTypeTypeCode, int typeKey) {
        this.feedbackTypeKey = feedbackTypeKey;
        this.feedbackTypeName = feedbackTypeName;
        this.feedbackTypeCode = feedbackTypeCode;
        this.feedbackTypeTypeKey = feedbackTypeTypeKey;
        this.feedbackTypeTypeName = feedbackTypeTypeName;
        this.feedbackTypeTypeCode = feedbackTypeTypeCode;
        this.typeKey = typeKey;
    }

    public HealthAttributeFeedbackDTO() {

    }

    public HealthAttributeFeedbackDTO(AttributeFeedbackDTO vitalityAgeFeedback) {
        this.feedbackTypeKey = vitalityAgeFeedback.getFeedbackTypeKey();
        this.feedbackTypeName = vitalityAgeFeedback.getFeedbackTypeName();
        this.feedbackTypeCode = vitalityAgeFeedback.getFeedbackTypeCode();
        this.feedbackTypeTypeKey = vitalityAgeFeedback.getFeedbackTypeTypeKey();
        this.feedbackTypeTypeName = vitalityAgeFeedback.getFeedbackTypeTypeName();
        this.feedbackTypeTypeCode = vitalityAgeFeedback.getFeedbackTypeTypeCode();
        this.typeKey = vitalityAgeFeedback.getTypeKey();
    }

    public Integer getFeedbackTypeKey() {
        return feedbackTypeKey;
    }

    public String getFeedbackTypeName() {
        return feedbackTypeName;
    }

    public String getFeedbackTypeCode() {
        return feedbackTypeCode;
    }

    public int getFeedbackTypeTypeKey() {
        return feedbackTypeTypeKey;
    }

    public String getFeedbackTypeTypeName() {
        return feedbackTypeTypeName;
    }

    public String getFeedbackTypeTypeCode() {
        return feedbackTypeTypeCode;
    }

    public String getWhyIsThisImportant() {
        return whyIsThisImportant;
    }

    public int getTypeKey() {
        return typeKey;
    }


}
