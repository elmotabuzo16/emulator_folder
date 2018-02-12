package com.vitalityactive.va.questionnaire;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.types.QuestionBasicInputValueDto;
import com.vitalityactive.va.questionnaire.types.QuestionFactory;
import com.vitalityactive.va.questionnaire.types.SingleSelectOptionQuestionDto;
import com.vitalityactive.va.questionnaire.types.TestQuestionFactory;
import com.vitalityactive.va.questionnaire.types.YesNoQuestionDto;
import com.vitalityactive.va.shared.questionnaire.repository.QuestionnaireSetRepositoryImpl;
import com.vitalityactive.va.shared.questionnaire.repository.ValidationRuleMapper;
import com.vitalityactive.va.shared.questionnaire.repository.model.AnswerModel;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetResponse;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;
import com.vitalityactive.va.vhr.content.QuestionnaireContentFromResourceString;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
@RepositoryTests
public class QuestionnaireStateManagerImplTest extends RepositoryTestBase {
    private QuestionnaireSetRepository repository;
    private QuestionnaireStateManager stateManager;

    public void setUp() throws IOException {
        super.setUp();
        final QuestionnaireContentFromResourceString content = new QuestionnaireContentFromResourceString(InstrumentationRegistry.getTargetContext());
        QuestionFactory questionFactory = new QuestionFactory(content);
        ValidationRuleMapper validationRuleFactory = new ValidationRuleMapper();
        repository = new QuestionnaireSetRepositoryImpl(dataStore, questionFactory, validationRuleFactory, 30);
        stateManager = new QuestionnaireStateManagerImpl(repository, questionFactory);
        stateManager.setQuestionnaireTypeKey(1);
    }

    @Test
    public void it_returns_the_correct_number_of_questionnaire_sections() throws Exception {
        persistResponseWithThreeSections();

        assertEquals(3, stateManager.getTotalSections());
    }

    @Test
    public void it_creates_default_answers_for_checkbox_questions() throws Exception {
        persistResponseWithThreeSections();

        Answer answerAfterCreated = repository.loadAnswers().get(10L);
        assertNull("expected no answer when created", answerAfterCreated);

        stateManager.getAllQuestionsInCurrentSection();

        Answer answerAfterLoaded = repository.loadAnswers().get(10L);
        assertNotNull("expected an answer after questions loaded", answerAfterLoaded);
    }

    @Test
    public void can_navigate_sections() throws Exception {
        persistResponseWithThreeSections();

        assertEquals(0, stateManager.getCurrentSectionIndex());

        assertTrue(stateManager.goToNextSection());
        assertEquals(1, stateManager.getCurrentSectionIndex());

        assertTrue(stateManager.goToPreviousSection());
        assertEquals(0, stateManager.getCurrentSectionIndex());
    }

    @Test
    public void when_section_navigation_fails_do_not_update_questionnaire_progress() throws Exception {
        persistResponseWithThreeSections();

        // index starts at 0
        assertEquals(0, stateManager.getCurrentSectionIndex());

        // fails, index is still 0
        assertFalse(stateManager.goToPreviousSection());
        assertEquals(0, stateManager.getCurrentSectionIndex());

        assertTrue(stateManager.goToNextSection());
        assertTrue(stateManager.goToNextSection());

        // fails, and index is still 2
        assertFalse(stateManager.goToNextSection());
        assertEquals(2, stateManager.getCurrentSectionIndex());
    }

    @Test
    public void do_not_persist_if_invalid_question_id() {
        QuestionBasicInputValueDto question = new QuestionBasicInputValueDto(1, 0, 0, null, null, null, null, 0, 1);
        question.setValue("123");

        stateManager.answer(question);

        assertFalse(dataStore.hasModelInstance(AnswerModel.class));
    }

    @Test
    public void persist_on_valid_question_answered() throws Exception {
        persistResponseWithThreeSections();

        QuestionBasicInputValueDto question = new QuestionBasicInputValueDto(66, 0, 0, null, null, null, null, 0, 1);

        question.setValue("Heart Disease");
        stateManager.answer(question);

        AnswerModel answer = dataStore.getModelInstance(AnswerModel.class, new DataStore.ModelMapper<AnswerModel, AnswerModel>() {
            @Override
            public AnswerModel mapModel(AnswerModel model) {
                return model;
            }
        }, "questionId", question.getIdentifier());

        assertEquals("Heart Disease", answer.getAnswer().getValue());
    }

    @Test
    public void get_current_section_title() throws Exception {
        persistResponseWithThreeSections();

        assertEquals("Your Wellbeing", stateManager.getCurrentSectionTitle());

        stateManager.goToNextSection();
        assertEquals("Your Eating Habits", stateManager.getCurrentSectionTitle());

        stateManager.goToNextSection();
        assertEquals("Your General Health", stateManager.getCurrentSectionTitle());

        stateManager.goToPreviousSection();
        assertEquals("Your Eating Habits", stateManager.getCurrentSectionTitle());
    }

    @Test
    public void get_all_questions_in_current_section_returns_the_correct_number_of_questions() throws Exception {
        persistResponseWithThreeSections();

        assertEquals(2, stateManager.getAllQuestionsInCurrentSection().size());
    }

    @Test
    public void get_all_questions_in_current_section_updates_can_be_answered_correctly_for_questions_with_visibility_tag_names() throws Exception {
        persistResponseWithThreeSections();

        stateManager.goToNextSection();
        stateManager.goToNextSection();

        List<Question> questions = stateManager.getAllQuestionsInCurrentSection();

        assertFalse(questions.get(0).getCanBeAnswered());
    }

    @Test
    public void get_all_questions_in_current_section_sets_can_be_answered_true_for_questions_without_visibility_tag_names() throws Exception {
        persistResponseWithThreeSections();

        List<Question> questions = stateManager.getAllQuestionsInCurrentSection();

        assertTrue(questions.get(0).getCanBeAnswered());
    }

    @Test
    public void get_all_questions_in_current_section_sets_can_be_answered_true_for_questions_with_answered_parents() throws Exception {
        persistResponseWithThreeSections();
        YesNoQuestionDto question = new YesNoQuestionDto(76, 0, 0, null, null, null, 1, "PositiveResponse", "NegativeResponse", "Yes", "No");
        question.setValue(true);
        stateManager.goToNextSection();
        stateManager.goToNextSection();

        stateManager.answer(question);

        List<Question> questions = stateManager.getAllQuestionsInCurrentSection();
        Collections.sort(questions, new Comparator<Question>() {
            @Override
            public int compare(Question o1, Question o2) {
                return Float.compare(o1.getSortOrder(), o2.getSortOrder());
            }
        });

        assertTrue(questions.get(0).getCanBeAnswered());
    }

    @Test
    public void get_unanswered_questions_in_current_section_returns_all_if_none_have_been_answered() throws Exception {
        persistResponseWithThreeSections();

        // the first section contains only a question that defaults to answered - the second section contains a single question that is answerable and defaults to unanswered
        stateManager.goToNextSection();

        assertEquals(1, stateManager.getUnansweredQuestionsInCurrentSection().size());
    }

    @Test
    public void get_unanswered_questions_in_current_section_returns_none_if_all_have_been_answered() throws Exception {
        persistResponseWithThreeSections();
        SingleSelectOptionQuestionDto question = new SingleSelectOptionQuestionDto(4, 0, 0, null, null, null, 1);
        question.addItem("GenderMale", null, null, null);
        question.checkItem("GenderMale");

        // the first section contains only a question that defaults to answered - the second section contains a single question that is answerable and defaults to unanswered
        stateManager.goToNextSection();

        stateManager.answer(question);

        assertEquals(0, stateManager.getUnansweredQuestionsInCurrentSection().size());
    }

    @Test
    public void get_unanswered_questions_in_current_section_returns_only_unanswered_and_answerable_questions() throws Exception {
        persistResponseWithThreeSections();

        // the second section contains a single question that is answerable and defaults to unanswered
        stateManager.goToNextSection();
        assertEquals(1, stateManager.getUnansweredQuestionsInCurrentSection().size());

        // the third section contains a question that defaults to answered and an unanswerable question (it depends on the other question via a visibilityTag)
        // and another question that is unanswered
        stateManager.goToNextSection();
        assertEquals(2, stateManager.getUnansweredQuestionsInCurrentSection().size());
    }

    @Test
    public void validate_answer_on_answered() throws Exception {
        persistResponseWithThreeSections();
        QuestionBasicInputValueDto question = TestQuestionFactory.waistMeasurement(78);

        // go to the 3rd section, since it has the question with id 78
        stateManager.goToNextSection();
        stateManager.goToNextSection();

        question.setSelectedUnit(question.getUnits().get(0));

        // when answered with an invalid value
        question.setValue("20000");
        boolean answerIsValid = stateManager.answer(question).isValid();
        assertFalse(answerIsValid);

        // when answered with a valid value
        question.setValue("10");
        answerIsValid = stateManager.answer(question).isValid();
        assertTrue(answerIsValid);
    }

    @Test
    public void get_invalid_answered_questions_in_current_section() throws Exception {
        persistResponseWithThreeSections();
        QuestionBasicInputValueDto question = TestQuestionFactory.waistMeasurement(78);
        question.setSelectedUnit(question.getUnits().get(0));

        // go to the 3rd section, since it has the question with id 78
        stateManager.goToNextSection();
        stateManager.goToNextSection();

        // when answered with validation issues...
        question.setValue("20000");
        stateManager.answer(question);
        // ... then 1 question is answered invalid
        assertEquals(1, stateManager.getInvalidAnsweredQuestionsInCurrentSection().size());

        // when answered without validation issues...
        question.setValue("10");
        stateManager.answer(question);
        // ... then no questions in the section has invalid answers
        assertEquals(0, stateManager.getInvalidAnsweredQuestionsInCurrentSection().size());
    }

    // VA-16217
    @Test
    public void no_crash_on_missing_length() throws IOException {
        final QuestionnaireSetResponse response = getResponse(QuestionnaireSetResponse.class, "questionnairestatemanager/question_without_length.json");
        repository.persistQuestionnaireSetResponse(response);

        stateManager.getAllQuestionsInCurrentSection();
    }

    private void persistResponseWithThreeSections() throws java.io.IOException {
        final QuestionnaireSetResponse response = getResponse(QuestionnaireSetResponse.class, "questionnairestatemanager/questionnaire_state_manager_3_sections_response.json");
        repository.persistQuestionnaireSetResponse(response);
    }
}
