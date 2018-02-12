package com.vitalityactive.va.shared.questionnaire.repository.model;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetResponse;

import io.realm.RealmObject;

public class PopulationValueModel extends RealmObject implements Model {
    public String fromValue;
    private String toValue;
    public String unitOfMeasure;
    private String valueType;
    public String value;
    public long questionTypeKey;
    public String eventDate;
    public int eventKey;

    public PopulationValueModel() {}

    public static PopulationValueModel create(QuestionnaireSetResponse.PopulationValue responsePopulationValue, long typeKey) {
        PopulationValueModel populationValueModel = new PopulationValueModel();
        populationValueModel.questionTypeKey = typeKey;

        populationValueModel.fromValue = responsePopulationValue.fromValue;
        populationValueModel.toValue = responsePopulationValue.toValue;
        populationValueModel.unitOfMeasure = responsePopulationValue.unitOfMeasure;
        populationValueModel.valueType = responsePopulationValue.valueType;
        populationValueModel.value = responsePopulationValue.value;
        populationValueModel.eventDate = responsePopulationValue.eventDate;
        populationValueModel.eventKey = responsePopulationValue.eventKey;

        return populationValueModel;
    }
}
