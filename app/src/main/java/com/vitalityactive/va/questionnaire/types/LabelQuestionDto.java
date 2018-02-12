package com.vitalityactive.va.questionnaire.types;

import com.vitalityactive.va.questionnaire.Answer;

public class LabelQuestionDto extends OptionQuestionDto {
    public LabelQuestionDto(long id,
                            long questionTypeKey,
                            long sectionTypeKey,
                            String title,
                            String detail,
                            String footer,
                            float sortOrder) {
        super(id, questionTypeKey, sectionTypeKey, title, detail, footer, sortOrder);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.LABEL;
    }

    @Override
    public Answer getAnswer() {
        return Answer.blank();
    }

    @Override
    public boolean getCanBeAnswered() {
        return true;
    }

    @Override
    public boolean isAnswered() {
        // always true
        return true;
    }

    @Override
    void loadAnswer(Answer answer) {
        // no-op
    }
}
