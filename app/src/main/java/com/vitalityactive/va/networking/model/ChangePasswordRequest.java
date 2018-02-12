package com.vitalityactive.va.networking.model;

import com.vitalityactive.va.register.entity.EmailAddress;
import com.vitalityactive.va.register.entity.Password;

public class ChangePasswordRequest {
    private String existingPassword;
    private String newPassword;
    private String userName;

    public ChangePasswordRequest(Password existingPassword, Password newPassword, EmailAddress userName) {
        this.existingPassword = existingPassword.toString();
        this.newPassword = newPassword.toString();
        this.userName = userName.toString();
    }
}
