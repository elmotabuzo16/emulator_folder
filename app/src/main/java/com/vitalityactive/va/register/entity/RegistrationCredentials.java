package com.vitalityactive.va.register.entity;

import android.support.annotation.NonNull;

public class RegistrationCredentials {
    private Username username = new Username("");
    private Password password = new Password("");
    private Password confirmationPassword = new Password("");
    private InsurerCode insurerCode = new InsurerCode("");

    public RegistrationCredentials() {

    }

    public RegistrationCredentials(Username username, Password password, InsurerCode insurerCode) {
        this.username = username;
        this.password = password;
        confirmationPassword = new Password(password.getText());
        this.insurerCode = insurerCode;
    }

    public boolean canRegister() {
        return username.isValid() && password.isValid() && isConfirmationPasswordValid() && insurerCode.isValid();
    }

    public void setUsername(@NonNull CharSequence text) {
        username.setText(text);
    }

    public void setPassword(@NonNull CharSequence text) {
        password.setText(text);
    }

    public void setConfirmationPassword(@NonNull CharSequence text) {
        confirmationPassword.setText(text);
    }

    public void setInsurerCode(@NonNull CharSequence text) {
        insurerCode.setText(text);
    }

    public boolean isUsernameValid() {
        return username.isValid();
    }

    public boolean isPasswordValid() {
        return password.isValid();
    }

    public boolean isConfirmationPasswordValid() {
        return !confirmationPassword.isEmpty() && confirmationPassword.equals(password);
    }

    public boolean isInsurerCodeValid() {
        return insurerCode.isValid();
    }

    public boolean passwordsDiffer() {
        return !password.equals(confirmationPassword);
    }

    public Username getUsername() {
        return username;
    }

    public Password getPassword() {
        return password;
    }

    public InsurerCode getInsurerCode() {
        return insurerCode;
    }

    public Password getConfirmationPassword() {
        return confirmationPassword;
    }
}
