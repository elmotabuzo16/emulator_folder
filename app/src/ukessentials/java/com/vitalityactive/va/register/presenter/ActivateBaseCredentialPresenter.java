package com.vitalityactive.va.register.presenter;

import android.text.InputType;

import com.vitalityactive.va.register.view.ActivateAdapter;
import com.vitalityactive.va.register.view.CredentialUserInterface;
import com.vitalityactive.va.register.view.RegistrationUserInterface;

public abstract class ActivateBaseCredentialPresenter implements CredentialPresenter {
    protected String value;
    private ActivateAdapter adapter;

    @Override
    public void onValueChanged(CharSequence value) {
        if (value == null) {
            this.value = null;
        } else {
            this.value = value.toString();
        }
        adapter.onValuesChanged();
    }

    @Override
    public int getInputType() {
        return InputType.TYPE_CLASS_TEXT;
    }

    @Override
    public boolean shouldShowValidationErrorMessage() {
        return value == null || value.isEmpty();
    }

    @Override
    public CharSequence getValidationMessage() {
        return null;
    }

    @Override
    public void onValueEntered() {
    }

    @Override
    public void bindWith(CredentialUserInterface credentialUserInterface) {
        credentialUserInterface.setValue(getValue());
    }

    @Override
    public void setRegistrationUserInterface(RegistrationUserInterface registrationUserInterface) {
    }

    protected CharSequence getValue() {
        return value;
    }

    public String getStringValue() {
        return value;
    }

    public ActivateBaseCredentialPresenter setAdapter(ActivateAdapter adapter) {
        this.adapter = adapter;
        return this;
    }
}
