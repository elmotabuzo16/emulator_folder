package com.vitalityactive.va.shared.questionnaire.repository;

import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.VitalityActiveApplication;
import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.questionnaire.types.QuestionFactory;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.vhr.content.QuestionnaireContentFromResourceString;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

@RepositoryTests
public class QuestionnaireSetRepositoryImplTest extends RepositoryTestBase {
    private QuestionnaireSetRepository repository;

    @Before
    public void setUp() throws IOException {
        super.setUp();
        final QuestionnaireContentFromResourceString content = new QuestionnaireContentFromResourceString(new VitalityActiveApplication());
        QuestionFactory questionFactory = new QuestionFactory(content);
        ValidationRuleMapper validationRuleFactory = new ValidationRuleMapper();
        repository = new QuestionnaireSetRepositoryImpl(dataStore, questionFactory, validationRuleFactory, 30);
    }

    @Test
    public void persist_answer() {
        repository.persistAnswer(123, new Answer("456"), TimeUtilities.getCurrentTimestamp());

        Map<Long, Answer> answers = repository.loadAnswers();

        Assert.assertNotNull(answers);
        Assert.assertTrue(answers.containsKey(123L));
        Assert.assertEquals("456", answers.get(123L).getValue());
    }

    @Test
    public void persist_answer_overwrites() {
        repository.persistAnswer(123, new Answer("456"), TimeUtilities.getCurrentTimestamp());
        repository.persistAnswer(123, new Answer("789"), TimeUtilities.getCurrentTimestamp());

        Map<Long, Answer> answers = repository.loadAnswers();

        Assert.assertNotNull(answers);
        Assert.assertTrue(answers.containsKey(123L));
        Assert.assertEquals("789", answers.get(123L).getValue());
    }

    @Test
    public void load_does_not_load_old_answers() {
        repository.persistAnswer(123, new Answer("456"), 0);

        Map<Long, Answer> answers = repository.loadAnswers();

        Assert.assertNotNull(answers);
        Assert.assertNull(answers.get(123L));
    }
}
