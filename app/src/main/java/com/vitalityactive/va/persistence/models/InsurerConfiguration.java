package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class InsurerConfiguration extends RealmObject implements Model {
    private long tenantId;

    public InsurerConfiguration() {

    }

    public InsurerConfiguration(Long tenantId) {
        this.tenantId = tenantId;
    }

    public long getTenantId() {
        return tenantId;
    }
}
