package com.vitalityactive.va.persistence.models.base;

import io.realm.RealmObject;

public class RealmInt extends RealmObject {
    private Integer value;

    public RealmInt() {}

    public RealmInt(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}