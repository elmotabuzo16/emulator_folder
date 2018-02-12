package com.vitalityactive.va.shared.questionnaire.repository.model;

import android.support.annotation.Nullable;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetResponse;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ValidValueModel extends RealmObject implements Model {
    public UnitOfMeasureModel unitOfMeasureModel;
    public String name;
    public String value;
    public String description;
    public String note;
    public RealmList<ValidValueTypesModel> validValueTypes;
    public long questionTypeKey;
    public long typeOfQuestion;

    public ValidValueModel() {
    }

    @Nullable
    public static ValidValueModel create(QuestionnaireSetResponse.ValidValues responseValidValue, long typeKey, Long typeOfQuestion) {
        if (responseValidValue.name == null
                || responseValidValue.value == null) {
            return null;
        }

        ValidValueModel validValueModel = new ValidValueModel();
        validValueModel.questionTypeKey = typeKey;
        validValueModel.typeOfQuestion = typeOfQuestion;

        if (responseValidValue.unitOfMeasure != null) {
            validValueModel.unitOfMeasureModel = UnitOfMeasureModel.create(responseValidValue.unitOfMeasure, validValueModel.questionTypeKey);
        }
        validValueModel.name = responseValidValue.name;
        validValueModel.value = responseValidValue.value;
        validValueModel.description = responseValidValue.description;
        validValueModel.note = responseValidValue.note;

        if (responseValidValue.validValueTypes != null) {
            validValueModel.validValueTypes = new RealmList<>();
            for (QuestionnaireSetResponse.ValidValueType responseValidValueType : responseValidValue.validValueTypes) {
                ValidValueTypesModel validValueTypesModel = ValidValueTypesModel.create(responseValidValueType);
                if (validValueTypesModel != null) {
                    validValueModel.validValueTypes.add(validValueTypesModel);
                }
            }
        }

        return validValueModel;
    }
}
