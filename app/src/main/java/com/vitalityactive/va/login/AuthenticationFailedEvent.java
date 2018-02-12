package com.vitalityactive.va.login;

public class AuthenticationFailedEvent {
    private LoginInteractor.LoginRequestResult loginRequestResult;

    public AuthenticationFailedEvent(LoginInteractor.LoginRequestResult loginRequestResult) {
        this.loginRequestResult = loginRequestResult;
    }

    public LoginInteractor.LoginRequestResult getLoginRequestResult() {
        return loginRequestResult;
    }
}
