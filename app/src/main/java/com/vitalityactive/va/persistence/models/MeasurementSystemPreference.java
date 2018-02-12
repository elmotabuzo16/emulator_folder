package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class MeasurementSystemPreference extends RealmObject implements Model {
    private String type;
    private String name;

    public MeasurementSystemPreference() {

    }

    public MeasurementSystemPreference(LoginServiceResponse.MeasurementSystemPreference measurementSystemPreference) {
        type = measurementSystemPreference.typeKey;
        name = measurementSystemPreference.name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
