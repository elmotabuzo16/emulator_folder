package com.vitalityactive.va.shared.questionnaire;

import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireDTO;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireSetInformation;

import java.util.List;

public class BaseAssessmentCompletedPresenter implements AssessmentCompletedPresenter {
    private final QuestionnaireSetRepository questionnaireSetRepository;
    private UserInterface userInterface;
    private long completedQuestionnaireTypeKey;

    public BaseAssessmentCompletedPresenter(QuestionnaireSetRepository questionnaireSetRepository) {
        this.questionnaireSetRepository = questionnaireSetRepository;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        List<QuestionnaireDTO> incompleteQuestionnaires = questionnaireSetRepository.getUnansweredQuestionnairesTopLevelData();

        if (incompleteQuestionnaires.size() == 0) {
            handleAllCompletedQuestionnaires();
        } else {
            handleIncompleteQuestionnaires(incompleteQuestionnaires);
        }
    }

    private void handleIncompleteQuestionnaires(List<QuestionnaireDTO> incompleteQuestionnaires) {
        final String questionnaireTitle =
                questionnaireSetRepository.getQuestionnaireTitle(completedQuestionnaireTypeKey);

        QuestionnaireSetInformation questionnairesSetTopLevelData = questionnaireSetRepository.getQuestionnairesSetTopLevelData();

        userInterface.showInCompleteState(questionnaireTitle,
                questionnairesSetTopLevelData.getTotalPotentialPoints(),
                incompleteQuestionnaires);
    }

    private void handleAllCompletedQuestionnaires() {
        userInterface.showCompletedState();
    }

    @Override
    public void onUserInterfaceAppeared() {

    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {

    }

    @Override
    public void onUserInterfaceDestroyed() {

    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void setQuestionnaireCompletedTypeKey(String questionnaireTypeKey) {
        this.completedQuestionnaireTypeKey = Long.parseLong(questionnaireTypeKey);
        questionnaireSetRepository.setQuestionnaireCompletedFlagToTrue(completedQuestionnaireTypeKey);
    }
}
