package com.vitalityactive.va.questionnaire.types;

public enum QuestionType {
    BASIC_INPUT_VALUE(1),
    SINGLE_SELECT_OPTION(2),
    MULTI_SELECT_OPTION(3),
    INPUT_VALUE_WITH_UNIT(4),
    YES_NO(5),
    SINGLE_CHECKBOX(6),
    LABEL(7),
    DATE(8),
    FREE_TEXT(9),
    LABEL_WITH_ASSOCIATIONS(10),
    SECTION_DESCRIPTION(999),
    UNKNOWN(0);

    private final int value;

    QuestionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static QuestionType fromValue(int value) {
        switch (value) {
            case 1:
                return BASIC_INPUT_VALUE;
            case 2:
                return SINGLE_SELECT_OPTION;
            case 3:
                return MULTI_SELECT_OPTION;
            case 4:
                return INPUT_VALUE_WITH_UNIT;
            case 5:
                return YES_NO;
            case 6:
                return SINGLE_CHECKBOX;
            case 7:
                return LABEL;
            case 8:
                return DATE;
            case 9:
                return FREE_TEXT;
            case 10:
                return LABEL_WITH_ASSOCIATIONS;
            case 999:
                return SECTION_DESCRIPTION;
        }
        return UNKNOWN;
    }
}
