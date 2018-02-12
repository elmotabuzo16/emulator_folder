package com.vitalityactive.va.shared.questionnaire.repository.model;

import android.support.annotation.Nullable;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetResponse;
import com.vitalityactive.va.utilities.InvalidModelLogger;

import io.realm.RealmList;
import io.realm.RealmObject;

public class QuestionnaireSectionModel extends RealmObject implements Model {
    public long typeKey;
    public String title;
    public String visibilityTagName;
    public RealmList<QuestionModel> questions = new RealmList<>();
    public Boolean isVisible;
    public int sortOrderIndex;
    public String typeCode;
    public String text;
    public String textDescription;
    public String textNote;
    private QuestionnaireModel questionnaire;

    public QuestionnaireSectionModel() {
    }

    @Nullable
    public static QuestionnaireSectionModel create(QuestionnaireSetResponse.QuestionnaireSections responseQuestionnaireSection,
                                                   QuestionnaireModel questionnaire) {
        if (responseQuestionnaireSection.typeKey == null
                || responseQuestionnaireSection.visibilityTagName == null
                || responseQuestionnaireSection.isVisible == null
                || responseQuestionnaireSection.sortOrderIndex == null)
        {
            InvalidModelLogger.warn(responseQuestionnaireSection, responseQuestionnaireSection.typeKey, "required fields are null");
            return null;
        }

        QuestionnaireSectionModel questionnaireSectionModel = new QuestionnaireSectionModel();
        questionnaireSectionModel.questionnaire = questionnaire;

        questionnaireSectionModel.typeKey = responseQuestionnaireSection.typeKey;
        questionnaireSectionModel.title = responseQuestionnaireSection.typeName;
        questionnaireSectionModel.visibilityTagName = responseQuestionnaireSection.visibilityTagName;
        questionnaireSectionModel.isVisible = responseQuestionnaireSection.isVisible;
        questionnaireSectionModel.sortOrderIndex = responseQuestionnaireSection.sortOrderIndex;
        questionnaireSectionModel.typeCode = responseQuestionnaireSection.typeCode;
        questionnaireSectionModel.text = responseQuestionnaireSection.text;
        questionnaireSectionModel.textNote = responseQuestionnaireSection.textNote;
        questionnaireSectionModel.textDescription = responseQuestionnaireSection.textDescription;

        if (responseQuestionnaireSection.questions != null)
            for (QuestionnaireSetResponse.Questions responseQuestion : responseQuestionnaireSection.questions) {
                QuestionModel questionModel = QuestionModel.create(responseQuestion, questionnaireSectionModel);

                if (questionModel != null) {
                    questionnaireSectionModel.questions.add(questionModel);
                }
            }

        return questionnaireSectionModel;
    }

    public long getTypeKey() {
        return typeKey;
    }

    public String getTitle() {
        return title;
    }
}
