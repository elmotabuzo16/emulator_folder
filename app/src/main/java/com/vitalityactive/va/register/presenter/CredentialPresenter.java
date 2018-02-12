package com.vitalityactive.va.register.presenter;

import com.vitalityactive.va.register.view.CredentialUserInterface;
import com.vitalityactive.va.register.view.RegistrationUserInterface;

public interface CredentialPresenter {
    void onValueChanged(CharSequence value);

    boolean shouldShowValidationErrorMessage();

    int getHintResourceId();

    CharSequence getFieldDescription();

    CharSequence getValidationMessage();

    int getIconResourceId();

    int getInputType();

    void onValueEntered();

    void bindWith(CredentialUserInterface credentialUserInterface);

    void setRegistrationUserInterface(RegistrationUserInterface registrationUserInterface);

    int getDisabledIconResourceId();
}
