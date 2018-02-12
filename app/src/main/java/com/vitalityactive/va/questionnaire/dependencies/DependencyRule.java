package com.vitalityactive.va.questionnaire.dependencies;

public abstract class DependencyRule {
    private final long parentQuestionId;
    protected Operator operator;

    public DependencyRule(long parentQuestionId, Operator operator) {
        this.parentQuestionId = parentQuestionId;
        this.operator = operator;
    }

    public abstract boolean isMet(String parentValue);

    public long getParentQuestionId() {
        return parentQuestionId;
    }

    public Operator getOperator() {
        return operator;
    }
}
