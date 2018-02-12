package com.vitalityactive.va.questionnaire.types;

import com.vitalityactive.va.questionnaire.Answer;

import java.util.ArrayList;
import java.util.List;

public abstract class OptionQuestionDto extends Question {
    private final List<Item> items;

    public OptionQuestionDto(long id, long questionTypeKey, long sectionTypeKey, String title, String detail, String footer, float sortOrder) {
        this(id, questionTypeKey, sectionTypeKey, title, detail, footer, new ArrayList<Item>(), sortOrder);
    }

    public OptionQuestionDto(long id,
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

    Item addItem(Item item) {
        items.add(item);
        return item;
    }

    Item addItem(String text, String detail) {
        return addItem(text, text, detail, null);
    }

    public Item addItem(String value, String text, String detail, String note) {
        return addItem(new Item(value, text, detail, note));
    }

    @Override
    public Answer getAnswer() {
        Answer answer = new Answer();
        for (Item item : getItems()) {
            if (item.checked) {
                answer.addCheckedItem(item.value);
            }
        }
        return answer;
    }

    public List<Item> getItems() {
        return items;
    }

    public void clearAllCheckedItems() {
        for (Item item : items) {
            item.checked = false;
        }
    }

    public void checkItem(String answer) {
        for (Item item : items) {
            if (item.value.equals(answer)) {
                item.checked = true;
            }
        }
    }

    public int getCheckedItemCount() {
        int count = 0;
        for (Item item : getItems()) {
            if (item.checked) {
                count++;
            }
        }
        return count;
    }

    public void onCheckedItemsChanged() {
    }

    public static class Item {
        public final String value;
        public final String optionText;
        public final String detailText;
        public boolean checked;
        public String note;

        public Item(String value, String optionText, String detailText, String note) {
            this.value = value;
            this.optionText = optionText;
            this.detailText = detailText;
            this.note = note;
        }

        public boolean hasNoteWhenSelected() {
            return note != null && !note.isEmpty();
        }
    }
}
