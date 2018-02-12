package com.vitalityactive.va.questionnaire.validations;

import android.support.annotation.CallSuper;

import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.questionnaire.types.LabelWithAssociationsQuestionDto;
import com.vitalityactive.va.questionnaire.types.QuestionBasicInputValueDto;
import com.vitalityactive.va.questionnaire.types.QuestionFreeTextDto;
import com.vitalityactive.va.questionnaire.types.TestQuestionFactory;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

class ValidatorBaseTest {
    protected Validator validator;
    protected QuestionBasicInputValueDto fruitPerDay;
    protected LabelWithAssociationsQuestionDto smokingData;
    protected QuestionFreeTextDto name;
    protected ArrayList<ValidationRule> rulesToReturn;
    protected QuestionBasicInputValueDto waist;

    @Mock
    QuestionnaireSetRepository repository;

    @Before
    @CallSuper
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        validator = new Validator(repository);
        createQuestions();
        setupRepositoryDefaults();
    }

    private void createQuestions() {
        fruitPerDay = TestQuestionFactory.fruitPerDay(1);
        name = TestQuestionFactory.whatIsYourName(1);
        waist = TestQuestionFactory.waistMeasurement(1);
        smokingData = TestQuestionFactory.smokingQuestions(1);
    }

    private void setupRepositoryDefaults() {
        rulesToReturn = new ArrayList<>();
        when(repository.getQuestionValidationRules(anyLong())).thenReturn(rulesToReturn);
    }
}
