package com.vitalityactive.va.networking.model;

import com.vitalityactive.va.register.entity.Password;
import com.vitalityactive.va.register.entity.Username;

public class LoginServiceRequest {
    public LoginRequest loginRequest;

    public LoginServiceRequest(LoginRequest loginRequest) {
        this.loginRequest = loginRequest;
    }

    public static class LoginRequest {
        public String username;
        public String password;

        public LoginRequest(Username username, Password password) {
            this.username = username.toString();
            this.password = password.toString();
        }
    }
}
