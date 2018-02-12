package com.vitalityactive.va.register.presenter;

import android.support.annotation.NonNull;

public class UsernamePresenter extends CredentialPresenterBase {
    private UsernamePresenter(Builder builder) {
        super(builder);
    }

    @Override
    protected void setCredential(@NonNull CharSequence value) {
        registrationInteractor.setUsername(value);
    }

    @Override
    protected CharSequence getValue() {
        return registrationInteractor.getUsername().getText();
    }

    @Override
    public boolean shouldShowValidationErrorMessage() {
        return isDirty && !registrationInteractor.isUsernameValid();
    }

    public static class Builder extends CredentialPresenterBaseBuilder {
        @Override
        public CredentialPresenter build() {
            return new UsernamePresenter(this);
        }
    }
}
