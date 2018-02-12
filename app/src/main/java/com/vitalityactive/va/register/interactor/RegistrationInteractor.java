package com.vitalityactive.va.register.interactor;

import android.support.annotation.NonNull;

import com.vitalityactive.va.register.entity.Credential;

public interface RegistrationInteractor {
    void setUsername(@NonNull CharSequence username);

    void setPassword(@NonNull CharSequence password);

    void setConfirmationPassword(@NonNull CharSequence confirmationPassword);

    void setInsurerCode(@NonNull CharSequence insurerCode);

    boolean isUsernameValid();

    boolean isPasswordValid();

    boolean isInsurerCodeValid();

    boolean canRegister();

    boolean passwordsDiffer();

    void register();

    Username getUsername();

    Password getPassword();

    ConfirmationPassword getConfirmationPassword();

    InsurerCode getInsurerCode();

    boolean isRegistering();

    RegistrationRequestResult getRegistrationRequestResult();

    interface Username extends Credential {
    }

    interface Password extends Credential {
    }

    interface ConfirmationPassword extends Credential {
    }

    interface InsurerCode extends Credential {
    }

    class RegistrationSucceededEvent {
    }

    class RegistrationFailedEvent {
        private RegistrationRequestResult registrationRequestResult;

        public RegistrationFailedEvent() {
            this(RegistrationRequestResult.GENERIC_ERROR);
        }

        public RegistrationFailedEvent(RegistrationRequestResult registrationRequestResult) {
            this.registrationRequestResult = registrationRequestResult;
        }

        public RegistrationRequestResult getRegistrationRequestResult() {
            return registrationRequestResult;
        }
    }

    enum RegistrationRequestResult {
        NONE,
        CONNECTION_ERROR,
        GENERIC_ERROR,
        INVALID_EMAIL_INSURER_CODE_ERROR,
        ALREADY_REGISTERED_ERROR,
        SUCCESSFUL
    }
}
