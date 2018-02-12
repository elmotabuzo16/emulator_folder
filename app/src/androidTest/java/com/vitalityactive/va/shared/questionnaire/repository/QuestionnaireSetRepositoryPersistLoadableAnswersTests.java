package com.vitalityactive.va.shared.questionnaire.repository;

import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.VitalityActiveApplication;
import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.questionnaire.types.MultiOptionOptionQuestionDto;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.types.QuestionBasicInputValueDto;
import com.vitalityactive.va.questionnaire.types.QuestionFactory;
import com.vitalityactive.va.questionnaire.types.TestQuestionFactory;
import com.vitalityactive.va.questionnaire.types.YesNoQuestionDto;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.vhr.content.QuestionnaireContentFromResourceString;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

@RepositoryTests
public class QuestionnaireSetRepositoryPersistLoadableAnswersTests extends RepositoryTestBase {
    private QuestionFactory questionFactory;
    private QuestionnaireSetRepository repository;

    @Before
    public void setUp() throws IOException {
        super.setUp();
        final QuestionnaireContentFromResourceString content = new QuestionnaireContentFromResourceString(new VitalityActiveApplication());
        questionFactory = new QuestionFactory(content);
        ValidationRuleMapper validationRuleFactory = new ValidationRuleMapper();
        repository = new QuestionnaireSetRepositoryImpl(dataStore, questionFactory, validationRuleFactory, 30);
    }

    private void persistAnswer(Question question) {
        repository.persistAnswer(question.getIdentifier(), question.getAnswer(), TimeUtilities.getCurrentTimestamp());
    }

    private void loadAnswer(Question question) {
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);
        questionFactory.loadAnswers(questions, repository.loadAnswers());
    }

    private void persistAndLoadValues(Question question) {
        persistAnswer(question);
        loadAnswer(question);
    }

    @Test
    public void persistable_yes_no() {
        YesNoQuestionDto question = (YesNoQuestionDto) TestQuestionFactory.drinkAlcohol();

        // set and save
        question.setValue(true);
        persistAnswer(question);

        // clear and load
        question = (YesNoQuestionDto) TestQuestionFactory.drinkAlcohol();
        loadAnswer(question);
        assertEquals(true, question.getValue());

        // change
        question.setValue(false);
        persistAndLoadValues(question);
        assertEquals(false, question.getValue());
    }

    @Test
    public void persistable_value_with_unit() {
        QuestionBasicInputValueDto question = TestQuestionFactory.waistMeasurement(0);

        // set and save
        question.setValue("123");
        question.setSelectedUnit(question.getUnits().get(1));
        persistAnswer(question);

        // clear and load
        question = TestQuestionFactory.waistMeasurement(0);
        loadAnswer(question);
        assertEquals("123", question.getValue());
        assertEquals("in", question.getSelectedUnit().getAbbreviation());

        // change
        question.setValue("10");
        question.setSelectedUnit(question.getUnits().get(1));
        persistAndLoadValues(question);
        assertEquals("10", question.getValue());
        assertEquals("in", question.getSelectedUnit().getAbbreviation());
    }

    @Test
    public void persistable_selected_items() {
        MultiOptionOptionQuestionDto question = TestQuestionFactory.medicalConditions(0);

        // check 1 item and save
        question.checkItem("Cancer");
        persistAnswer(question);

        // clear and load
        question = TestQuestionFactory.medicalConditions(0);
        loadAnswer(question);
        assertEquals(1, question.getCheckedItemCount());
        assertEquals("Cancer", question.getItems().get(1).value);
        assertEquals(true, question.getItems().get(1).checked);

        // check 2nd item
        question.checkItem("Asthma");
        persistAndLoadValues(question);
        assertEquals(2, question.getCheckedItemCount());
        assertEquals("Asthma", question.getItems().get(0).value);
        assertEquals(true, question.getItems().get(0).checked);
        assertEquals("Cancer", question.getItems().get(1).value);
        assertEquals(true, question.getItems().get(1).checked);
    }
}
