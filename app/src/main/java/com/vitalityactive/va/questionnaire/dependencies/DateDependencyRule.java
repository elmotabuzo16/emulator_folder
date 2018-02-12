package com.vitalityactive.va.questionnaire.dependencies;

import android.support.annotation.NonNull;

import com.vitalityactive.va.utilities.date.LocalDate;

class DateDependencyRule extends DependencyRule {
    @NonNull
    private final LocalDate value;

    public DateDependencyRule(long parentQuestionId, Operator operator, String value) {
        super(parentQuestionId, operator);
        this.value = new LocalDate(value);
    }

    @Override
    public boolean isMet(String parentValue) {
        switch (operator) {
            case NOT_EQUAL_TO:
                return !valueEquals(parentValue);
            case GREATER_THAN:
                return parentValue == null || compare(parentValue) > 0;
            case GREATER_THAN_OR_EQUALS:
                return parentValue == null || compare(parentValue) >= 0;
            case LESS_THAN:
                return parentValue == null || compare(parentValue) < 0;
            case LESS_THAN_OR_EQUALS:
                return parentValue == null || compare(parentValue) <= 0;
            default:
                return valueEquals(parentValue);
        }
    }

    private int compare(String parentValue) {
        return value.compareTo(new LocalDate(parentValue));
    }

    private boolean valueEquals(String parentValue) {
        return parentValue != null && value.equals(LocalDate.create(parentValue));
    }

    @NonNull
    public LocalDate getValue() {
        return value;
    }
}
