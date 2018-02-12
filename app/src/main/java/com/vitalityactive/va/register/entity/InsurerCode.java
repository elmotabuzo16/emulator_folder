package com.vitalityactive.va.register.entity;

import android.support.annotation.NonNull;

import com.vitalityactive.va.utilities.TextUtilities;

public class InsurerCode extends CredentialImpl {
    public InsurerCode(@NonNull CharSequence text) {
        super(text);
    }

    @Override
    public boolean isValid() {
        return !TextUtilities.isNullOrWhitespace(text);
    }
}
