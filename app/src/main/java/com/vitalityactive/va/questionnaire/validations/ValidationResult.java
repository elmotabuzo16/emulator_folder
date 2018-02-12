package com.vitalityactive.va.questionnaire.validations;

import android.support.annotation.Nullable;

public class ValidationResult {
    private final boolean valid;
    @Nullable
    private final Float lowerLimit;
    @Nullable
    private final Float upperLimit;

    public ValidationResult(boolean valid, @Nullable Float lowerLimit, @Nullable Float upperLimit) {
        this.valid = valid;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
    }

    public ValidationResult(boolean valid) {
        this.valid = valid;
        lowerLimit = null;
        upperLimit = null;
    }

    public boolean isValid() {
        return valid;
    }

    @Nullable
    public Float getLowerLimit() {
        return lowerLimit;
    }

    @Nullable
    public Float getUpperLimit() {
        return upperLimit;
    }
}
