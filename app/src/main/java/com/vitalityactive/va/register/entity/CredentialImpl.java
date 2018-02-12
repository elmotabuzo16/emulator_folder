package com.vitalityactive.va.register.entity;

import android.support.annotation.NonNull;

public abstract class CredentialImpl implements Credential {
    @NonNull
    protected CharSequence text;

    public CredentialImpl(@NonNull CharSequence text) {
        this.text = text;
    }

    @NonNull
    public CharSequence getText() {
        return text;
    }

    public void setText(@NonNull CharSequence text) {
        this.text = text;
    }

    public boolean isEmpty() {
        return text.length() == 0;
    }

    public abstract boolean isValid();

    @Override
    public String toString() {
        return getText().toString();
    }
}
