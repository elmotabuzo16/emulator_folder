package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class ApplicationData extends RealmObject implements Model {
    private String typeCode;
    private String name;
    private int typeKey;

    @SuppressWarnings("unused") // required by realm
    public ApplicationData() {
    }

    public ApplicationData(String typeCode, String name, int typeKey) {
        this.typeCode = typeCode;
        this.name = name;
        this.typeKey = typeKey;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public String getName() {
        return name;
    }

    public int getTypeKey() {
        return typeKey;
    }
}
