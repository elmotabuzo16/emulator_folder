package com.vitalityactive.va.shared.questionnaire.repository.model;

import android.util.Log;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetResponse;
import com.vitalityactive.va.utilities.InvalidModelLogger;

import io.realm.RealmList;
import io.realm.RealmObject;

public class QuestionnaireSetModel extends RealmObject implements Model{
    public Integer totalPotentialPoints;
    public RealmList<QuestionnaireModel> questionnaires = new RealmList<>();
    public Integer totalQuestionnaireCompleted;
    public Boolean questionnaireSetCompleted;
    public Integer totalEarnedPoints;
    public int typeKey;
    public Integer totalQuestionnaires;
    public String questionnaireSetTypeCode;
    public String questionnaireSetTypeName;
    public String footer;
    public String description;
    public String text;

    public QuestionnaireSetModel() {
    }

    public static QuestionnaireSetModel create(QuestionnaireSetResponse questionnaireSetResponse) {
        if (questionnaireSetResponse.totalPotentialPoints == null
                || questionnaireSetResponse.totalQuestionnaireCompleted == null
                || questionnaireSetResponse.questionnaireSetCompleted == null
                || questionnaireSetResponse.totalEarnedPoints == null
                || questionnaireSetResponse.questionnaireSetTypeKey == null
                || questionnaireSetResponse.totalQuestionnaires == null)
        {
            InvalidModelLogger.warn(questionnaireSetResponse, questionnaireSetResponse.questionnaireSetTypeKey, "required fields are null");
            return null;
        }

        QuestionnaireSetModel questionnaireSetModel = new QuestionnaireSetModel();

        questionnaireSetModel.totalPotentialPoints = questionnaireSetResponse.totalPotentialPoints;
        questionnaireSetModel.totalQuestionnaireCompleted = questionnaireSetResponse.totalQuestionnaireCompleted;
        questionnaireSetModel.questionnaireSetCompleted = questionnaireSetResponse.questionnaireSetCompleted;
        questionnaireSetModel.totalEarnedPoints = questionnaireSetResponse.totalEarnedPoints;
        questionnaireSetModel.typeKey = questionnaireSetResponse.questionnaireSetTypeKey;
        questionnaireSetModel.totalQuestionnaires = questionnaireSetResponse.totalQuestionnaires;
        questionnaireSetModel.questionnaireSetTypeCode = questionnaireSetResponse.questionnaireSetTypeCode;
        questionnaireSetModel.questionnaireSetTypeName = questionnaireSetResponse.questionnaireSetTypeName;
        questionnaireSetModel.footer = questionnaireSetResponse.setTextNote;
        questionnaireSetModel.description = questionnaireSetResponse.setTextDescription;
        questionnaireSetModel.text = questionnaireSetResponse.setText;

        if (questionnaireSetResponse.questionnaire != null) {
            for (QuestionnaireSetResponse.Questionnaire questionnaireResponse : questionnaireSetResponse.questionnaire) {
                final QuestionnaireModel questionnaireModel = QuestionnaireModel.create(questionnaireResponse, questionnaireSetModel.typeKey);

                if (questionnaireModel != null) {
                    questionnaireSetModel.questionnaires.add(questionnaireModel);
                }
            }
        }

        return questionnaireSetModel;
    }
}
