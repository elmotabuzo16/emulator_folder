package com.vitalityactive.va.snv.confirmandsubmit.model;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class PresentableSNVField {

    @Nullable
    private final String fieldName;
    @NonNull
    private final String captureDate;

    public PresentableSNVField(@Nullable String fieldName, @NonNull String captureDate) {
        this.fieldName = fieldName;
        this.captureDate = captureDate;
    }

    @Nullable
    public String getFieldName() {
        return fieldName;
    }

    @NonNull
    public String getCaptureDate() {
        return captureDate;
    }
}