package com.vitalityactive.va.shared.questionnaire.repository.model;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetResponse;

import io.realm.RealmObject;

public class QuestionAssociationModel extends RealmObject implements Model {
    public int childQuestionKey;
    public int sortIndex;

    public QuestionAssociationModel() {}

    public static QuestionAssociationModel create(QuestionnaireSetResponse.QuestionAssociations responseQuestionAssociations) {
        QuestionAssociationModel questionAssociationModel = new QuestionAssociationModel();
        questionAssociationModel.childQuestionKey = responseQuestionAssociations.childQuestionKey;
        questionAssociationModel.sortIndex = responseQuestionAssociations.sortIndex;

        return questionAssociationModel;
    }
}
