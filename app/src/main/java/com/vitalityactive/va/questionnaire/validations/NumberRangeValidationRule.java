package com.vitalityactive.va.questionnaire.validations;

import android.support.annotation.Nullable;

import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.questionnaire.dependencies.Operator;
import com.vitalityactive.va.utilities.TextUtilities;

class NumberRangeValidationRule implements ValidationRule {
    private final float value;
    private final Operator operator;

    public NumberRangeValidationRule(Operator operator, float number) {
        this.value = number;
        this.operator = operator;
    }

    @Override
    public boolean isValid(Answer answer) {
        try {
            String activeAnswerValue = answer.getActiveAnswerValue();
            if (!TextUtilities.isNullOrWhitespace(activeAnswerValue)) {
                return isValid(Float.parseFloat(activeAnswerValue));
            }

            return isValid(answer.getFloatValue());
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    @Nullable
    @Override
    public Float getLowerLimit(Answer answer) {
        if (operator == Operator.GREATER_THAN || operator == Operator.GREATER_THAN_OR_EQUALS) {
            return value;
        }
        return null;
    }

    @Nullable
    @Override
    public Float getUpperLimit(Answer answer) {
        if (operator == Operator.LESS_THAN || operator == Operator.LESS_THAN_OR_EQUALS) {
            return value;
        }
        return null;
    }

    private boolean isValid(float answer) {
        switch (operator) {
            case LESS_THAN:
                return answer < value;
            case LESS_THAN_OR_EQUALS:
                return answer <= value;
            case GREATER_THAN:
                return answer > value;
            case GREATER_THAN_OR_EQUALS:
                return answer >= value;
            default:
                // todo: defaults to valid
                return true;
        }
    }

    @Override
    public String toString() {
        return String.format("accept any floating point number that is %s %s", operator, value);
    }
}
