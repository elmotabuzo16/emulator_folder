package com.vitalityactive.va.questionnaire.dependencies;

public enum Operator {
    EQUALS,
    GREATER_THAN,
    GREATER_THAN_OR_EQUALS,
    LESS_THAN,
    LESS_THAN_OR_EQUALS,
    NOT_EQUAL_TO;

    public static Operator fromString(String operator) {
        switch (operator) {
            case "==":
                return EQUALS;
            case ">=":
                return GREATER_THAN_OR_EQUALS;
            case ">":
                return GREATER_THAN;
            case "<=":
                return LESS_THAN_OR_EQUALS;
            case "<":
                return LESS_THAN;
            case "!=":
                return NOT_EQUAL_TO;
            default:
                return EQUALS;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case EQUALS:
                return "==";
            case GREATER_THAN:
                return ">";
            case GREATER_THAN_OR_EQUALS:
                return ">=";
            case LESS_THAN:
                return "<";
            case LESS_THAN_OR_EQUALS:
                return "<=";
            case NOT_EQUAL_TO:
                return "!=";
            default:
                return "(invalid operator)";
        }
    }
}
