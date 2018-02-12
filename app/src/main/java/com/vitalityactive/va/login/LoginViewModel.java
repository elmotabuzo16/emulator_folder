package com.vitalityactive.va.login;

public interface LoginViewModel {
    boolean isLoginEnabled();

    CharSequence getUsername();

    CharSequence getPassword();

    boolean shouldShowInvalidUsernameMessage();

    boolean shouldShowLoadingIndicator();
}
