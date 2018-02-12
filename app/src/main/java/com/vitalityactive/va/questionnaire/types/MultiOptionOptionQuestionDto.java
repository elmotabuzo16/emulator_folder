package com.vitalityactive.va.questionnaire.types;

import com.vitalityactive.va.questionnaire.Answer;

import java.util.List;

public class MultiOptionOptionQuestionDto extends OptionQuestionDto {
    private boolean answered;

    public MultiOptionOptionQuestionDto(long id,
                                        long questionTypeKey,
                                        long sectionTypeKey,
                                        String title,
                                        String detail,
                                        String footer,
                                        float sortOrder) {
        super(id, questionTypeKey, sectionTypeKey, title, detail, footer, sortOrder);
    }

    @Override
    public void onCheckedItemsChanged() {
        super.onCheckedItemsChanged();
        updateAnswered();
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTI_SELECT_OPTION;
    }

    @Override
    public boolean isAnswered() {
        return answered;
    }

    @Override
    void loadAnswer(Answer answer) {
        clearAllCheckedItems();
        checkAllItems(answer.getValues());
        updateAnswered();
    }

    private void updateAnswered() {
        answered = getCheckedItemCount() > 0;
    }

    @Override
    public void hasAnAnswerSavedButNoValues() {
        super.hasAnAnswerSavedButNoValues();
        updateAnswered();
    }

    private void checkAllItems(List<String> items) {
        for (String s : items) {
            checkItem(s);
        }
    }
}
