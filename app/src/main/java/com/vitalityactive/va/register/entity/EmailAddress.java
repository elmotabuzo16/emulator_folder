package com.vitalityactive.va.register.entity;

import android.support.annotation.NonNull;

import com.vitalityactive.va.utilities.ValidationPatterns;

public class EmailAddress extends CredentialImpl {
    public EmailAddress(@NonNull CharSequence text) {
        super(text);
    }

    public boolean isValid() {
        return ValidationPatterns.EMAIL_ADDRESS.matcher(text).matches() && isSingleByte();
    }

    // Allow only single byte characters
    private boolean isSingleByte() {
        for (int i=0; i<text.length(); i++) {
            if (String.valueOf(text.charAt(i)).getBytes().length != 1) {
                return false;
            }
        }
        return true;
    }
}
