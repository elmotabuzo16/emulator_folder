package com.vitalityactive.va.register.presenter;

import android.support.annotation.NonNull;

public class PasswordPresenter extends CredentialPresenterBase {
    final CredentialPresenterCallback callback;

    private PasswordPresenter(Builder builder) {
        super(builder);
        callback = builder.callback;
    }

    @Override
    protected void setCredential(@NonNull CharSequence value) {
        registrationInteractor.setPassword(value);
        if (callback != null) {
            callback.onCredentialChanged();
        }
    }

    @Override
    protected CharSequence getValue() {
        return registrationInteractor.getPassword().getText();
    }

    @Override
    public boolean shouldShowValidationErrorMessage() {
        return isDirty && !registrationInteractor.isPasswordValid();
    }

    public static class Builder extends CredentialPresenterBaseBuilder {
        CredentialPresenterCallback callback;

        @Override
        public CredentialPresenter build() {
            return new PasswordPresenter(this);
        }
    }
}
