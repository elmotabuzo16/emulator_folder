package com.vitalityactive.va.login.callback;

import android.support.annotation.NonNull;

import com.vitalityactive.va.login.service.RegistrationFailure;

public class UkeLoginEvent {
    private final RegistrationRequestResult requestResult;
    public final RegistrationFailure validationError;

    UkeLoginEvent(RegistrationRequestResult requestResult) {
        this.requestResult = requestResult;
        validationError = null;
    }

    public UkeLoginEvent(RegistrationRequestResult requestResult, RegistrationFailure error) {
        this.requestResult = requestResult;
        this.validationError = error;
    }

    @NonNull
    static UkeLoginEvent genericError() {
        return new UkeLoginEvent(RegistrationRequestResult.GENERIC_ERROR);
    }

    @NonNull
    static UkeLoginEvent connectionError() {
        return new UkeLoginEvent(RegistrationRequestResult.CONNECTION_ERROR);
    }

    @NonNull
    static UkeLoginEvent needMoreInformation() {
        return new UkeLoginEvent(RegistrationRequestResult.NEED_MORE_INFORMATION);
    }

    @NonNull
    static UkeLoginEvent invalidActivationDetails(RegistrationFailure error) {
        return new UkeLoginEvent(RegistrationRequestResult.INVALID_DETAILS, error);
    }

    @NonNull
    public static UkeLoginEvent success() {
        return new UkeLoginEvent(RegistrationRequestResult.SUCCESS);
    }

    public RegistrationRequestResult getRequestResult() {
        return requestResult;
    }

    public static UkeLoginEvent invalidToken() {
        return new UkeLoginEvent(RegistrationRequestResult.INVALID_TOKEN);
    }

    public enum RegistrationRequestResult {
        SUCCESS,
        CONNECTION_ERROR,
        GENERIC_ERROR,
        INVALID_TOKEN,
        INVALID_DETAILS,
        NEED_MORE_INFORMATION
    }
}
