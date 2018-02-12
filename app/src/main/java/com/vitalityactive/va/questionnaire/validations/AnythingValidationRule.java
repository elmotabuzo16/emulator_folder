package com.vitalityactive.va.questionnaire.validations;

import android.support.annotation.Nullable;

import com.vitalityactive.va.questionnaire.Answer;

class AnythingValidationRule implements ValidationRule {
    @Override
    public boolean isValid(Answer answer) {
        return true;
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
        return "accept any question that is answered as valid";
    }
}
