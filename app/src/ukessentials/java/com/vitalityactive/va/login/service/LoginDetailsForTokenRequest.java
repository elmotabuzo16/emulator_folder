package com.vitalityactive.va.login.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.vitalityactive.va.register.ActivationValues;
import com.vitalityactive.va.utilities.TextUtilities;

class LoginDetailsForTokenRequest {
    @NonNull
    @SerializedName("token")
    public final String token;

    @Nullable
    @SerializedName("authorizationCode")
    public String authorizationCode = null;

    @Nullable
    @SerializedName("validation")
    public ValidationItem[] validations = null;

    LoginDetailsForTokenRequest(@NonNull String token) {
        this.token = token;
    }

    LoginDetailsForTokenRequest(@NonNull String token, @NonNull ActivationValues activationValues) {
        this(token);
        authorizationCode = activationValues.authenticationCode;
        if (TextUtilities.isNullOrWhitespace(activationValues.entityNumber)) {
            validations = new ValidationItem[]{
                    ValidationItem.dateOfBirth(activationValues.dateOfBirth)
            };
        } else {
            validations = new ValidationItem[]{
                    ValidationItem.dateOfBirth(activationValues.dateOfBirth),
                    ValidationItem.entityNumber(activationValues.entityNumber)
            };
        }
    }

    public static class ValidationItem {
        @SerializedName("typeKey")
        public final int typeKey;

        @NonNull
        @SerializedName("typeCode")
        public final String typeCode;

        @NonNull
        @SerializedName("value")
        public final String value;

        private ValidationItem(int typeKey, @NonNull String typeCode, @NonNull String value) {
            this.typeKey = typeKey;
            this.typeCode = typeCode;
            this.value = value;
        }

        @NonNull
        private static ValidationItem dateOfBirth(String dateOfBirth) {
            return new ValidationItem(1, "DOB", dateOfBirth);
        }

        @NonNull
        private static ValidationItem entityNumber(String entityNumber) {
            return new ValidationItem(2, "DEN", entityNumber);
        }
    }
}
