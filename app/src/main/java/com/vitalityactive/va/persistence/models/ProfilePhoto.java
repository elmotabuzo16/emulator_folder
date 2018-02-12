package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.persistence.Model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProfilePhoto extends RealmObject implements Model {
    @PrimaryKey
    private String id;
    private String referenceId;
    private String uri;

    public ProfilePhoto() {

    }

    public ProfilePhoto(String uri) {
        this.uri = uri;
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public String getUri() {
        return uri;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
