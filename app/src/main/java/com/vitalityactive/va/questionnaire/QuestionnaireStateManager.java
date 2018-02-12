package com.vitalityactive.va.questionnaire;

import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.validations.ValidationResult;

import java.util.List;

public interface QuestionnaireStateManager {
    long getTotalSections();
    int getCurrentSectionIndex();
    boolean goToNextSection();
    boolean goToPreviousSection();
    List<Question> getAllQuestionsInCurrentSection();
    List<Question> getUnansweredQuestionsInCurrentSection();
    List<Question> getInvalidAnsweredQuestionsInCurrentSection();
    List<Question> getAllValidAnsweredQuestionsForQuestionnaire(long questionnaireTypeKey);
    ValidationResult answer(Question question);
    String getCurrentSectionTitle();
    void setQuestionnaireTypeKey(long questionnaireTypeKey);
    ValidationResult validate(Question question);
    void clearQuestionnaireProgress();
    Long getCurrentSectionTypeKey();
    String getQuestionnaireTitle();
}
