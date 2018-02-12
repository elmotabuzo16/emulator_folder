package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.networking.model.Application;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class AppConfigFeatureParameter extends RealmObject implements Model {
    private String name;
    private String value;
    private String featureType; // TODO: when backlinks/LinkingObjects are finally added (https://github.com/realm/realm-java/pull/4219), this can be removed. Think of this as a #hacklink

    public AppConfigFeatureParameter() {

    }

    public AppConfigFeatureParameter(Application.ApplicationFeatureParameter featureParameter, String featureType) {
        name = featureParameter.name;
        value = featureParameter.value;
        this.featureType = featureType;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getFeatureType() {
        return featureType;
    }
}
