package com.vitalityactive.va.questionnaire.types;

import android.text.InputType;

import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.utilities.TextUtilities;

import java.util.List;

public class LabelWithAssociationsQuestionDto extends LabelOptionDto {

    public LabelWithAssociationsQuestionDto(long id,
                                            long questionTypeKey,
                                            long sectionTypeKey,
                                            String title,
                                            String detail,
                                            String footer,
                                            int valueInputType,
                                            float sortOrder) {
        super(id, questionTypeKey, sectionTypeKey, title, detail, footer, sortOrder);
    }

    public static LabelWithAssociationsQuestionDto numericInput(long id,
                                                                int questionTypeKey,
                                                                long sectionTypeKey,
                                                                String title,
                                                                String detail,
                                                                String footer,
                                                                String hintText,
                                                                float sortOrder) {

        int inputType = InputType.TYPE_CLASS_NUMBER;


        return new LabelWithAssociationsQuestionDto(id, questionTypeKey,
                sectionTypeKey,
                title, detail, footer,
                inputType, sortOrder);
    }

    public static LabelWithAssociationsQuestionDto numericInput(int id,
                                                                long sectionTypeKey, String title,
                                                                String detail,
                                                                String footer,
                                                                String hintText,
                                                                int sortOrder) {
        return numericInput(id, -1, sectionTypeKey, title, detail, footer, hintText, sortOrder);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.LABEL_WITH_ASSOCIATIONS;
    }

    @Override
    public boolean isAnswered() {
        for (Item item : getItems()) {
            if (TextUtilities.isNotNullAndHasAnyInput(item.textValue)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAnsweredByTypeKey(int typeKey) {
        for (Item item : getItems()) {
            if (typeKey == item.typeKey && TextUtilities.isNotNullAndHasAnyInput(item.textValue)) {
                return true;
            }
        }
        return false;
    }

    public Answer getAnswerByTypeKey(int typeKey) {
        Answer answer = new Answer();
        for (Item item : getItems()) {
            if (item.typeKey == typeKey && TextUtilities.isNotNullAndHasAnyInput(item.textValue)) {
                answer.addAssociatedAnswer(item.textValue);
                answer.addAssociatedIdentifier(item.typeKey);
                answer.addAssociatiedTitle(item.hintLabel);
            }
        }
        return answer;
    }

    @Override
    public void loadAnswer(Answer answer) {
        clearItems();

        List<String> values = answer.getValues();
        List<Integer> ids = answer.getAssociatedValuesIdentifier();
        List<String> titles = answer.getAssociatedValuesTitle();
        int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED;

        if (values != null && values.size() > 0 &&
                ids.size() > 0 &&
                titles.size() > 0) {
            for (int i = 0; i < values.size(); i++) {
                addItem(titles.get(i), values.get(i), inputType, ids.get(i));
            }
        }
    }
}
