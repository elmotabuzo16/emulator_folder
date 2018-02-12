package com.vitalityactive.va.shared.questionnaire;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireDTO;

import java.util.List;

public interface AssessmentCompletedPresenter extends Presenter<AssessmentCompletedPresenter.UserInterface> {
    void setQuestionnaireCompletedTypeKey(String questionnaireTypeKey);

    interface UserInterface {

        void setTitle(String title);

        void setSubtitle(String subtitle);

        void setText(String text);

        void showCompletedState();

        void showInCompleteState(String questionnaireTitle, int questionnairesRemaining, List<QuestionnaireDTO> incompleteQuestionnaires);
    }
}
