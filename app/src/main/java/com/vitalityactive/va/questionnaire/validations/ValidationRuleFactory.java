package com.vitalityactive.va.questionnaire.validations;

import com.vitalityactive.va.questionnaire.dependencies.Operator;

public class ValidationRuleFactory {
    public static ValidationRule numberGreaterThanOrEqual(float number) {
        return new NumberRangeValidationRule(Operator.GREATER_THAN_OR_EQUALS, number);
    }

    public static ValidationRule numberGreaterThanOrEqual(float number, String unitOfMeasureKey) {
        return new NumberRangeWithUnitValidationRule(Operator.GREATER_THAN_OR_EQUALS, number, unitOfMeasureKey);
    }

    public static ValidationRule numberLessThanOrEqual(float number) {
        return new NumberRangeValidationRule(Operator.LESS_THAN_OR_EQUALS, number);
    }

    public static ValidationRule numberLessThanOrEqual(float number, String unitOfMeasureKey) {
        return new NumberRangeWithUnitValidationRule(Operator.LESS_THAN_OR_EQUALS, number, unitOfMeasureKey);
    }

    public static ValidationRule anything() {
        return new AnythingValidationRule();
    }

    public static ValidationRule nothing(String reason) {
        return new NothingValidationRule(reason);
    }
}
