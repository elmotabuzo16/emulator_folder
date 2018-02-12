package com.vitalityactive.va.shared.questionnaire.capture;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.validations.ValidationResult;

import java.util.List;

public interface QuestionnaireCapturePresenter extends Presenter<QuestionnaireCapturePresenter.UserInterface> {
    ValidationResult setQuestionAnswered(Question question);

    void onForwardButtonClicked();

    void handleQuestionnaireCompleted();

    void onBackButtonClicked();

    void setQuestionnaireTypeKey(long questionnaireTypeKey);

    ValidationResult validate(Question question);

    Long getSectionTypeKey();

    interface UserInterface {
        void showLoadingIndicator();

        void hideLoadingIndicator();

        void bindQuestions(List<Question> questions);

        void rebindQuestions(List<Question> questions);

        void navigateAfterQuestionnaireCompleted();

        void sectionProgress(int progress, long total);

        void navigateToNextSection();

        void navigateToLandingScreen();

        void setQuestionnaireTitle(String sectionTitle);

        void setQuestionnaireSectionTitle(String sectionTitle);

        void canProceedToNextSection(boolean yes);

        void clearFocus();

        void showConnectionSubmissionRequestErrorMessage();

        void showGenericSubmissionRequestErrorMessage();
    }
}
