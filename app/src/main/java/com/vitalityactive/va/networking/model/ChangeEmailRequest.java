package com.vitalityactive.va.networking.model;


import com.vitalityactive.va.register.entity.EmailAddress;
import com.vitalityactive.va.register.entity.Password;

public class ChangeEmailRequest {
    private String existingUserName;
    private String newUsername;
    private String password;

    public ChangeEmailRequest(EmailAddress existingUserName, EmailAddress newUsername, Password password) {
        this.existingUserName = existingUserName.toString();
        this.newUsername = newUsername.toString();
        this.password = password.toString();
    }
}