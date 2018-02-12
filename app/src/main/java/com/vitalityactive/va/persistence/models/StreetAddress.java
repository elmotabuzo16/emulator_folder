package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class StreetAddress extends RealmObject implements Model {
    private String country;
    private boolean POBox;
    private String complex;
    private String postalCode;
    private String streetAddress1;
    private String unitNumber;
    private String streetAddress2;
    private RealmList<ContactRole> contactRoles;
    private String streetAddress3;
    private String place;
    private String effectiveFrom;
    private String effectiveTo;

    public StreetAddress() {

    }

    public StreetAddress(LoginServiceResponse.PhysicalAddress physicalAddress) {
        country = physicalAddress.country;
        POBox = physicalAddress.pOBox;
        complex = physicalAddress.complex;
        postalCode = physicalAddress.postalCode;
        streetAddress1 = physicalAddress.streetAddress1;
        streetAddress2 = physicalAddress.streetAddress2;
        streetAddress3 = physicalAddress.streetAddress3;
        unitNumber = physicalAddress.unitNumber;
        place = physicalAddress.place;
        effectiveFrom = physicalAddress.effectiveFrom;
        effectiveTo = physicalAddress.effectiveTo;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isPOBox() {
        return POBox;
    }

    public void setPOBox(boolean POBox) {
        this.POBox = POBox;
    }

    public String getComplex() {
        return complex;
    }

    public void setComplex(String complex) {
        this.complex = complex;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreetAddress1() {
        return streetAddress1;
    }

    public void setStreetAddress1(String streetAddress1) {
        this.streetAddress1 = streetAddress1;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getStreetAddress2() {
        return streetAddress2;
    }

    public void setStreetAddress2(String streetAddress2) {
        this.streetAddress2 = streetAddress2;
    }

    public RealmList<ContactRole> getContactRoles() {
        return contactRoles;
    }

    public void setContactRoles(RealmList<ContactRole> contactRoles) {
        this.contactRoles = contactRoles;
    }

    public String getStreetAddress3() {
        return streetAddress3;
    }

    public void setStreetAddress3(String streetAddress3) {
        this.streetAddress3 = streetAddress3;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
