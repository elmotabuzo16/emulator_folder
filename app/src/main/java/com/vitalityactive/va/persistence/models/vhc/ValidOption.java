package com.vitalityactive.va.persistence.models.vhc;

import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class ValidOption extends RealmObject implements Model {
    private String value;

    public ValidOption() {

    }

    public ValidOption(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
