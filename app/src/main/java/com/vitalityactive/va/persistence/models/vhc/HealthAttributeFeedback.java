package com.vitalityactive.va.persistence.models.vhc;

import com.vitalityactive.va.networking.model.HealthAttributeFeedbackResponse;
import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.persistence.models.myhealth.HealthAttributeTip;
import com.vitalityactive.va.vhc.service.HealthAttributeResponse;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class HealthAttributeFeedback extends RealmObject implements Model {
    protected int feedbackTypeKey;
    protected String feedbackTypeName;
    protected String feedbackTypeCode;
    protected int feedbackTypeTypeKey;
    protected String feedbackTypeTypeName;
    protected String feedbackTypeTypeCode;
    protected String whyIsThisImportant;
    protected int typeKey;
    protected RealmList<HealthAttributeTip> healthAttributeTips;

    public HealthAttributeFeedback() {

    }

    public HealthAttributeFeedback(HealthAttributeFeedbackResponse.HealthAttributeFeedback healthAttributeFeedback) {
        this.feedbackTypeKey = healthAttributeFeedback.feedbackTypeKey;
        this.feedbackTypeName = healthAttributeFeedback.feedbackTypeName;
        this.feedbackTypeCode = healthAttributeFeedback.feedbackTypeCode;
        this.feedbackTypeTypeKey = healthAttributeFeedback.feedbackTypeTypeKey!=null? healthAttributeFeedback.feedbackTypeTypeKey:0;
        this.feedbackTypeTypeName = healthAttributeFeedback.feedbackTypeTypeName;
        this.feedbackTypeTypeCode = healthAttributeFeedback.feedbackTypeTypeCode;
    }

    public HealthAttributeFeedback(HealthAttributeResponse.HealthAttributeFeedback healthAttributeFeedback) {
        feedbackTypeKey = healthAttributeFeedback.feedbackTypeKey;
        feedbackTypeName = healthAttributeFeedback.feedbackTypeName;
        feedbackTypeCode = healthAttributeFeedback.feedbackTypeCode;
        feedbackTypeTypeKey = healthAttributeFeedback.feedbackTypeTypeKey;
        feedbackTypeTypeName = healthAttributeFeedback.feedbackTypeTypeName;
        feedbackTypeTypeCode = healthAttributeFeedback.feedbackTypeTypeCode;
    }

    public HealthAttributeFeedback(HealthAttributeInformationResponse.HealthAttributeFeedback healthAttributeFeedback) {
        feedbackTypeKey = healthAttributeFeedback.feedbackTypeKey!=null?healthAttributeFeedback.feedbackTypeKey:0;
        feedbackTypeName = healthAttributeFeedback.feedbackTypeName;
        feedbackTypeCode = healthAttributeFeedback.feedbackTypeCode;
        feedbackTypeTypeKey = healthAttributeFeedback.feedbackTypeTypeKey!=null?healthAttributeFeedback.feedbackTypeTypeKey:0;
        feedbackTypeTypeName = healthAttributeFeedback.feedbackTypeTypeName;
        feedbackTypeTypeCode = healthAttributeFeedback.feedbackTypeTypeCode;
        whyIsThisImportant = healthAttributeFeedback.whyIsThisImportant;
        healthAttributeTips = toHealthAttributeTip(healthAttributeFeedback.feedbackTips);
    }

    public int getFeedbackTypeKey() {
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

    public int getTypeKey() {
        return typeKey;
    }

    public String getWhyIsThisImportant() {
        return whyIsThisImportant;
    }

    private RealmList<HealthAttributeTip> toHealthAttributeTip(List<HealthAttributeInformationResponse.FeedbackTip> feedbackTipList) {
        RealmList<HealthAttributeTip> healthAttributeTips = new RealmList<HealthAttributeTip>();
        if (feedbackTipList != null) {
            for (HealthAttributeInformationResponse.FeedbackTip feedbackTip : feedbackTipList) {
                healthAttributeTips.add(new HealthAttributeTip(feedbackTip));
            }
        }
        return healthAttributeTips;
    }

    public RealmList<HealthAttributeTip> getHealthAttributeTips() {
        return healthAttributeTips;
    }
}
