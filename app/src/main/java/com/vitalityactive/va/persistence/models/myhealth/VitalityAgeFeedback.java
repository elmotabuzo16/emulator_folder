package com.vitalityactive.va.persistence.models.myhealth;


import com.vitalityactive.va.networking.model.HealthAttributeFeedbackResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class VitalityAgeFeedback extends RealmObject implements Model {
    protected int feedbackTypeKey;
    protected String feedbackTypeName;
    protected String feedbackTypeCode;
    protected int feedbackTypeTypeKey;
    protected String feedbackTypeTypeName;
    protected String feedbackTypeTypeCode;
    protected int typeKey;

    public VitalityAgeFeedback() {
    }

    public VitalityAgeFeedback(HealthAttributeFeedbackResponse.HealthAttributeFeedback healthAttributeFeedback) {
        this.feedbackTypeKey = healthAttributeFeedback.feedbackTypeKey;
        this.feedbackTypeName = healthAttributeFeedback.feedbackTypeName;
        this.feedbackTypeCode = healthAttributeFeedback.feedbackTypeCode;
        this.feedbackTypeTypeKey = healthAttributeFeedback.feedbackTypeTypeKey;
        this.feedbackTypeTypeName = healthAttributeFeedback.feedbackTypeTypeName;
        this.feedbackTypeTypeCode = healthAttributeFeedback.feedbackTypeTypeCode;
    }
}
