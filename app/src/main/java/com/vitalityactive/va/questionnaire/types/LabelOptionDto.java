package com.vitalityactive.va.questionnaire.types;

import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.utilities.TextUtilities;

import java.util.ArrayList;
import java.util.List;

public abstract class LabelOptionDto extends Question {
    private final List<Item> items;
    private int activeChildTypeKeyForValidation;

    public LabelOptionDto(long id, long questionTypeKey, long sectionTypeKey, String title, String detail, String footer, float sortOrder) {
        this(id, questionTypeKey, sectionTypeKey, title, detail, footer, new ArrayList<Item>(), sortOrder);
    }

    public LabelOptionDto(long id,
                          long questionTypeKey,
                          long sectionTypeKey,
                          String title,
                          String detail,
                          String footer,
                          List<Item> items,
                          float sortOrder) {
        super(id, questionTypeKey, sectionTypeKey, title, detail, footer, sortOrder);
        this.items = items;
    }

    public void addItem(String hintLabel, String textValue, int valueInputType, int typeKey) {
        items.add(new Item(hintLabel, textValue, valueInputType, typeKey));
    }

    @Override
    public Answer getAnswer() {
        Answer answer = new Answer();
        for (Item item : getItems()) {
            if (TextUtilities.isNotNullAndHasAnyInput(item.textValue)) {
                answer.addAssociatedAnswer(item.textValue);
                answer.addAssociatedIdentifier(item.typeKey);
                answer.addAssociatiedTitle(item.hintLabel);
            } else {
                answer.addAssociatedAnswer("");
                answer.addAssociatedIdentifier(item.typeKey);
                answer.addAssociatiedTitle(item.hintLabel);
            }
        }
        return answer;
    }

    public void clearItems() {
        items.clear();
    }

    public List<Item> getItems() {
        return items;
    }

    public int getActiveChildTypeKeyForValidation() {
        return activeChildTypeKeyForValidation;
    }

    public void setActiveChildTypeKeyForValidation(int activeChildTypeKeyForValidation) {
        this.activeChildTypeKeyForValidation = activeChildTypeKeyForValidation;
    }

    public static class Item {
        public String hintLabel;
        public String textValue;
        public int valueInputType;
        public int typeKey;
        public boolean validationInProgress;

        public Item(String hintLabel, String textValue, int valueInputType, int typeKey) {
            this.hintLabel = hintLabel;
            this.textValue = textValue;
            this.valueInputType = valueInputType;
            this.typeKey = typeKey;
        }

        public boolean setValue(String value) {
            if (this.textValue != null) {
                if (this.textValue.equals(value)) {
                    return false;
                }
            }
            this.textValue = value;
            return true;
        }
    }
}
