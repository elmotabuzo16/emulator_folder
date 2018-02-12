package com.vitalityactive.va.questionnaire.dependencies;

class NumberDependencyRule extends DependencyRule {
    private final float value;

    public NumberDependencyRule(long parentQuestionId, Operator operator, float value) {
        super(parentQuestionId, operator);
        this.value = value;
    }

    @Override
    public boolean isMet(String parentValue) {
        return parentValue != null && isMet(Float.valueOf(parentValue));
    }

    private boolean isMet(float parentValue) {
        switch (operator) {
            case EQUALS:
                return valueEquals(parentValue);
            case GREATER_THAN:
                return parentValue > value;
            case GREATER_THAN_OR_EQUALS:
                return parentValue >= value;
            case LESS_THAN:
                return parentValue < value;
            case LESS_THAN_OR_EQUALS:
                return parentValue <= value;
            case NOT_EQUAL_TO:
                return valueNotEqualTo(parentValue);
        }
        return false;
    }

    private boolean valueNotEqualTo(float parentValue) {
        return Math.abs(parentValue - value) > Float.MIN_VALUE;
    }

    private boolean valueEquals(float parentValue) {
        return Math.abs(parentValue - value) <= Float.MIN_VALUE;
    }

    public float getValue() {
        return value;
    }
}
