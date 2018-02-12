package com.vitalityactive.va.questionnaire.types;

public enum InternalQuestionType {
    SECTION_DESCRIPTION(999);

    private final int id;

    InternalQuestionType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
