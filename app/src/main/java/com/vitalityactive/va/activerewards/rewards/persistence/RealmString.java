package com.vitalityactive.va.activerewards.rewards.persistence;

import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class RealmString extends RealmObject implements Model {
    public String value;

    public RealmString() {}

    public RealmString(String value) {
        this.value = value;
    }
}
