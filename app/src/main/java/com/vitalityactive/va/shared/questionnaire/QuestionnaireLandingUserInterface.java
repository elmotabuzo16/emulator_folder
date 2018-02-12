package com.vitalityactive.va.shared.questionnaire;

import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireDTO;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireSetInformation;

import java.util.List;

public interface QuestionnaireLandingUserInterface {
    void showLoadingIndicator();

    void hideLoadingIndicator();

    void setActionBarTitleAndDisplayHomeAsUp(String onboardingTitle);

    void showConnectionError();

    void showGenericError();

    void showQuestionnaireSetAndQuestionnaires(QuestionnaireSetInformation questionnaireSets, List<QuestionnaireDTO> questionnaires);


    void showVitalityAgeView();
}
