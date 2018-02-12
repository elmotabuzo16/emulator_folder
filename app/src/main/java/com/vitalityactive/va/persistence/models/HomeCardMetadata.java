package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.home.service.HomeScreenCardStatusResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class HomeCardMetadata extends RealmObject implements Model {
    private int typeKey;
    private String value;

    public HomeCardMetadata() {

    }

    public HomeCardMetadata(HomeScreenCardStatusResponse.Metadata metadata) {
        typeKey = metadata.typeKey;
        value = metadata.value;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(int typeKey) {
        this.typeKey = typeKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
