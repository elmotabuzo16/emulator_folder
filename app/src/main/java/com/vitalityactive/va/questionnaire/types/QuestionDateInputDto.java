package com.vitalityactive.va.questionnaire.types;

import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.utilities.date.LocalDate;

public class QuestionDateInputDto extends Question {
    private LocalDate date;

    public QuestionDateInputDto(int id,
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
        return QuestionType.DATE;
    }

    @Override
    public Answer getAnswer() {
        return new Answer(date);
    }

    @Override
    public boolean isAnswered() {
        return date != null;
    }

    @Override
    void loadAnswer(Answer answer) {
        date = answer.getDateValue();
    }

    public LocalDate getValue() {
        return date;
    }

    public void setValue(LocalDate date) {
        this.date = date;
    }
}
