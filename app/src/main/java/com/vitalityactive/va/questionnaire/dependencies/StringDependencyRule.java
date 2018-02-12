package com.vitalityactive.va.questionnaire.dependencies;

import android.support.annotation.NonNull;

class StringDependencyRule extends DependencyRule {
    @NonNull
    private final String value;

    public StringDependencyRule(long parentQuestionId, @NonNull String value) {
        this(parentQuestionId, Operator.EQUALS, value);
    }

    public StringDependencyRule(long parentQuestionId, Operator operator, @NonNull String value) {
        super(parentQuestionId, operator);
        this.value = value;
    }

    @Override
    public boolean isMet(String parentValue) {
        switch (operator) {
            case NOT_EQUAL_TO:
                return parentValue == null || !parentValue.equals(value);
            default:
                return parentValue != null && parentValue.equals(value);
        }
    }

    @NonNull
    public String getValue() {
        return value;
    }
}
