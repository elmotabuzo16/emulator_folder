package com.vitalityactive.va.questionnaire.types;

import com.vitalityactive.va.questionnaire.Answer;

public class SingleCheckboxQuestionDto extends Question {
    private boolean value;

    SingleCheckboxQuestionDto(long id, long questionTypeKey, long sectionTypeKey, String title, String detail, String footer, float sortOrder) {
        super(id, questionTypeKey, sectionTypeKey, title, detail, footer, sortOrder);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.SINGLE_CHECKBOX;
    }

    @Override
    public Answer getAnswer() {
        return new Answer(value);
    }

    @Override
    void loadAnswer(Answer answer) {
        setValue(answer.getBooleanValue());
    }

    @Override
    public boolean shouldCreateDefaultAnswer() {
        return true;
    }

    @Override
    public boolean isAnswered() {
        return true;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
