package com.vitalityactive.va.vhc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

class PresentableCapturedField {
    @NonNull
    final String value;
    @Nullable
    final String fieldName;
    @NonNull
    final String captureDate;

    PresentableCapturedField(@NonNull String value, @Nullable String fieldName, @NonNull String captureDate) {
        this.value = value;
        this.fieldName = fieldName;
        this.captureDate = captureDate;
    }

}
