package com.vitalityactive.va.vhc.dto;

import com.vitalityactive.va.dto.HealthAttributeReading;
import com.vitalityactive.va.persistence.models.vhc.HealthAttributeFeedback;

import io.realm.RealmList;

public class HealthAttributeReadingDTO {
    private int healthAttributeTypeKey;
    private String measuredOn;
    private HealthAttributeFeedbackDTO feedback = new HealthAttributeFeedbackDTO();

    public HealthAttributeReadingDTO(HealthAttributeReading model) {
        measuredOn = model.getMeasuredOn();

        RealmList<HealthAttributeFeedback> feedbacks = model.getFeedbacks();

        if (feedbacks != null && feedbacks.size() > 0) {
            feedback = new HealthAttributeFeedbackDTO(feedbacks.get(0));
        }

        healthAttributeTypeKey = model.getHealthAttributeTypeKey();
    }

    public HealthAttributeReadingDTO() {

    }

    public int getHealthAttributeTypeKey() {
        return healthAttributeTypeKey;
    }

    public String getMeasuredOn() {
        return measuredOn;
    }

    public HealthAttributeFeedbackDTO getFeedback() {
        return feedback;
    }
}
