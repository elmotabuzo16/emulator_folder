package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class GeographicalAreaPreference extends RealmObject implements Model {
    private String type;
    private String value;
    private String effectiveFrom;
    private String effectiveTo;

    public GeographicalAreaPreference() {

    }

    public GeographicalAreaPreference(LoginServiceResponse.GeographicalAreaPreference geographicalAreaPreference) {
        type = geographicalAreaPreference.type;
        value = geographicalAreaPreference.value;
        effectiveFrom = geographicalAreaPreference.effectiveFrom;
        effectiveTo = geographicalAreaPreference.effectiveTo;
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(String effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public String getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(String effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
