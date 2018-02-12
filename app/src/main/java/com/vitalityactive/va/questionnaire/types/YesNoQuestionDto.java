package com.vitalityactive.va.questionnaire.types;

import com.vitalityactive.va.questionnaire.Answer;

public class YesNoQuestionDto extends Question {
    private final String yesValue;
    private final String noValue;
    private boolean value;
    private boolean answered;
    private String yesDisplayString;
    private String noDisplayString;

    public YesNoQuestionDto(long id, long questionTypeKey, long sectionTypeKey, String title, String detail, String footer, float sortOrder, String yesValue, String noValue, String yesDisplayString, String noDisplayString) {
        super(id, questionTypeKey, sectionTypeKey, title, detail, footer, sortOrder);
        this.yesValue = yesValue;
        this.noValue = noValue;
        this.yesDisplayString = yesDisplayString;
        this.noDisplayString = noDisplayString;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.YES_NO;
    }

    @Override
    public Answer getAnswer() {
        return new Answer(value ? yesValue : noValue);
    }

    @Override
    void loadAnswer(Answer answer) {
        boolean yes = answer.getValue().equals(yesValue);
        boolean no = answer.getValue().equals(noValue);
        if (yes || no) {
            setValue(yes);
        } else {
            answered = false;
        }
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        answered = true;
        this.value = value;
    }

    @Override
    public boolean isAnswered() {
        return answered;
    }

    public String getYesDisplayString() {
        return yesDisplayString;
    }

    public String getNoDisplayString() {
        return noDisplayString;
    }
}
