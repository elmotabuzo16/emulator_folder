package com.vitalityactive.va.questionnaire.types;

import com.vitalityactive.va.questionnaire.Answer;

public class SingleSelectOptionQuestionDto extends OptionQuestionDto {
    public SingleSelectOptionQuestionDto(long id, long questionTypeKey, long sectionTypeKey, String title, String detail, String footer, float sortOrder) {
        super(id, questionTypeKey, sectionTypeKey, title, detail, footer, sortOrder);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.SINGLE_SELECT_OPTION;
    }

    @Override
    void loadAnswer(Answer answer) {
        super.clearAllCheckedItems();
        super.checkItem(answer.getValue());
    }

    @Override
    public boolean isAnswered() {
        return getCheckedItemCount() == 1;
    }
}
