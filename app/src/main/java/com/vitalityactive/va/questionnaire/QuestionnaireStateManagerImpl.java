package com.vitalityactive.va.questionnaire;

import android.util.Log;

import com.vitalityactive.va.questionnaire.dependencies.DependencyChecker;
import com.vitalityactive.va.questionnaire.types.LabelWithAssociationsQuestionDto;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.types.QuestionFactory;
import com.vitalityactive.va.questionnaire.validations.ValidationResult;
import com.vitalityactive.va.questionnaire.validations.Validator;
import com.vitalityactive.va.utilities.MethodCallTrace;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.TimeUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionnaireStateManagerImpl implements QuestionnaireStateManager {
    private QuestionnaireSetRepository repository;
    private long questionnaireTypeKey;
    private QuestionFactory questionFactory;
    private Validator validator;

    public QuestionnaireStateManagerImpl(QuestionnaireSetRepository repository,
                                         QuestionFactory questionFactory) {
        this.repository = repository;
        this.questionFactory = questionFactory;
        validator = new Validator(repository);
    }

    @Override
    public long getTotalSections() {
        return repository.getNumberOfVisibleQuestionnaireSections(questionnaireTypeKey);
    }

    @Override
    public int getCurrentSectionIndex() {
        Long section = repository.getLastSeenSection(questionnaireTypeKey);
        if (section == null) {
            return 0;
        }

        List<Long> keys = getVisibleSectionKeys();
        return keys.indexOf(section);
    }

    @Override
    public boolean goToNextSection() {
        int nextSectionIndex = getCurrentSectionIndex() + 1;
        if (nextSectionIndex >= getTotalSections()) {
            return false;
        }
        List<Long> keys = getVisibleSectionKeys();
        repository.setLastSeenSection(questionnaireTypeKey, keys.get(nextSectionIndex));
        return true;
    }

    @Override
    public boolean goToPreviousSection() {
        int previousSectionIndex = getCurrentSectionIndex() - 1;
        if (previousSectionIndex < 0) {
            return false;
        }
        List<Long> keys = getVisibleSectionKeys();
        repository.setLastSeenSection(questionnaireTypeKey, keys.get(previousSectionIndex));
        return true;
    }

    @Override
    public List<Question> getAllQuestionsInCurrentSection() {
        int sectionIndex = getCurrentSectionIndex();
        List<Question> allQuestionsInCurrentSection = repository.getAllQuestionsInVisibleSectionAtIndex(questionnaireTypeKey, sectionIndex);

        loadAnswers(allQuestionsInCurrentSection);
        updateCanBeAnswered(allQuestionsInCurrentSection);

        addSectionDescription(allQuestionsInCurrentSection);

        return allQuestionsInCurrentSection;
    }

    private void addSectionDescription(List<Question> allQuestionsInCurrentSection) {
        Long sectionTypeKey = repository.getCurrentSectionTypeKey(questionnaireTypeKey);

        String sectionDescription = repository.getSectionDescription(sectionTypeKey);

        if (!TextUtilities.isNullOrWhitespace(sectionDescription)) {
            allQuestionsInCurrentSection.add(0, questionFactory.sectionDescription(sectionDescription, sectionTypeKey));
        }
    }

    private void loadAnswers(List<Question> questions) {
        Map<Long, Answer> answers = repository.loadAnswers();
        for (Question question : questions) {
            if (!answers.containsKey(question.getIdentifier()) && question.shouldCreateDefaultAnswer()) {
                answer(question);
            }
        }
        questionFactory.loadAnswers(questions, answers);
    }

    private void updateCanBeAnswered(List<Question> allQuestionsInCurrentSection) {
        List<QuestionnaireSection> sections = repository.getQuestionnaireSections(questionnaireTypeKey);
        DependencyChecker dependencyChecker = new DependencyChecker();
        loadQuestionsFromPreviousSections(dependencyChecker);
        dependencyChecker.addQuestions(allQuestionsInCurrentSection);
        dependencyChecker.addSections(sections);
        dependencyChecker.update();
        repository.updateQuestionnaireSectionVisibility(sections);
    }

    private void loadQuestionsFromPreviousSections(DependencyChecker dependencyChecker) {
        int currentSectionIndex = getCurrentSectionIndex();
        for (int i = 0; i < currentSectionIndex; i++) {
            List<Question> previousQuestions = repository.getAllQuestionsInVisibleSectionAtIndex(questionnaireTypeKey, i);
            loadAnswers(previousQuestions);
            dependencyChecker.addReferenceQuestions(previousQuestions);
        }
    }

    @Override
    public List<Question> getUnansweredQuestionsInCurrentSection() {
        List<Question> allQuestionsInCurrentSection = getAllQuestionsInCurrentSection();
        List<Question> unansweredQuestionsInCurrentSection = new ArrayList<>();
        for (Question question : allQuestionsInCurrentSection) {
            if (question.getCanBeAnswered() && isQuestionAssociationsAvailable(question)) {
                List<Integer> childTypeKeys = question.getAssociatedChildTypeKeys();
                for (int typeKey : childTypeKeys) {
                    if (!((LabelWithAssociationsQuestionDto) question).isAnsweredByTypeKey(typeKey)) {
                        unansweredQuestionsInCurrentSection.add(question);
                    }
                }
            } else if (question.getCanBeAnswered() && !question.isAnswered() && !question.isChildQuestion()) {
                unansweredQuestionsInCurrentSection.add(question);
            }
        }

        return unansweredQuestionsInCurrentSection;
    }

    @Override
    public List<Question> getInvalidAnsweredQuestionsInCurrentSection() {
        List<Question> invalid = new ArrayList<>();
        List<Question> allQuestionsInCurrentSection = getAllQuestionsInCurrentSection();
        for (Question question : allQuestionsInCurrentSection) {
            if (question.getCanBeAnswered() && isQuestionAssociationsAvailable(question)) {
                List<Integer> childTypeKeys = question.getAssociatedChildTypeKeys();
                for (int typeKey : childTypeKeys) {
                    LabelWithAssociationsQuestionDto questionChild = (LabelWithAssociationsQuestionDto) question;
                    questionChild.setActiveChildTypeKeyForValidation(typeKey);
                    if (questionChild.isAnsweredByTypeKey(typeKey) && !(validator.validate(questionChild)).isValid()) {
                        invalid.add(question);
                    }
                }
            } else if (question.getCanBeAnswered() && question.isAnswered()) {
                if (!(validator.validate(question)).isValid()) {
                    invalid.add(question);
                }
            }
        }
        return invalid;
    }

    @Override
    public List<Question> getAllValidAnsweredQuestionsForQuestionnaire(long questionnaireTypeKey) {
        List<Question> valid = new ArrayList<>();
        List<Question> allQuestionsInQuestionnaire = repository.getAllQuestionsInQuestionnaire(questionnaireTypeKey);

        loadAnswers(allQuestionsInQuestionnaire);
        updateCanBeAnswered(allQuestionsInQuestionnaire);

        for (Question question : allQuestionsInQuestionnaire) {
            if (question.getCanBeAnswered() && question.isAnswered() && isQuestionAssociationsAvailable(question)) {
                List<Integer> childTypeKeys = question.getAssociatedChildTypeKeys();
                valid.add(question);
                for (int typeKey : childTypeKeys) {
                    ((LabelWithAssociationsQuestionDto) question).setActiveChildTypeKeyForValidation(typeKey);
                    if (validator.validate(question).isValid()) {
                        if (question.getAnswer().getValues().size() > 0) {
                            Question childQuestion = getQuestionByIdentifier(allQuestionsInQuestionnaire, typeKey);
                            valid.add(childQuestion);
                            if (!isParentQuestionAlreadyAdded(allQuestionsInQuestionnaire, (int) question.getIdentifier())) {
                                valid.add(question);
                            }
                        }
                    }
                }
            } else if (question.getCanBeAnswered() && question.isAnswered()) {
                if (validator.validate(question).isValid()) {
                    if (question.getAnswer().getValues().size() > 0)
                        valid.add(question);
                }
            }
        }
        return valid;
    }

    private boolean isQuestionAssociationsAvailable(Question question) {
        return question.getAssociatedChildTypeKeys() != null && question.getAssociatedChildTypeKeys().size() > 0
                && question instanceof LabelWithAssociationsQuestionDto;
    }

    private Question getQuestionByIdentifier(List<Question> questions, int identifier) {
        for (Question question : questions) {
            if ((int) question.getIdentifier() == identifier) {
                return question;
            }
        }
        return null;
    }

    private boolean isParentQuestionAlreadyAdded(List<Question> valid, int parentTypeKey) {
        for (Question validQuestion : valid) {
            if (validQuestion.getIdentifier() == parentTypeKey) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ValidationResult answer(Question question) {
        MethodCallTrace.start();
        if (questionDoesNotExist(question)) {
            MethodCallTrace.stop();
            return new ValidationResult(false);
        }
        ValidationResult validationResult = validator.validate(question);
        repository.persistAnswer(question.getIdentifier(), question.getAnswer(), TimeUtilities.getCurrentTimestamp());
        MethodCallTrace.stop();
        return validationResult;
    }

    @Override
    public String getCurrentSectionTitle() {
        return repository.getQuestionnaireSectionTitle(questionnaireTypeKey, getCurrentSectionIndex());
    }

    @Override
    public void setQuestionnaireTypeKey(long questionnaireTypeKey) {
        this.questionnaireTypeKey = questionnaireTypeKey;
        repository.createAnswersForPrePopulatedValues(questionnaireTypeKey);
    }

    @Override
    public void clearQuestionnaireProgress() {
        repository.clearSectionProgress(questionnaireTypeKey);
        repository.clearAnswers(questionnaireTypeKey);
    }

    @Override
    public Long getCurrentSectionTypeKey() {
        return repository.getCurrentSectionTypeKey(questionnaireTypeKey);
    }

    @Override
    public String getQuestionnaireTitle() {
        return repository.getQuestionnaireTitle(questionnaireTypeKey);
    }

    @Override
    public ValidationResult validate(Question question) {
        if (questionDoesNotExist(question)) {
            return new ValidationResult(false);
        }
        return validator.validate(question);
    }

    private boolean questionDoesNotExist(Question question) {
        long trace = MethodCallTrace.enterMethod();
        Long lastSeenSection = getLastSeenSectionTypeKeyOrTheFirstSectionIfNoProgress();
        if (lastSeenSection == null) {
            MethodCallTrace.exitMethod(trace);
            return true;
        }

        boolean exists = repository.doesQuestionExist(question.getIdentifier(), questionnaireTypeKey, lastSeenSection);
        MethodCallTrace.exitMethod(trace);
        return !exists;
    }

    private Long getLastSeenSectionTypeKeyOrTheFirstSectionIfNoProgress() {
        Long lastSeenSection = repository.getLastSeenSection(questionnaireTypeKey);
        if (lastSeenSection == null) {
            List<Long> visibleSectionKeys = getVisibleSectionKeys();
            if (visibleSectionKeys.size() == 0) {
                return null;
            }
            lastSeenSection = visibleSectionKeys.get(0);
        }
        return lastSeenSection;
    }

    private List<Long> getVisibleSectionKeys() {
        return repository.getVisibleSectionKeys(questionnaireTypeKey);
    }
}
