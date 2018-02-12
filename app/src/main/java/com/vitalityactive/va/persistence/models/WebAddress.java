package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class WebAddress extends RealmObject implements Model {
    private String URL;
    private RealmList<ContactRole> contactRoles;
    private String effectiveFrom;
    private String effectiveTo;

    public WebAddress() {

    }

    public WebAddress(LoginServiceResponse.WebAddress webAddress) {
        URL = webAddress.uRL;
        effectiveFrom = webAddress.effectiveFrom;
        effectiveTo = webAddress.effectiveTo;
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(String effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public String getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(String effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public RealmList<ContactRole> getContactRoles() {
        return contactRoles;
    }

    public void setContactRoles(RealmList<ContactRole> contactRoles) {
        this.contactRoles = contactRoles;
    }
}
