package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Telephone extends RealmObject implements Model {
    private String extension;
    private String contactNumber;
    private RealmList<ContactRole> contactRoles;
    private String countryDialCode;
    private String areaDialCode;
    private String effectiveFrom;
    private String effectiveTo;

    public Telephone() {

    }

    public Telephone(LoginServiceResponse.Telephone telephone) {
        extension = telephone.extension;
        contactNumber = telephone.contactNumber;
        countryDialCode = telephone.countryDialCode;
        areaDialCode = telephone.areaDialCode;
        effectiveFrom = telephone.effectiveFrom;
        effectiveTo = telephone.effectiveTo;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public RealmList<ContactRole> getContactRoles() {
        return contactRoles;
    }

    public void setContactRoles(RealmList<ContactRole> contactRoles) {
        this.contactRoles = contactRoles;
    }

    public String getCountryDialCode() {
        return countryDialCode;
    }

    public void setCountryDialCode(String countryDialCode) {
        this.countryDialCode = countryDialCode;
    }

    public String getAreaDialCode() {
        return areaDialCode;
    }

    public void setAreaDialCode(String areaDialCode) {
        this.areaDialCode = areaDialCode;
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
