package com.vitalityactive.va.shared.questionnaire.repository;

import com.vitalityactive.va.constants.QuestionType;
import com.vitalityactive.va.constants.QuestionValidValueType;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.questionnaire.validations.ValidationRule;
import com.vitalityactive.va.questionnaire.validations.ValidationRuleFactory;
import com.vitalityactive.va.shared.questionnaire.repository.model.ValidValueModel;
import com.vitalityactive.va.utilities.TextUtilities;

import java.util.ArrayList;
import java.util.List;

public class ValidationRuleMapper extends ValidationRuleFactory implements
        DataStore.ModelMapper<ValidValueModel, ValidationRule>,
        DataStore.ModelListMapper<ValidValueModel, ValidationRule> {
    @Override
    public List<ValidationRule> mapModels(List<ValidValueModel> models) {
        ArrayList<ValidationRule> list = new ArrayList<>();
        if (models != null && models.size() > 0) {
            for (ValidValueModel model : models) {
                list.add(mapModel(model));
            }
        }
        return list;
    }

    @Override
    public ValidationRule mapModel(ValidValueModel model) {
        switch ((int) model.typeOfQuestion) {
            case QuestionType._DECIMALRANGE:
            case QuestionType._NUMBERRANGE:
            case QuestionType._PERCENTAGE:
            case QuestionType._CHILDNUMBERRANGE:
                return mapNumberRangeValidationRule(model);
            case QuestionType._FORMATTEXT:
            case QuestionType._MULTIDECIMALRANGE:
            case QuestionType._MULTINUMBERRANGE:
                return nothing("validation not implemented");

            // must just be answered
            case QuestionType._OPTIONLIST:
            case QuestionType._MULTISELECT:
            case QuestionType._SINGLESELECT:
            case QuestionType._CHECKLIST:
            case QuestionType._DATE:
            case QuestionType._FREETEXT:
            case QuestionType._LABEL:
                return anything();
            default:
                return nothing("Unknown question type is not implemented");
        }
    }

    private ValidationRule mapNumberRangeValidationRule(ValidValueModel model) {
        if (model.validValueTypes == null || model.validValueTypes.size() == 0) {
            return nothing("mapNumberRangeValidationRule failed");
        }
        long typeKey = model.validValueTypes.get(0).typeKey;
        float number;
        try {
            number = Float.parseFloat(model.value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            number = 0;
        }
        String unitOfMeasureKey = model.unitOfMeasureModel == null ? null : model.unitOfMeasureModel.value;

        if (unitOfMeasureKeyExists(unitOfMeasureKey)) {
            if (typeKey == QuestionValidValueType._LOWERLIMIT || typeKey == QuestionValidValueType._VALUEMINIMUM) {
                return numberGreaterThanOrEqual(number, unitOfMeasureKey);
            } else if (typeKey == QuestionValidValueType._UPPERLIMIT || typeKey == QuestionValidValueType._VALUEMAXIMUM) {
                return numberLessThanOrEqual(number, unitOfMeasureKey);
            }
        }

        if (typeKey == QuestionValidValueType._LOWERLIMIT || typeKey == QuestionValidValueType._VALUEMINIMUM) {
            return numberGreaterThanOrEqual(number);
        } else if (typeKey == QuestionValidValueType._UPPERLIMIT || typeKey == QuestionValidValueType._VALUEMAXIMUM) {
            return numberLessThanOrEqual(number);
        }

        return nothing("unknown number validation rule details");
    }

    private boolean unitOfMeasureKeyExists(String unitOfMeasureKey) {
        return !TextUtilities.isNullOrWhitespace(unitOfMeasureKey);
    }
}
