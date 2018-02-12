package com.vitalityactive.va.constants;

import android.support.annotation.NonNull;

public enum BooleanString {

    TRUE("true"),
    FALSE("false");

    public final String value;

    BooleanString(String value) {
        this.value = value;
    }

    @NonNull
    public static String stringFromBoolean(boolean value) {
        return value ? TRUE.value : FALSE.value;
    }
}
