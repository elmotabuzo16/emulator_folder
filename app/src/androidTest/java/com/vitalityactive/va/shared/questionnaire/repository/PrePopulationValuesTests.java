package com.vitalityactive.va.shared.questionnaire.repository;

import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.VitalityActiveApplication;
import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.questionnaire.types.QuestionFactory;
import com.vitalityactive.va.shared.questionnaire.repository.model.AnswerModel;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetResponse;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.vhr.content.QuestionnaireContentFromResourceString;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RepositoryTests
public class PrePopulationValuesTests extends RepositoryTestBase {
    private QuestionnaireSetRepository repository;

    public void setUp() throws IOException {
        super.setUp();
        final QuestionnaireContentFromResourceString content = new QuestionnaireContentFromResourceString(new VitalityActiveApplication());
        ValidationRuleMapper validationRuleFactory = new ValidationRuleMapper();
        QuestionFactory questionFactory = new QuestionFactory(content);
        repository = new QuestionnaireSetRepositoryImpl(dataStore, questionFactory, validationRuleFactory, 30);
    }

    @Test
    public void questionnaire_set_with_pre_population_response_is_persisted() throws Exception {
        final QuestionnaireSetResponse response = getResponse(QuestionnaireSetResponse.class,
                "vhr_questionnaire_progress_and_points_tracker_with_pre_population.json");

        assertTrue(repository.persistQuestionnaireSetResponse(response));

        repository.createAnswersForPrePopulatedValues(1L);

        assertOneModelIsCreated(AnswerModel.class);

        // and the value is saved correctly
        assertEquals(10, repository.loadAnswers().get(42L).getIntValue());
    }

    @Test
    public void with_unit_of_measure() throws IOException {
        final QuestionnaireSetResponse response = getResponse(QuestionnaireSetResponse.class, "vhr/pre_population_with_unit_of_measure.json");
        assertTrue(repository.persistQuestionnaireSetResponse(response));

        repository.createAnswersForPrePopulatedValues(1L);

        // and the value is saved correctly
        assertEquals("100109", repository.loadAnswers().get(688L).getUnitOfMeasureKey());
    }

    @Test
    public void with_from_value_only() throws IOException {
        final QuestionnaireSetResponse response = getResponse(QuestionnaireSetResponse.class, "vhr/pre_population_with_unit_of_measure.json");
        assertTrue(repository.persistQuestionnaireSetResponse(response));

        repository.createAnswersForPrePopulatedValues(1L);

        // and the value is saved correctly
        assertNotNull(repository.loadAnswers().get(688L).getValue(), "value not loaded");
        assertEquals(75.0, repository.loadAnswers().get(688L).getFloatValue(), 0.00001);
    }

    @Test
    public void questionnaire_set_with_pre_population_single_answer_created_based_on_questionnaire_key() throws Exception {
        final QuestionnaireSetResponse response = getResponse(QuestionnaireSetResponse.class,
                "vhr_questionnaire_progress_and_points_tracker_with_pre_population.json");

        assertTrue(repository.persistQuestionnaireSetResponse(response));

        // not created
        assertNull(repository.loadAnswers().get(42L));
        assertNull(repository.loadAnswers().get(112L));

        // create first answer for first questionnaire
        repository.createAnswersForPrePopulatedValues(1L);
        assertNotNull(repository.loadAnswers().get(42L));
        assertNull(repository.loadAnswers().get(112L));

        // create second answer for 2nd questionnaire
        repository.createAnswersForPrePopulatedValues(3L);
        assertNotNull(repository.loadAnswers().get(42L));
        assertNotNull(repository.loadAnswers().get(112L));
    }

    @Test
    public void questionnaire_set_with_pre_population_response_do_not_override_user_entered_answers() throws Exception {
        // create an answer for the user
        repository.persistAnswer(42, new Answer("12"), TimeUtilities.getCurrentTimestamp());
        assertEquals(12, repository.loadAnswers().get(42L).getIntValue());

        // after a new questionnaire set response is loaded and persisted
        final QuestionnaireSetResponse response = getResponse(QuestionnaireSetResponse.class,
                "vhr_questionnaire_progress_and_points_tracker_with_pre_population.json");
        assertTrue(repository.persistQuestionnaireSetResponse(response));

        repository.createAnswersForPrePopulatedValues(1L);

        // then the answer is still 12 (not 10 as in the pre-populated values)
        assertEquals(12, repository.loadAnswers().get(42L).getIntValue());
    }

    @Test
    public void questionnaire_set_with_pre_population_response_override_old_user_entered_answers() throws Exception {
        // create an answer for the user that was entered long ago
        repository.persistAnswer(42, new Answer("13"), 0);
        assertEquals(0, repository.loadAnswers().size());

        // after a new questionnaire set response is loaded and persisted
        final QuestionnaireSetResponse response = getResponse(QuestionnaireSetResponse.class,
                "vhr_questionnaire_progress_and_points_tracker_with_pre_population.json");
        assertTrue(repository.persistQuestionnaireSetResponse(response));

        repository.createAnswersForPrePopulatedValues(1L);

        // then the answer is the pre-populated answer
        assertEquals(10, repository.loadAnswers().get(42L).getIntValue());
    }

    @Test
    public void valid_pre_populations_are_created() throws IOException {
        final QuestionnaireSetResponse response = getResponse(QuestionnaireSetResponse.class,
                "vhr/pre_population_with_unit_of_measure.json");
        assertTrue(repository.persistQuestionnaireSetResponse(response));

        repository.createAnswersForPrePopulatedValues(1L);

        // all answers are valid, so all are persisted
        assertEquals(2, repository.loadAnswers().size());
    }

    @Test
    public void invalid_pre_populations_are_not_created() throws IOException {
        final QuestionnaireSetResponse response = getResponse(QuestionnaireSetResponse.class,
                "vhr/pre_population_with_unit_of_measure_invalid.json");
        assertTrue(repository.persistQuestionnaireSetResponse(response));

        repository.createAnswersForPrePopulatedValues(1L);

        // all answers are invalid, so nothing is persisted
        assertEquals(0, repository.loadAnswers().size());
    }
}
