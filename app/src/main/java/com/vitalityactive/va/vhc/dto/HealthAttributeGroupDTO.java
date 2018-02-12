package com.vitalityactive.va.vhc.dto;

import com.vitalityactive.va.vhc.HealthAttributeFeedbackType;

import java.util.Date;

public class HealthAttributeGroupDTO {
    private Integer feedbackTypeKey;
    private Date measuredOn;
    private boolean groupHasNoReading;
    private String description;
    private int featureTypeKey;
    private int maxPotentialPoints;
    private int totalEarnedPoints;
    private boolean partiallyComplete;

    public HealthAttributeGroupDTO(String groupDescription,
                                   boolean groupHasNoReading,
                                   Integer feedbackTypeKey,
                                   Date measuredOn,
                                   Integer featureTypeKey,
                                   int maxPotentialPoints,
                                   int totalEarnedPoints,
                                   boolean partiallyComplete) {
        description = groupDescription;
        this.groupHasNoReading = groupHasNoReading;
        this.feedbackTypeKey = feedbackTypeKey;
        this.measuredOn = measuredOn;
        this.featureTypeKey = featureTypeKey;
        this.maxPotentialPoints = maxPotentialPoints;
        this.totalEarnedPoints = totalEarnedPoints;
        this.partiallyComplete = partiallyComplete;
    }

    public HealthAttributeGroupDTO() {

    }

    public boolean groupHasReadings() {
        return !groupHasNoReading;
    }

    public boolean inHealthyRange() {
        return HealthAttributeFeedbackType.fromValue(getFeedbackTypeKey()).inHealthyRange();

    }

    public boolean groupHasFeedback() {
        return getFeedbackTypeKey() != null;

    }

    private Integer getFeedbackTypeKey() {
        return feedbackTypeKey;
    }

    public Date getMeasuredOn() {
        return measuredOn;
    }

    public String getDescription() {
        return description;
    }

    public int getFeatureTypeKey() {
        return featureTypeKey;
    }

    public int getMaxPotentialPoints() {
        return maxPotentialPoints;
    }

    public int getTotalEarnedPoints() {
        return totalEarnedPoints;
    }

    public boolean isPartiallyComplete() {
        return partiallyComplete;
    }
}
