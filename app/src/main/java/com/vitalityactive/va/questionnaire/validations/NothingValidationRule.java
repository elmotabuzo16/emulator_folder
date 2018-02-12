package com.vitalityactive.va.questionnaire.validations;

import android.support.annotation.Nullable;

import com.vitalityactive.va.questionnaire.Answer;

class NothingValidationRule implements ValidationRule {
    private final String reason;

    public NothingValidationRule(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean isValid(Answer answer) {
        return false;
    }

    @Nullable
    @Override
    public Float getLowerLimit(Answer answer) {
        return null;
    }

    @Nullable
    @Override
    public Float getUpperLimit(Answer answer) {
        return null;
    }

    @Override
    public String toString() {
        return "accept no answer: " + reason;
    }
}
