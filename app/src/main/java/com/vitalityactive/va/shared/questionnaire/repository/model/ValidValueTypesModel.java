package com.vitalityactive.va.shared.questionnaire.repository.model;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetResponse;

import io.realm.RealmObject;

public class ValidValueTypesModel extends RealmObject implements Model {
    public long typeKey;
    public String typeName;
    public String typeCode;

    public ValidValueTypesModel() {}

    public static ValidValueTypesModel create(QuestionnaireSetResponse.ValidValueType responseValidValueType) {
        if (responseValidValueType.typeKey == null) {
            return null;
        }

        ValidValueTypesModel validValueTypesModel = new ValidValueTypesModel();

        validValueTypesModel.typeKey = responseValidValueType.typeKey;
        validValueTypesModel.typeName = responseValidValueType.typeName;
        validValueTypesModel.typeCode = responseValidValueType.typeCode;

        return validValueTypesModel;
    }
}
