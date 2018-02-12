package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.networking.model.LoginServiceResponse;

import io.realm.RealmObject;

public class ContactRole extends RealmObject {
    private String availabilityFrom;
    private String availabilityTo;
    private String rolePurposeType;
    private String roleType;
    private String effectiveFrom;
    private String effectiveTo;

    public ContactRole() {

    }

    public ContactRole(LoginServiceResponse.ContactRole contactRole) {
        availabilityFrom = contactRole.availabilityFrom;
        availabilityTo = contactRole.availabilityTo;
        rolePurposeType = contactRole.rolePurposeType;
        roleType = contactRole.roleType;
        effectiveFrom = contactRole.effectiveFrom;
        effectiveTo = contactRole.effectiveTo;
    }

    public String getAvailabilityFrom() {
        return availabilityFrom;
    }

    public void setAvailabilityFrom(String availabilityFrom) {
        this.availabilityFrom = availabilityFrom;
    }

    public String getAvailabilityTo() {
        return availabilityTo;
    }

    public void setAvailabilityTo(String availabilityTo) {
        this.availabilityTo = availabilityTo;
    }

    public String getRolePurposeType() {
        return rolePurposeType;
    }

    public void setRolePurposeType(String rolePurposeType) {
        this.rolePurposeType = rolePurposeType;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
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
}
