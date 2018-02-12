package com.vitalityactive.va.networking.model;

public class ChangeEntityNumberRequest {

    private String partyId;
    private String dateOfBirth;
    private String entityNumber;

    public ChangeEntityNumberRequest(String partyId, String dateOfBirth, String entityNumber) {
        this.partyId = partyId;
        this.dateOfBirth = dateOfBirth;
        this.entityNumber = entityNumber;
    }
}
