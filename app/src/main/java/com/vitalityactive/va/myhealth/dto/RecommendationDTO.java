package com.vitalityactive.va.myhealth.dto;

import com.vitalityactive.va.persistence.models.myhealth.Recommendation;

public class RecommendationDTO {
    private String fromValue;
    private String toValue;
    private Integer unitOfMeasureId;
    private String value;
    private String friendlyValue;

    public RecommendationDTO(Recommendation recommendation) {
        this.fromValue = recommendation.getFromValue();
        this.toValue = recommendation.getToValue();
        this.unitOfMeasureId = recommendation.getUnitOfMeasureId();
        this.value = recommendation.getValue();
        this.friendlyValue = recommendation.getFriendlyValue();
    }

    public RecommendationDTO(String fromValue, String toValue, Integer unitOfMeasureId, String value, String friendlyValue) {
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.unitOfMeasureId = unitOfMeasureId;
        this.value = value;
        this.friendlyValue = friendlyValue;
    }

    public String getFromValue() {
        return fromValue;
    }

    public String getToValue() {
        return toValue;
    }

    public Integer getUnitOfMeasureId() {
        return unitOfMeasureId;
    }

    public String getValue() {
        return value;
    }

    public String getFriendlyValue() {
        return friendlyValue;
    }
}
