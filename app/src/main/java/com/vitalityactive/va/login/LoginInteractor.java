package com.vitalityactive.va.login;

import android.support.annotation.NonNull;

import com.vitalityactive.va.register.entity.Password;
import com.vitalityactive.va.register.entity.Username;

public interface LoginInteractor {

    void logIn(@NonNull Username username, @NonNull Password password);

    boolean areLoginCredentialsValid(@NonNull Username username, @NonNull Password password);

    boolean isBusyLoggingIn();

    LoginRequestResult getLoginRequestResult();

    void setCurrentUserHasSeenOnboarding();

    String getPartyId();

    String getTenantId();

    enum LoginRequestResult {
        NONE,
        CONNECTION_ERROR,
        GENERIC_ERROR,
        INCORRECT_USERNAME_OR_PASSWORD,
        LOCKED_ACCOUNT,
        SUCCESSFUL
    }
}
