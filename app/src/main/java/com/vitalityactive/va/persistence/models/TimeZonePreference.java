package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class TimeZonePreference extends RealmObject implements Model {
    private String code;
    private String daylightSavings;
    private String value;

    public TimeZonePreference() {
    }

    public TimeZonePreference(LoginServiceResponse.TimeZonePreference timeZonePreference) {
        code = timeZonePreference.code;
        daylightSavings = timeZonePreference.daylightSavings == null ? "" : timeZonePreference.daylightSavings.toString();
        value = timeZonePreference.value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDaylightSavings() {
        return daylightSavings;
    }

    public void setDaylightSavings(String daylightSavings) {
        this.daylightSavings = daylightSavings;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
