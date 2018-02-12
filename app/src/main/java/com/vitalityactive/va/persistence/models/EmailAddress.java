package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class EmailAddress extends RealmObject implements Model {
    private String value;
    private RealmList<ContactRole> contactRoles;
    private String effectiveFrom;
    private String effectiveTo;

    public EmailAddress() {

    }

    public EmailAddress(LoginServiceResponse.EmailAddress emailAddress) {
        value = emailAddress.value;
        effectiveFrom = emailAddress.effectiveFrom;
        effectiveTo = emailAddress.effectiveTo;
        contactRoles = new RealmList<>();
        if (emailAddress.contactRoles != null &&
                !emailAddress.contactRoles.isEmpty()) {
            for (LoginServiceResponse.ContactRole contactRole : emailAddress.contactRoles) {
                contactRoles.add(new ContactRole(contactRole));
            }
        }
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public RealmList<ContactRole> getContactRoles() {
        return contactRoles;
    }

    public void setContactRoles(RealmList<ContactRole> contactRoles) {
        this.contactRoles = contactRoles;
    }
}
