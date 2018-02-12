package com.vitalityactive.va.register.presenter;

import android.support.annotation.NonNull;

import com.vitalityactive.va.register.interactor.RegistrationInteractor;
import com.vitalityactive.va.register.view.CredentialUserInterface;
import com.vitalityactive.va.register.view.RegistrationUserInterface;

public abstract class CredentialPresenterBase implements CredentialPresenter {
    private final String description;
    private RegistrationUserInterface registrationUserInterface;
    private final CharSequence validationMessage;
    private final int iconResourceId;
    private int disabledIconResourceId;
    private final int hintResourceId;
    private final int inputType;
    final RegistrationInteractor registrationInteractor;
    private final CredentialPresenterCallback callback;
    boolean isDirty;
    CredentialUserInterface credentialUserInterface;

    public CredentialPresenterBase(CredentialPresenterBaseBuilder builder) {
        registrationUserInterface = builder.registrationUserInterface;
        hintResourceId = builder.hintResourceId;
        validationMessage = builder.validationMessage;
        iconResourceId = builder.iconResourceId;
        disabledIconResourceId = builder.disabledIconResourceId;
        inputType = builder.inputType;
        registrationInteractor = builder.registrationInteractor;
        callback = builder.callback;
        description = builder.description;
    }

    public void setRegistrationUserInterface(RegistrationUserInterface registrationUserInterface) {
        this.registrationUserInterface = registrationUserInterface;
    }

    protected abstract void setCredential(@NonNull CharSequence value);

    @Override
    public void bindWith(CredentialUserInterface credentialUserInterface) {
        this.credentialUserInterface = credentialUserInterface;
        credentialUserInterface.setValue(getValue());
        if (registrationInteractor.canRegister()) {
            registrationUserInterface.allowRegistration();
        } else {
            registrationUserInterface.disallowRegistration();
        }
    }

    protected abstract CharSequence getValue();

    @Override
    public void onValueChanged(CharSequence value) {
        setCredential(value == null ? "" : value);
        if (registrationInteractor.canRegister()) {
            registrationUserInterface.allowRegistration();
        } else {
            registrationUserInterface.disallowRegistration();
        }
        if (callback != null) {
            callback.onCredentialChanged();
        }
    }

    @Override
    public void onValueEntered() {
        isDirty = true;
    }

    @Override
    public int getHintResourceId() {
        return hintResourceId;
    }

    @Override
    public CharSequence getFieldDescription() {
        return description;
    }

    @Override
    public CharSequence getValidationMessage() {
        return validationMessage;
    }

    @Override
    public int getIconResourceId() {
        return iconResourceId;
    }

    @Override
    public int getDisabledIconResourceId() {
        return disabledIconResourceId;
    }

    @Override
    public int getInputType() {
        return inputType;
    }

    public String getDescription() {
        return description;
    }

    public abstract static class CredentialPresenterBaseBuilder {
        RegistrationUserInterface registrationUserInterface;
        int hintResourceId;
        CharSequence validationMessage;
        int iconResourceId;
        private int disabledIconResourceId;
        int inputType;
        RegistrationInteractor registrationInteractor;
        CredentialPresenterCallback callback;
        private String description;

        public CredentialPresenterBaseBuilder setRegistrationUserInterface(RegistrationUserInterface registrationUserInterface) {
            this.registrationUserInterface = registrationUserInterface;
            return this;
        }

        public CredentialPresenterBaseBuilder setHint(int hintResourceId) {
            this.hintResourceId = hintResourceId;
            return this;
        }

        public CredentialPresenterBaseBuilder setValidationMessage(CharSequence validationMessage) {
            this.validationMessage = validationMessage;
            return this;
        }

        public CredentialPresenterBaseBuilder setIconResourceId(int iconResourceId) {
            this.iconResourceId = iconResourceId;
            return this;
        }

        public CredentialPresenterBaseBuilder setDisabledIconResourceId(int disabledIconResourceId) {
            this.disabledIconResourceId = disabledIconResourceId;
            return this;
        }

        public CredentialPresenterBaseBuilder setInputType(int inputType) {
            this.inputType = inputType;
            return this;
        }

        public CredentialPresenterBaseBuilder setRegistrationInteractor(RegistrationInteractor registrationInteractor) {
            this.registrationInteractor = registrationInteractor;
            return this;
        }

        public CredentialPresenterBaseBuilder setFieldDescription(String description) {
            this.description = description;
            return this;
        }

        public CredentialPresenterBaseBuilder setCallback(CredentialPresenterCallback callback) {
            this.callback = callback;
            return this;
        }

        public abstract CredentialPresenter build();
    }
}
