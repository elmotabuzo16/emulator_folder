package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class AppConfigVersion extends RealmObject implements Model {
    private String effectiveFrom;
    private String effectiveTo;
    private String releaseVersion;

    @SuppressWarnings("unused") // required by realm
    public AppConfigVersion() {
    }

    public AppConfigVersion(String effectiveFrom, String effectiveTo, String releaseVersion) {
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
        this.releaseVersion = releaseVersion;
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public String getEffectiveTo() {
        return effectiveTo;
    }

    public String getReleaseVersion() {
        return releaseVersion;
    }
}
