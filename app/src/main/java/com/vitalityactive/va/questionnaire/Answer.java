package com.vitalityactive.va.questionnaire;

import android.support.annotation.NonNull;

import com.vitalityactive.va.utilities.date.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class Answer {
    private String unitOfMeasureKey;
    private List<String> values = new ArrayList<>();
    private long prePopulationValueDate;
    private boolean prePopulatedValue;
    private int prePopulationValueSource;
    private String activeAnswerValue;
    private List<Integer> associatedValuesIdentifier = new ArrayList<>();
    private List<String> associatedValuesTitle = new ArrayList<>();

    public Answer() {
    }

    public Answer(String value) {
        this.values.add(value);
    }

    public Answer(boolean yesNo) {
        this.values.add(stringFromBoolean(yesNo));
    }

    @NonNull
    private String stringFromBoolean(boolean yesNo) {
        String s = String.valueOf(yesNo);
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public Answer(long date) {
        this.values.add(String.valueOf(date));
    }

    public Answer(String value, String unitOfMeasureKey) {
        this(value);
        this.unitOfMeasureKey = unitOfMeasureKey;
    }

    public Answer(LocalDate date) {
        this.values.add(date.toString());
    }

    public static Answer blank() {
        return new Answer();
    }

    public String getValue() {
        if (values.size() > 0)
            return values.get(0);

        return null;
    }

    public float getFloatValue() {
        return Float.parseFloat(getValue());
    }

    public int getIntValue() {
        return Integer.parseInt(getValue());
    }

    public boolean getBooleanValue() {
        return Boolean.parseBoolean(getValue());
    }

    public LocalDate getDateValue() {
        return getValue() == null ? null : new LocalDate(getValue());
    }

    public String getUnitOfMeasureKey() {
        return unitOfMeasureKey;
    }

    public void addCheckedItem(String value) {
        values.add(value);
    }

    public void addAssociatedAnswer(String value) {
        values.add(value);
    }

    public void addAssociatedIdentifier(int id) {
        associatedValuesIdentifier.add(id);
    }

    public void addAssociatiedTitle(String title) {
        associatedValuesTitle.add(title);
    }

    public List<Integer> getAssociatedValuesIdentifier() {
        return associatedValuesIdentifier;
    }

    public List<String> getAssociatedValuesTitle() {
        return associatedValuesTitle;
    }

    public void setAssociatedValuesIdentifier(List<Integer> associatedValuesIdentifier) {
        this.associatedValuesIdentifier = associatedValuesIdentifier;
    }

    public void setAssociatedValuesTitle(List<String> associatedValuesTitle) {
        this.associatedValuesTitle = associatedValuesTitle;
    }

    public List<String> getValues() {
        return values;
    }

    public boolean hasAnswer() {
        return values.size() > 0;
    }

    public long getPrePopulationValueDate() {
        return prePopulationValueDate;
    }

    public void setPrePopulationValueDate(long prePopulationValueDate) {
        this.prePopulationValueDate = prePopulationValueDate;
    }

    public boolean isPrePopulatedValue() {
        return prePopulatedValue;
    }

    public void setPrePopulatedValue(boolean prePopulatedValue) {
        this.prePopulatedValue = prePopulatedValue;
    }

    public int getPrePopulationValueSource() {
        return prePopulationValueSource;
    }

    public void setPrePopulationValueSource(int prePopulationValueSource) {
        this.prePopulationValueSource = prePopulationValueSource;
    }

    public String getActiveAnswerValue() {
        return activeAnswerValue;
    }

    public void setActiveAnswerValue(String activeAnswerValue) {
        this.activeAnswerValue = activeAnswerValue;
    }
}
