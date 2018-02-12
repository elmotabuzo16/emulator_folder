package com.vitalityactive.va.register.presenter;

import android.support.annotation.NonNull;

public class InsurerCodePresenter extends CredentialPresenterBase {
    public InsurerCodePresenter(Builder builder) {
        super(builder);
    }

    @Override
    protected void setCredential(@NonNull CharSequence value) {
        registrationInteractor.setInsurerCode(value);
    }

    @Override
    protected CharSequence getValue() {
        return registrationInteractor.getInsurerCode().getText();
    }

    @Override
    public boolean shouldShowValidationErrorMessage() {
        return isDirty && !registrationInteractor.isInsurerCodeValid();
    }

    public static class Builder extends CredentialPresenterBaseBuilder {
        @Override
        public CredentialPresenter build() {
            return new InsurerCodePresenter(this);
        }
    }
}
