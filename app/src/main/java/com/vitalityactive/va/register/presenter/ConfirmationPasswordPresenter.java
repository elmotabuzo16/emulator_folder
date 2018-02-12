package com.vitalityactive.va.register.presenter;

import android.support.annotation.NonNull;

public class ConfirmationPasswordPresenter extends CredentialPresenterBase implements CredentialPresenterCallback {
    public ConfirmationPasswordPresenter(Builder builder) {
        super(builder);
    }

    @Override
    protected void setCredential(@NonNull CharSequence value) {
        registrationInteractor.setConfirmationPassword(value);
    }

    @Override
    protected CharSequence getValue() {
        return registrationInteractor.getConfirmationPassword().getText();
    }

    @Override
    public boolean shouldShowValidationErrorMessage() {
        return isDirty && registrationInteractor.passwordsDiffer();
    }

    @Override
    public void onCredentialChanged() {
        if (shouldShowValidationErrorMessage()) {
            credentialUserInterface.showValidationErrorMessage();
        } else {
            credentialUserInterface.hideValidationErrorMessage();
        }
    }

    public static class Builder extends CredentialPresenterBaseBuilder {
        @Override
        public CredentialPresenter build() {
            return new ConfirmationPasswordPresenter(this);
        }
    }
}
