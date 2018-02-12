package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.networking.model.Application;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class AppConfigFeature extends RealmObject implements Model {

    private RealmList<AppConfigFeatureParameter> featureParameters;
    private String type;
    private boolean toggle;
    private String name;

    public AppConfigFeature() {

    }

    public AppConfigFeature(Application.ApplicationFeature applicationFeature) {
        type = applicationFeature.type;
        toggle = applicationFeature.toggle;
        name = applicationFeature.name;

        featureParameters = new RealmList<>();
        for (Application.ApplicationFeatureParameter featureParameter : applicationFeature.applicationFeatureParameters) {
            featureParameters.add(new AppConfigFeatureParameter(featureParameter, type));
        }
    }

    public RealmList<AppConfigFeatureParameter> getFeatureParameters() {
        return featureParameters;
    }

    public String getType() {
        return type;
    }

    public boolean getToggle() {
        return toggle;
    }

    public String getName() {
        return name;
    }
}
