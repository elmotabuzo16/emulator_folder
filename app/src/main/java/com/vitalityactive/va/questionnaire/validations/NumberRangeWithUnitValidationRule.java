package com.vitalityactive.va.questionnaire.validations;

import android.support.annotation.Nullable;

import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.questionnaire.dependencies.Operator;

class NumberRangeWithUnitValidationRule extends NumberRangeValidationRule {
    private final String unitKey;

    public NumberRangeWithUnitValidationRule(Operator operator, float number, String unitKey) {
        super(operator, number);
        this.unitKey = unitKey;
    }

    @Override
    public boolean isValid(Answer answer) {
        String answeredUnit = answer.getUnitOfMeasureKey();
        if (doesNotHaveASelectedUnitOfMeasure(answeredUnit))
            return false;

        if (isAnsweredForDifferentUnitOfMeasure(answeredUnit))
            return true;

        return super.isValid(answer);
    }

    @Nullable
    @Override
    public Float getLowerLimit(Answer answer) {
        if (answer.getUnitOfMeasureKey() != null && answer.getUnitOfMeasureKey().equals(unitKey)) {
            return super.getLowerLimit(answer);
        }
        return null;
    }

    @Nullable
    @Override
    public Float getUpperLimit(Answer answer) {
        if (answer.getUnitOfMeasureKey() != null && answer.getUnitOfMeasureKey().equals(unitKey)) {
            return super.getUpperLimit(answer);
        }
        return null;
    }

    private boolean doesNotHaveASelectedUnitOfMeasure(String answeredUnit) {
        return answeredUnit == null || answeredUnit.isEmpty();
    }

    private boolean isAnsweredForDifferentUnitOfMeasure(String answeredUnit) {
        return !answeredUnit.equals(unitKey);
    }

    @Override
    public String toString() {
        return String.format("(%s on unit with feedbackKey %s) or (any value on any other unit feedbackKey)", super.toString(), unitKey);
    }
}
