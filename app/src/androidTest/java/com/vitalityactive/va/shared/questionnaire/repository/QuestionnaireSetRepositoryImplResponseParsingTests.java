package com.vitalityactive.va.shared.questionnaire.repository;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.VitalityActiveApplication;
import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.questionnaire.types.QuestionFactory;
import com.vitalityactive.va.shared.questionnaire.repository.model.QuestionnaireSetModel;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetResponse;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;
import com.vitalityactive.va.vhr.content.QuestionnaireContentFromResourceString;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
@RepositoryTests
public class QuestionnaireSetRepositoryImplResponseParsingTests extends RepositoryTestBase {
    private QuestionnaireSetRepository repository;

    public void setUp() throws IOException {
        super.setUp();
        final QuestionnaireContentFromResourceString content = new QuestionnaireContentFromResourceString(new VitalityActiveApplication());
        ValidationRuleMapper validationRuleFactory = new ValidationRuleMapper();
        QuestionFactory questionFactory = new QuestionFactory(content);
        repository = new QuestionnaireSetRepositoryImpl(dataStore, questionFactory, validationRuleFactory, 30);
    }

    @Test
    public void questionnaire_set_response_is_persisted() throws Exception {
        final QuestionnaireSetResponse response = getResponse(QuestionnaireSetResponse.class,
                "vhr_questionnaire_progress_and_points_tracker_1.json");

        assertTrue(repository.persistQuestionnaireSetResponse(response));

        assertOneModelIsCreated(QuestionnaireSetModel.class);
    }

    @Test
    public void parsing_fails_if_total_potential_points_missing() throws Exception {
        assertModelIsNotCreated(QuestionnaireSetModel.class);
    }

    @Test
    public void nothing_is_persisted_if_parsing_fails() throws IOException {
        assertNothingIsPersisted("vhr_questionnaire_progress_and_points_tracker_points_missing.json");
    }

    private void assertNothingIsPersisted(String path) throws IOException {
        boolean parsingResult = repository.persistQuestionnaireSetResponse(getResponse(QuestionnaireSetResponse.class, path));

        assertFalse(parsingResult);
        assertTrue(getRealm().isEmpty());
    }
}
