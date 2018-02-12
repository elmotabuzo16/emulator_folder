package com.vitalityactive.va.persistence.models.myhealth;

import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class Recommendation extends RealmObject implements Model {
    private String fromValue;
    private String toValue;
    private Integer unitOfMeasureId;
    private String value;
    private String friendlyValue;

    public Recommendation() {
    }

    public Recommendation(HealthAttributeInformationResponse.Recommendation recommendation) {
        this.fromValue = recommendation.fromValue;
        this.toValue = recommendation.toValue;
        this.unitOfMeasureId = recommendation.unitOfMeasureId;
        this.value = recommendation.value;
        this.friendlyValue = recommendation.friendlyValue;
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
