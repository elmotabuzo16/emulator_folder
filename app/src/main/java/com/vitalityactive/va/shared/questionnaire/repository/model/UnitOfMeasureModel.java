package com.vitalityactive.va.shared.questionnaire.repository.model;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetResponse;
import com.vitalityactive.va.utilities.InvalidModelLogger;

import io.realm.RealmObject;

public class UnitOfMeasureModel extends RealmObject implements Model {
    public String value;
    private long questionTypeKey;

    public UnitOfMeasureModel() {}

    public static UnitOfMeasureModel create(QuestionnaireSetResponse.UnitOfMeasure responseUnitOfMeasure, long typeKey) {
        if (responseUnitOfMeasure.value == null) {
            InvalidModelLogger.warn(responseUnitOfMeasure, typeKey, "'value' is null");
            return null;
        }

        UnitOfMeasureModel unitOfMeasureModel = new UnitOfMeasureModel();
        unitOfMeasureModel.questionTypeKey = typeKey;

        unitOfMeasureModel.value = responseUnitOfMeasure.value;

        return unitOfMeasureModel;
    }
}
