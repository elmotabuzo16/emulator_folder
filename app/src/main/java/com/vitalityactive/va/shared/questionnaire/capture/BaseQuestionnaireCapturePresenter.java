package com.vitalityactive.va.shared.questionnaire.capture;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.constants.QuestionnaireSections;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.questionnaire.QuestionnaireStateManager;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.types.QuestionType;
import com.vitalityactive.va.questionnaire.types.SingleCheckboxQuestionDto;
import com.vitalityactive.va.questionnaire.validations.ValidationResult;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmissionRequestCompletedEvent;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmitter;

import java.util.List;

public abstract class BaseQuestionnaireCapturePresenter implements QuestionnaireCapturePresenter {

    private static int NO_APPLICABLE_DISEASE_IDENTIFIER = 143;

    protected final QuestionnaireStateManager questionnaireStateManager;
    protected final QuestionnaireSubmitter questionnaireSubmitter;
    protected final EventDispatcher eventDispatcher;
    protected final Scheduler scheduler;
    protected InsurerConfigurationRepository insurerConfigurationRepository;
    private UserInterface userInterface;
    private long questionnaireTypeKey;
    private EventListener<QuestionnaireSubmissionRequestCompletedEvent> submissionRequestCompletedEventListener = new EventListener<QuestionnaireSubmissionRequestCompletedEvent>() {
        @Override
        public void onEvent(QuestionnaireSubmissionRequestCompletedEvent event) {
            scheduler.schedule(() -> {
                if (didSubmissionRequestFail()) {
                    userInterface.hideLoadingIndicator();
                    showRequestErrorMessage();
                } else {
                    userInterface.navigateAfterQuestionnaireCompleted();
                }
            });
        }
    };

    public BaseQuestionnaireCapturePresenter(QuestionnaireStateManager questionnaireStateManager, InsurerConfigurationRepository insurerConfigurationRepository, QuestionnaireSubmitter questionnaireSubmitter, EventDispatcher eventDispatcher, Scheduler scheduler) {
        this.questionnaireStateManager = questionnaireStateManager;
        this.eventDispatcher = eventDispatcher;
        this.insurerConfigurationRepository = insurerConfigurationRepository;
        this.scheduler = scheduler;
        this.questionnaireSubmitter = questionnaireSubmitter;
    }

    private void showRequestErrorMessage() {
        if (questionnaireSubmitter.getSubmissionRequestResult() == RequestResult.CONNECTION_ERROR) {
            userInterface.showConnectionSubmissionRequestErrorMessage();
        } else if (questionnaireSubmitter.getSubmissionRequestResult() == RequestResult.GENERIC_ERROR) {
            userInterface.showGenericSubmissionRequestErrorMessage();
        }
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        questionnaireStateManager.setQuestionnaireTypeKey(questionnaireTypeKey);
        userInterface.setQuestionnaireTitle(questionnaireStateManager.getQuestionnaireTitle());
    }

    @Override
    public void onUserInterfaceAppeared() {
        eventDispatcher.addEventListener(QuestionnaireSubmissionRequestCompletedEvent.class, submissionRequestCompletedEventListener);

        loadSectionDetails();

        userInterface.setQuestionnaireSectionTitle(questionnaireStateManager.getCurrentSectionTitle());
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        eventDispatcher.removeEventListener(QuestionnaireSubmissionRequestCompletedEvent.class, submissionRequestCompletedEventListener);
    }

    @Override
    public void onUserInterfaceDestroyed() {
        this.userInterface = null;
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    private void loadSectionDetails() {
        updateQuestionnaireProgress();
        updateCanProceedToNextSection(true);

        final List<Question> allQuestionsInCurrentSection =
                questionnaireStateManager.getAllQuestionsInCurrentSection();

        userInterface.bindQuestions(allQuestionsInCurrentSection);
    }

    private void updateQuestionnaireProgress() {
        userInterface.sectionProgress(questionnaireStateManager.getCurrentSectionIndex() + 1,
                questionnaireStateManager.getTotalSections());
    }

    @Override
    public ValidationResult setQuestionAnswered(Question question) {
        preValidationUpdates(question);

        // TODO: build up the error message here and return it to the UI instead of returning a ValidationResult
        ValidationResult validationResult = questionnaireStateManager.answer(question);
        boolean valid = validationResult.isValid();
        userInterface.rebindQuestions(questionnaireStateManager.getAllQuestionsInCurrentSection());
        updateCanProceedToNextSection(valid);
        updateQuestionnaireProgress();
        return validationResult;
    }

    private void preValidationUpdates(Question question) {
        if (getSectionTypeKey() == QuestionnaireSections._MEDICALHISTORY) {
            updateMedicalQuestionsStatus(question);
        }
    }

    private void updateMedicalQuestionsStatus(Question question) {
        if (question.getIdentifier() == NO_APPLICABLE_DISEASE_IDENTIFIER) {
            clearDiseasesAnswers();
        } else {
            clearNoApplicableDisease();
        }
    }

    private void clearDiseasesAnswers() {
        for (Question question : questionnaireStateManager.getAllQuestionsInCurrentSection()) {
            if (question.getQuestionType() == QuestionType.SINGLE_CHECKBOX) {
                ((SingleCheckboxQuestionDto) question).setValue(false);
                questionnaireStateManager.answer(question);
            }
        }
    }

    private void clearNoApplicableDisease() {
        for (Question question : questionnaireStateManager.getAllQuestionsInCurrentSection()) {
            if (question.getIdentifier() == NO_APPLICABLE_DISEASE_IDENTIFIER && question.getQuestionType() == QuestionType.SINGLE_CHECKBOX) {
                ((SingleCheckboxQuestionDto) question).setValue(false);
                questionnaireStateManager.answer(question);
                break;
            }
        }
    }

    private void updateCanProceedToNextSection(boolean lastQuestionHasValidAnswer) {
        boolean canProceed = lastQuestionHasValidAnswer;
        canProceed = canProceed && !hasUnansweredQuestions();
        canProceed = canProceed && !hasInvalidAnsweredQuestions();
        userInterface.canProceedToNextSection(canProceed);
    }

    private boolean hasUnansweredQuestions() {
        return !questionnaireStateManager.getUnansweredQuestionsInCurrentSection().isEmpty();
    }

    private boolean hasInvalidAnsweredQuestions() {
        return !questionnaireStateManager.getInvalidAnsweredQuestionsInCurrentSection().isEmpty();
    }

    @Override
    public void onForwardButtonClicked() {
        userInterface.clearFocus();

        if (questionnaireStateManager.goToNextSection()) {
            userInterface.navigateToNextSection();
            userInterface.setQuestionnaireSectionTitle(questionnaireStateManager.getCurrentSectionTitle());
        } else {
            handleQuestionnaireCompleted();
        }
    }

    public void handleQuestionnaireCompleted() {
        if (shouldShowPrivacyPolicy()) {
            userInterface.navigateAfterQuestionnaireCompleted();
        } else {
            userInterface.showLoadingIndicator();
            questionnaireSubmitter.submit(questionnaireTypeKey);
        }
    }

    protected abstract boolean shouldShowPrivacyPolicy();

    @Override
    public void onBackButtonClicked() {
        userInterface.clearFocus();

        if (questionnaireStateManager.goToPreviousSection()) {
            userInterface.setQuestionnaireSectionTitle(questionnaireStateManager.getCurrentSectionTitle());
            loadSectionDetails();
        } else {
            userInterface.navigateToLandingScreen();
        }
    }

    @Override
    public void setQuestionnaireTypeKey(long questionnaireTypeKey) {
        this.questionnaireTypeKey = questionnaireTypeKey;
    }

    @Override
    public ValidationResult validate(Question question) {
        return questionnaireStateManager.validate(question);
    }

    @Override
    public Long getSectionTypeKey() {
        return questionnaireStateManager.getCurrentSectionTypeKey();
    }

    private boolean didSubmissionRequestFail() {
        return questionnaireSubmitter.getSubmissionRequestResult() != RequestResult.SUCCESSFUL &&
                questionnaireSubmitter.getSubmissionRequestResult() != RequestResult.NONE;
    }
}
