package com.vitalityactive.va.questionnaire.types;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;

import com.vitalityactive.va.UnitsOfMeasure;
import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.vhc.captureresults.ValueLimit;
import com.vitalityactive.va.vhc.dto.UnitAbbreviationDescription;

import java.util.ArrayList;
import java.util.List;

public class QuestionBasicInputValueDto extends Question {
    private final String hintText;
    private final int valueInputType;
    private final List<UnitAbbreviationDescription> units;
    private String value;
    private UnitAbbreviationDescription selectedUnit;

    public QuestionBasicInputValueDto(long id,
                                      long questionTypeKey,
                                      long sectionTypeKey,
                                      String title,
                                      String detail,
                                      String footer,
                                      String hintText,
                                      int valueInputType,
                                      float sortOrder) {
        super(id, questionTypeKey, sectionTypeKey, title, detail, footer, sortOrder);
        this.hintText = hintText;
        this.valueInputType = valueInputType;
        this.units = new ArrayList<>();
    }

    public static QuestionBasicInputValueDto numericInput(long id,
                                                          int questionTypeKey,
                                                          long sectionTypeKey,
                                                          String title,
                                                          String detail,
                                                          String footer,
                                                          String hintText,
                                                          float sortOrder) {

        int inputType;

        if (questionTypeKey == com.vitalityactive.va.constants.QuestionType._DECIMALRANGE) {
            inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
                    | InputType.TYPE_NUMBER_FLAG_SIGNED
                    | InputType.TYPE_CLASS_NUMBER;
        } else {
            inputType = InputType.TYPE_CLASS_NUMBER;
        }

        return new QuestionBasicInputValueDto(id, questionTypeKey,
                sectionTypeKey,
                title, detail, footer, hintText,
                inputType, sortOrder);
    }

    public static QuestionBasicInputValueDto numericInput(int id,
                                                          long sectionTypeKey, String title,
                                                          String detail,
                                                          String footer,
                                                          String hintText,
                                                          int sortOrder) {
        return numericInput(id, -1, sectionTypeKey, title, detail, footer, hintText, sortOrder);
    }

    public String getHintText() {
        return hintText;
    }

    public String getSingleUnit() {
        String unit = "";

        if (units.size() > 0) {
            unit = units.get(0).getAbbreviation();
        }

        return unit;
    }

    public int getValueInputType() {
        return valueInputType;
    }

    public List<UnitAbbreviationDescription> getUnits() {
        return units;
    }

    @Override
    public QuestionType getQuestionType() {
        if (units.size() > 1)
            return QuestionType.INPUT_VALUE_WITH_UNIT;
        return QuestionType.BASIC_INPUT_VALUE;
    }

    @Override
    public Answer getAnswer() {
        if (!isAnswered())
            return Answer.blank();

        if (getQuestionType() == QuestionType.INPUT_VALUE_WITH_UNIT) {
            return assessmultiUnitAnswer(value, selectedUnit.getUnitOfMeasure());
        } else if (units.size() == 1) {
            return assessmultiUnitAnswer(value, units.get(0).getUnitOfMeasure());
        } else {
            return new Answer(value);
        }
    }


    private Answer assessmultiUnitAnswer(String value, UnitsOfMeasure unitOfMeasure) {
        //TODO: Tailor multi-unit answers here, workaround is on QuestionnaireSubmitRequestBody class
        return new Answer(value, unitOfMeasure.getTypeKey());
    }

    @Override
    public boolean isAnswered() {
        return !(getQuestionType() == QuestionType.INPUT_VALUE_WITH_UNIT && selectedUnit == null)
                && value != null && !value.isEmpty();
    }

    @Override
    void loadAnswer(Answer answer) {
        if (answer == null)
            return;

        if (getQuestionType() == QuestionType.INPUT_VALUE_WITH_UNIT) {
            setSelectedUnit(getUnitAbbreviationDescriptionFromUnitKey(answer.getUnitOfMeasureKey()));
        }

        if (isIntegerInputType()) {
            value = getIntegerValue(answer);
        } else {
            value = answer.getValue();
        }
    }

    private boolean isIntegerInputType() {
        return (valueInputType & InputType.TYPE_NUMBER_FLAG_DECIMAL) != InputType.TYPE_NUMBER_FLAG_DECIMAL;
    }

    @NonNull
    private String getIntegerValue(Answer answer) {
        return String.valueOf(Float.valueOf(answer.getValue()).intValue());
    }

    @Nullable
    private UnitAbbreviationDescription getUnitAbbreviationDescriptionFromUnitKey(String typeKey) {
        UnitAbbreviationDescription selected = null;
        for (UnitAbbreviationDescription unitAbbreviationDescription : getUnits()) {
            if (!TextUtils.isEmpty(typeKey) && typeKey.equals(unitAbbreviationDescription.getUnitOfMeasure().getTypeKey()))
                selected = unitAbbreviationDescription;
        }
        return selected;
    }

    void addUnit(UnitsOfMeasure unit, String symbol, String name) {

        if (unit.getTypeKey().equals(UnitsOfMeasure.FOOTINCH.getTypeKey())) {
            UnitAbbreviationDescription.SubUnit subUnit1 = new UnitAbbreviationDescription.SubUnit("Feet", new ValueLimit(0f, null, ""));
            UnitAbbreviationDescription.SubUnit subUnit2 = new UnitAbbreviationDescription.SubUnit("Inches", new ValueLimit(0f, 11f, "Range 0 - 11"));
            units.add(new UnitAbbreviationDescription(unit, symbol, name, new ValueLimit(0f, null, ""), subUnit1, subUnit2));
            return;
        }

        if (unit.getTypeKey().equals(UnitsOfMeasure.STONEPOUND.getTypeKey())) {
            UnitAbbreviationDescription.SubUnit subUnit1 = new UnitAbbreviationDescription.SubUnit("Stone", new ValueLimit(0f, null, ""));
            UnitAbbreviationDescription.SubUnit subUnit2 = new UnitAbbreviationDescription.SubUnit("Pound", new ValueLimit(0f, 13f, "Range 0 - 13"));
            units.add(new UnitAbbreviationDescription(unit, symbol, name, new ValueLimit(0f, null, ""), subUnit1, subUnit2));
            return;
        }

        units.add(new UnitAbbreviationDescription(unit, symbol, name, new ValueLimit(0f, null, "")));
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

    public boolean setSelectedUnit(UnitAbbreviationDescription unit) {
        if (unit == null || unit.equals(selectedUnit))
            return false;

        this.selectedUnit = unit;
        return true;
    }

    public UnitAbbreviationDescription getSelectedUnit() {
        return selectedUnit;
    }
}
