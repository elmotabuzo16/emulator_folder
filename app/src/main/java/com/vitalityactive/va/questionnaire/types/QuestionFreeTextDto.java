package com.vitalityactive.va.questionnaire.types;

import com.vitalityactive.va.questionnaire.Answer;

public class QuestionFreeTextDto extends Question {
    private final String hintText;
    private String value;

    public QuestionFreeTextDto(long id,
                               long questionTypekey,
                               String title,
                               String detail,
                               String footer,
                               String hintText,
                               float sortOrder,
                               int maxLength) {
        super(id, questionTypekey, 0, title, detail, footer, sortOrder, maxLength);
        this.hintText = hintText;
    }

    public static QuestionFreeTextDto textInput(long id,
                                                long questionTypeKey,
                                                String title,
                                                String detail,
                                                String footer,
                                                String hintText,
                                                float sortOrder,
                                                int maxLength) {

        return new QuestionFreeTextDto(id, questionTypeKey, title, detail, footer,
                hintText, sortOrder, maxLength);
    }

    public String getHintText() {
        return hintText;
    }


    @Override
    public QuestionType getQuestionType() {
        return QuestionType.FREE_TEXT;
    }

    @Override
    public Answer getAnswer() {
        if (!isAnswered())
            return Answer.blank();

        return new Answer(value);
    }

    @Override
    public boolean isAnswered() {
        return value != null && !value.isEmpty();
    }

    @Override
    void loadAnswer(Answer answer) {
        value = answer.getValue();
    }

    public String getValue() {
        return value;
    }

    public boolean setValue(String value) {
        if (this.value != null) {
            if (this.value.equals(value)) {
                return false;
            }
        }
        this.value = value;
        return true;
    }
}
