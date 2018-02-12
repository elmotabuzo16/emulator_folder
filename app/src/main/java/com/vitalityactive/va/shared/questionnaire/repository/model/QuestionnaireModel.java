package com.vitalityactive.va.shared.questionnaire.repository.model;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetResponse;
import com.vitalityactive.va.utilities.InvalidModelLogger;

import io.realm.RealmList;
import io.realm.RealmObject;

public class QuestionnaireModel extends RealmObject implements Model {
    public int questionnaireSetTypeKey;
    public long typeKey;
    public String completedOn;
    public String typeName;
    public Boolean completionFlag;
    public float sortOrderIndex;
    public String typeCode;
    public RealmList<QuestionnaireSectionModel> questionnaireSection = new RealmList<>();
    public String textDescription;

    public static QuestionnaireModel create(QuestionnaireSetResponse.Questionnaire responseQuestionnaire,
                                            int questionnaireSetTypeKey) {
        if (responseQuestionnaire.typeKey == null
                || responseQuestionnaire.completionFlag == null
                || responseQuestionnaire.sortOrderIndex == null
                || responseQuestionnaire.typeCode == null)
        {
            InvalidModelLogger.warn(responseQuestionnaire, responseQuestionnaire.typeKey, "required fields are null");
            return null;
        }

        QuestionnaireModel questionnaireModel = new QuestionnaireModel();
        questionnaireModel.questionnaireSetTypeKey = questionnaireSetTypeKey;

        questionnaireModel.typeKey = responseQuestionnaire.typeKey;
        questionnaireModel.completedOn = responseQuestionnaire.completedOn;
        questionnaireModel.typeName = responseQuestionnaire.typeName;
        questionnaireModel.completionFlag = responseQuestionnaire.completionFlag;
        questionnaireModel.sortOrderIndex = responseQuestionnaire.sortOrderIndex;
        questionnaireModel.typeCode = responseQuestionnaire.typeCode;
        questionnaireModel.textDescription = responseQuestionnaire.textDescription;

        if (responseQuestionnaire.questionnaireSections != null) {
            questionnaireModel.questionnaireSection = new RealmList<>();

            for (QuestionnaireSetResponse.QuestionnaireSections responseQuestionnaireSection : responseQuestionnaire.questionnaireSections) {
                final QuestionnaireSectionModel section = QuestionnaireSectionModel.create(responseQuestionnaireSection, questionnaireModel);
                if (section != null) {
                    questionnaireModel.questionnaireSection.add(section);
                }
            }
        }

        return questionnaireModel;
    }
}
