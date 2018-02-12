package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class LanguagePreference extends RealmObject implements Model {
    private String ISOCode;
    private String value;

    public LanguagePreference() {

    }

    public LanguagePreference(LoginServiceResponse.LanguagePreference languagePreference) {
        ISOCode = languagePreference.iSOCode;
        value = languagePreference.value;
    }

    public String getISOCode() {
        return ISOCode;
    }

    public void setISOCode(String ISOCode) {
        this.ISOCode = ISOCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
