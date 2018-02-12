package com.vitalityactive.va.questionnaire;

import android.support.annotation.NonNull;

import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.validations.ValidationRule;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireDTO;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireSetInformation;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetResponse;

import java.util.List;
import java.util.Map;

public interface QuestionnaireSetRepository {
    void persistAnswer(long questionId, Answer answer, long answeredTimestamp);

    void createAnswersForPrePopulatedValues(long questionnaireTypeKey);

    long getNumberOfVisibleQuestionnaireSections(long questionnaireTypeKey);

    List<QuestionnaireSection> getQuestionnaireSections(long questionnaireTypeKey);

    int updateQuestionnaireSectionVisibility(List<QuestionnaireSection> sections);

    Map<Long, Answer> loadAnswers();

    void setLastSeenSection(long questionnaireKey, long sectionTypeKey);

    Long getLastSeenSection(long questionnaireKey);

    boolean doesQuestionExist(long questionTypeKey, long questionnaireTypeKey, long lastSeenSectionTypeKey);

    @NonNull
    String getQuestionnaireSectionTitle(long questionnaireTypeKey, int sectionIndex);

    List<Question> getAllQuestionsInVisibleSectionAtIndex(long questionnaireTypeKey, int sectionIndex);

    List<ValidationRule> getQuestionValidationRules(long questionTypeKey);

    String getQuestionnaireTitle(long questionnaireTypeKey);

    List<Question> getAllQuestionsInQuestionnaire(long questionnaireTypeKey);

    void clearSectionProgress(long questionnaireTypeKey);

    void clearAnswers(long questionnaireTypeKey);

    String getSectionDescription(long sectionTypeKey);

    Long getCurrentSectionTypeKey(long questionnaireTypeKey);

    void setQuestionnaireCompletedFlagToTrue(long questionnaireTypeKey);

    Boolean persistQuestionnaireSetResponse(QuestionnaireSetResponse questionnaireSetResponse);

    List<QuestionnaireDTO> getUnansweredQuestionnairesTopLevelData();

    QuestionnaireSetInformation getQuestionnairesSetTopLevelData();

    List<QuestionnaireDTO> getQuestionnairesTopLevelData();

    List<Long> getVisibleSectionKeys(long questionnaireKey);

    void prepare();
}
