package com.vitalityactive.va.vhc.dto;

import com.vitalityactive.va.utilities.TextUtilities;

public class HealthAttributeDTO {
    private String unitOfMeasureTypeKey;
    private Integer feedbackTypeKey;
    private String feedbackTypeName;
    private String measuredOn;
    private String value;
    private int potentialPoints;
    private int earnedPoints;
    private String pointsStatus;
    private String sourceString;
    private int eventTypeKey;

    public HealthAttributeDTO(int eventTypeKey,
                              String sourceString,
                              String pointsStatus,
                              int totalEarnedPoints,
                              int totalPotentialPoints,
                              String value,
                              String measuredOn,
                              String unitOfMeasureTypeKey,
                              Integer feedbackTypeKey,
                              String feedbackTypeName) {

        this.sourceString = sourceString;
        this.pointsStatus = pointsStatus;
        this.eventTypeKey = eventTypeKey;
        this.potentialPoints = totalPotentialPoints;
        this.value = value;
        this.measuredOn = measuredOn;
        this.unitOfMeasureTypeKey = unitOfMeasureTypeKey;
        this.feedbackTypeKey = feedbackTypeKey;
        this.feedbackTypeName = feedbackTypeName;
        this.earnedPoints = totalEarnedPoints;
    }

    public HealthAttributeDTO() {

    }

    public boolean hasReading() {
        return !(TextUtilities.isNullOrWhitespace(value)
                || TextUtilities.isNullOrWhitespace(measuredOn));
    }

    public String getUnitOfMeasureTypeKey() {
        return unitOfMeasureTypeKey;
    }

    public Integer getFeedbackTypeKey() {
        return feedbackTypeKey;
    }

    public String getFeedbackTypeName() {
        return feedbackTypeName;
    }

    public String getMeasuredOn() {
        return measuredOn;
    }

    public String getValue() {
        return value;
    }

    public int getPotentialPoints() {
        return potentialPoints;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public String getPointsStatus() {
        return pointsStatus;
    }

    public String getSourceString() {
        return sourceString;
    }

    public int getEventTypeKey() {
        return eventTypeKey;
    }
}
