package com.vitalityactive.va.questionnaire.dependencies;

class BooleanDependencyRule extends DependencyRule {
    private final boolean wantedValue;

    public BooleanDependencyRule(long parentQuestionId, boolean wantedValue) {
        this(parentQuestionId, Operator.EQUALS, wantedValue);
    }

    public BooleanDependencyRule(long parentQuestionId, Operator operator, boolean wantedValue) {
        super(parentQuestionId, operator);
        this.wantedValue = wantedValue;
    }

    @Override
    public boolean isMet(String parentValue) {
        return isMet(Boolean.parseBoolean(parentValue));
    }

    private boolean isMet(boolean parentValue) {
        switch (operator) {
            case NOT_EQUAL_TO:
                return parentValue != wantedValue;
            default:
                return parentValue == wantedValue;
        }
    }

    public boolean getValue() {
        return wantedValue;
    }
}
