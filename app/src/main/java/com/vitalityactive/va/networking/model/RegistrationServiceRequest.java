package com.vitalityactive.va.networking.model;

import com.google.gson.annotations.SerializedName;
import com.vitalityactive.va.register.entity.RegistrationCredentials;

public class RegistrationServiceRequest {
    @SerializedName("userName")
    public final CharSequence username;

    public final CharSequence password;

    @SerializedName("registrationCode")
    public final CharSequence insurerCode;

    public RegistrationServiceRequest(RegistrationCredentials registrationCredentials) {
        username = registrationCredentials.getUsername().toString();
        password = registrationCredentials.getPassword().toString();
        insurerCode = registrationCredentials.getInsurerCode().toString();
    }
}
