package com.vitalityactive.va.questionnaire.types.loadAnswers;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.VitalityActiveApplication;
import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.questionnaire.types.LabelWithAssociationsQuestionDto;
import com.vitalityactive.va.questionnaire.types.MultiOptionOptionQuestionDto;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.types.QuestionBasicInputValueDto;
import com.vitalityactive.va.questionnaire.types.QuestionDateInputDto;
import com.vitalityactive.va.questionnaire.types.QuestionFactory;
import com.vitalityactive.va.questionnaire.types.QuestionFreeTextDto;
import com.vitalityactive.va.questionnaire.types.SingleCheckboxQuestionDto;
import com.vitalityactive.va.questionnaire.types.SingleSelectOptionQuestionDto;
import com.vitalityactive.va.questionnaire.types.TestQuestionFactory;
import com.vitalityactive.va.questionnaire.types.YesNoQuestionDto;
import com.vitalityactive.va.vhr.content.QuestionnaireContentFromResourceString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("UseSparseArrays")
class BaseLoadAnswer extends BaseTest {
    protected List<Question> questions;
    protected YesNoQuestionDto yesNoQuestion;
    protected SingleCheckboxQuestionDto checkboxQuestion;
    protected QuestionDateInputDto dateQuestion;
    protected QuestionBasicInputValueDto basicInputValueQuestion;
    protected SingleSelectOptionQuestionDto singleSelectQuestion;
    protected MultiOptionOptionQuestionDto multiSelectQuestion;
    protected LabelWithAssociationsQuestionDto labaelWithAssociationsQuestion;
    protected QuestionBasicInputValueDto waistQuestion;
    protected QuestionFreeTextDto freeTextQuestion;
    protected QuestionFactory questionFactory;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initialiseAndroidThreeTen();
        questionFactory = getQuestionFactory();

        questions = new ArrayList<>();
        yesNoQuestion = (YesNoQuestionDto) TestQuestionFactory.drinkAlcohol();
        checkboxQuestion = TestQuestionFactory.allergies();
        dateQuestion = (QuestionDateInputDto) TestQuestionFactory.datePickerQuestion(questionFactory);
        basicInputValueQuestion = TestQuestionFactory.fruitPerDay(5);
        singleSelectQuestion = TestQuestionFactory.higherFatCookingMethods(6);
        multiSelectQuestion = TestQuestionFactory.medicalConditions(8);
        waistQuestion = TestQuestionFactory.waistMeasurement(9);
        labaelWithAssociationsQuestion = TestQuestionFactory.smokingQuestions(10);
        freeTextQuestion = (QuestionFreeTextDto) TestQuestionFactory.freeTextTextBoxQuestion(questionFactory);

        questions.add(yesNoQuestion);
        questions.add(checkboxQuestion);
        questions.add(dateQuestion);
        questions.add(basicInputValueQuestion);
        questions.add(singleSelectQuestion);
        questions.add(multiSelectQuestion);
        questions.add(waistQuestion);
        questions.add(freeTextQuestion);
        questions.add(labaelWithAssociationsQuestion);
    }

    @NonNull
    private QuestionFactory getQuestionFactory() {
        final VitalityActiveApplication vitalityActiveApplication = new VitalityActiveApplication();
        final QuestionnaireContentFromResourceString content = new QuestionnaireContentFromResourceString(vitalityActiveApplication);
        return new QuestionFactory(content);
    }

    protected void saveAnswersAndLoadThem(Question question) {
        HashMap<Long, Answer> answers = new HashMap<>();
        answers.put(question.getIdentifier(), question.getAnswer());
        questionFactory.loadAnswers(questions, answers);
    }

    protected void saveAnswerStringAndLoadThem(Question question, String valueToSave) {
        HashMap<Long, Answer> answers = new HashMap<>();
        answers.put(question.getIdentifier(), new Answer(valueToSave));
        questionFactory.loadAnswers(questions, answers);
    }
}
