package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class UserInstruction extends RealmObject implements Model {
    private String id;
    private String type;
    private String effectiveFrom;

    public UserInstruction() {

    }

    public UserInstruction(LoginServiceResponse.UserInstruction userInstruction) {
        id = userInstruction.id == null ? "0" : userInstruction.id.toString();
        type = userInstruction.typeKey;
        effectiveFrom = userInstruction.effectiveFrom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(String effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }
}
