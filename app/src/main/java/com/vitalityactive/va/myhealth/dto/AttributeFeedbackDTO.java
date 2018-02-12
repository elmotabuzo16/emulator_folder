package com.vitalityactive.va.myhealth.dto;

import com.vitalityactive.va.persistence.models.myhealth.HealthAttributeTip;
import com.vitalityactive.va.persistence.models.vhc.HealthAttributeFeedback;
import com.vitalityactive.va.vhc.dto.HealthAttributeFeedbackDTO;

import java.util.ArrayList;
import java.util.List;

public class AttributeFeedbackDTO extends HealthAttributeFeedbackDTO {
    List<FeedbackTipDTO> feedbackTips = null;

    public AttributeFeedbackDTO(HealthAttributeFeedback healthAttributeFeedback) {
        super(healthAttributeFeedback);
        this.feedbackTips = new ArrayList<>();
        for (HealthAttributeTip healthAttributeTip : healthAttributeFeedback.getHealthAttributeTips()) {
            this.feedbackTips.add(new FeedbackTipDTO(healthAttributeTip));
        }
    }

    public AttributeFeedbackDTO(Integer feedbackTypeKey, String feedbackTypeName, String feedbackTypeCode, int feedbackTypeTypeKey, String feedbackTypeTypeName, String feedbackTypeTypeCode, int typeKey, List<FeedbackTipDTO> feedbackTips) {
        super(feedbackTypeKey, feedbackTypeName, feedbackTypeCode, feedbackTypeTypeKey, feedbackTypeTypeName, feedbackTypeTypeCode, typeKey);
        this.feedbackTips = feedbackTips;
    }

    public List<FeedbackTipDTO> getFeedbackTips() {
        return feedbackTips;
    }

    public void setFeedbackTips(List<FeedbackTipDTO> feedbackTips) {
        this.feedbackTips = feedbackTips;
    }

}
