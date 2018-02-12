package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class User extends RealmObject implements Model {
    private long partyId;
    private String middleNames;
    private String gender;
    private String dateOfBirth;
    private String givenName;
    private String familyName;
    private String language;
    private String preferredName;
    private String title;
    private String suffix;
    private String username;
    private String vitalityMembershipId;
    private String accessToken;
    private String partnerRefreshToken;

    public User() {

    }

    public User(LoginServiceResponse.PartyDetails partyDetails, Long vitalityMembershipId, String username) {
        this.accessToken = partyDetails.accessToken;
        this.partyId = partyDetails.partyId;
        this.partnerRefreshToken = partyDetails.partnerRefreshToken;
        LoginServiceResponse.Person person = partyDetails.person;
        middleNames = person.middleNames;
        gender = person.genderTypeKey;
        dateOfBirth = person.bornOn;
        givenName = person.givenName;
        familyName = person.familyName;
        language = person.language;
        preferredName = person.preferredName;
        title = person.titleTypeKey;
        suffix = person.suffix;
        this.vitalityMembershipId = vitalityMembershipId.toString();
        this.username = username;
    }

    public long getPartyId() {
        return partyId;
    }

    public String getUsername() {
        return username;
    }

    public String getMiddleNames() {
        return middleNames;
    }

    public void setMiddleNames(String middleNames) {
        this.middleNames = middleNames;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getVitalityMembershipId() {
        return vitalityMembershipId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getPartnerRefreshToken() {
        return partnerRefreshToken;
    }
}
