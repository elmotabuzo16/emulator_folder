package com.vitalityactive.va.questionnaire.validations;

import android.support.annotation.Nullable;

import com.vitalityactive.va.questionnaire.Answer;

public interface ValidationRule {
    boolean isValid(Answer answer);

    @Nullable
    Float getLowerLimit(Answer answer);

    @Nullable
    Float getUpperLimit(Answer answer);
}
