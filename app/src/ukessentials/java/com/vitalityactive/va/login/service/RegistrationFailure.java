package com.vitalityactive.va.login.service;

public enum RegistrationFailure {
    UNKNOWN, NEED_MORE_INFO, INVALID_TOKEN, INVALID_DATE_OF_BIRTH, INVALID_ENTITY_NUMBER, INVALID_REGISTRATION_CODE, USER_ALREADY_REGISTERED;

    public static RegistrationFailure fromCode(int code) {
        switch (code) {
            case 1:     // Need more information
                return RegistrationFailure.NEED_MORE_INFO;
            case 3:     // Invalid token
            case 6:     // Token required.
                return RegistrationFailure.INVALID_TOKEN;
            case 2:     // An incorrect email or registration code was captured
                return RegistrationFailure.INVALID_REGISTRATION_CODE;
            case 4:     // Discovery Entity Number validation failed.
            case 14:    // Party not found.
                return RegistrationFailure.INVALID_ENTITY_NUMBER;
            case 13:    // Date of birth validation failed with V1.
            case 15:    // Date of birth validation failed with VHS.
            case 16:    // Date of birth missing. Please provide the date of birth.
                return RegistrationFailure.INVALID_DATE_OF_BIRTH;
            case 409:   // User already registered
                return RegistrationFailure.USER_ALREADY_REGISTERED;
            case 5:     // Failed to decrypt the registration code. No party id.
                return RegistrationFailure.UNKNOWN;
        }
        return RegistrationFailure.UNKNOWN;
    }
}
