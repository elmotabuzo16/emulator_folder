package com.vitalityactive.va.questionnaire.types.loadAnswers;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.text.InputType;

import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.shared.questionnaire.repository.model.AnswerModel;
import com.vitalityactive.va.shared.questionnaire.repository.model.PopulationValueModel;
import com.vitalityactive.va.utilities.date.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("UseSparseArrays")
public class QuestionFactoryLoadAnswerTests extends BaseLoadAnswer {
    @Test
    public void load_answers() {
        Long questionId = yesNoQuestion.getIdentifier();
        HashMap<Long, Answer> answers = new HashMap<>();
        answers.put(questionId, new Answer(true));

        questionFactory.loadAnswers(questions, answers);

        Assert.assertEquals(true, yesNoQuestion.getValue());
    }

    @Test
    public void load_answer_only_for_available_answers() {
        yesNoQuestion.setValue(true);

        HashMap<Long, Answer> answers = new HashMap<>();
        questionFactory.loadAnswers(questions, answers);

        Assert.assertEquals("expected value to be unchanged",
                true, yesNoQuestion.getValue());
    }

    @Test
    public void only_load_for_answers_with_values() {
        // set empty answers
        HashMap<Long, Answer> answers = new HashMap<>();
        for (Question question : questions) {
            answers.put(question.getIdentifier(), Answer.blank());
        }

        // and ui has values
        yesNoQuestion.setValue(true);

        // when loaded
        questionFactory.loadAnswers(questions, answers);

        // nothing fails..., and values are unchanged
        Assert.assertEquals("expected value to be unchanged",
                true, yesNoQuestion.getValue());
    }

    @Test
    public void only_load_if_there_are_any_questions() {
        // and ui has values
        yesNoQuestion.setValue(true);

        // when loaded
        questionFactory.loadAnswers(null, new HashMap<Long, Answer>());

        // nothing fails..., and values are unchanged
        Assert.assertEquals("expected value to be unchanged",
                true, yesNoQuestion.getValue());
    }

    @Test
    public void only_load_if_there_are_any_answers() {
        // and ui has values
        yesNoQuestion.setValue(true);

        // when loaded
        questionFactory.loadAnswers(questions, null);

        // nothing fails..., and values are unchanged
        Assert.assertEquals("expected value to be unchanged",
                true, yesNoQuestion.getValue());
    }

    @Test
    public void load_checkbox() {
        checkboxQuestion.setValue(true);
        saveAnswersAndLoadThem(checkboxQuestion);
        Assert.assertEquals(true, checkboxQuestion.getValue());

        checkboxQuestion.setValue(false);
        saveAnswersAndLoadThem(checkboxQuestion);
        Assert.assertEquals(false, checkboxQuestion.getValue());
    }

    @Test
    public void load_date() {
        LocalDate date = new LocalDate(2017, 1, 1);
        dateQuestion.setValue(date);
        saveAnswersAndLoadThem(dateQuestion);
        Assert.assertEquals(date, dateQuestion.getValue());

        LocalDate laterDate = new LocalDate(2017, 5, 1);
        dateQuestion.setValue(laterDate);
        saveAnswersAndLoadThem(dateQuestion);
        Assert.assertEquals(laterDate, dateQuestion.getValue());
    }

    @Test
    public void load_basic_input() {
        basicInputValueQuestion.setValue("14");
        saveAnswersAndLoadThem(basicInputValueQuestion);
        Assert.assertEquals("14", basicInputValueQuestion.getValue());

        basicInputValueQuestion.setValue("234");
        saveAnswersAndLoadThem(basicInputValueQuestion);
        Assert.assertEquals("234", basicInputValueQuestion.getValue());
    }

    @Test
    public void load_basic_input_with_unit() {
        waistQuestion.setValue("123");
        waistQuestion.setSelectedUnit(waistQuestion.getUnits().get(0));
        saveAnswersAndLoadThem(waistQuestion);
        Assert.assertEquals("123", waistQuestion.getValue());
        Assert.assertEquals("cm", waistQuestion.getSelectedUnit().getAbbreviation());

        waistQuestion.setValue("10");
        waistQuestion.setSelectedUnit(waistQuestion.getUnits().get(1));
        saveAnswersAndLoadThem(waistQuestion);
        Assert.assertEquals("10", waistQuestion.getValue());
        Assert.assertEquals("in", waistQuestion.getSelectedUnit().getAbbreviation());
    }

    @Test
    public void load_label_with_associations() {
        int typeKeysTest[] = {123,456,789};
        int inputType = InputType.TYPE_CLASS_NUMBER;

        labaelWithAssociationsQuestion.addItem("Years","5", inputType,typeKeysTest[0]);
        labaelWithAssociationsQuestion.addItem("Months","11", inputType,typeKeysTest[1]);
        labaelWithAssociationsQuestion.addItem("Days","54", inputType,typeKeysTest[2]);
        saveAnswersAndLoadThem(labaelWithAssociationsQuestion);

        Assert.assertEquals("5", labaelWithAssociationsQuestion.getAnswerByTypeKey(typeKeysTest[0]).getValues().get(0));
        Assert.assertEquals("Years", labaelWithAssociationsQuestion.getAnswerByTypeKey(typeKeysTest[0]).getAssociatedValuesTitle().get(0));

        Assert.assertEquals("11", labaelWithAssociationsQuestion.getAnswerByTypeKey(typeKeysTest[1]).getValues().get(0));
        Assert.assertEquals("Months", labaelWithAssociationsQuestion.getAnswerByTypeKey(typeKeysTest[1]).getAssociatedValuesTitle().get(0));

        Assert.assertEquals("54", labaelWithAssociationsQuestion.getAnswerByTypeKey(typeKeysTest[2]).getValues().get(0));
        Assert.assertEquals("Days", labaelWithAssociationsQuestion.getAnswerByTypeKey(typeKeysTest[2]).getAssociatedValuesTitle().get(0));
    }

    @Test
    public void load_single_select() {
        Assert.assertFalse(singleSelectQuestion.isAnswered());

        singleSelectQuestion.clearAllCheckedItems();
        singleSelectQuestion.checkItem("Never");
        saveAnswersAndLoadThem(singleSelectQuestion);
        Assert.assertEquals(1, singleSelectQuestion.getCheckedItemCount());
        Assert.assertEquals("Never", singleSelectQuestion.getItems().get(0).value);
        Assert.assertEquals(true, singleSelectQuestion.getItems().get(0).checked);
        Assert.assertTrue(singleSelectQuestion.isAnswered());

        singleSelectQuestion.clearAllCheckedItems();
        singleSelectQuestion.checkItem("Usually");
        saveAnswersAndLoadThem(singleSelectQuestion);
        Assert.assertEquals(1, singleSelectQuestion.getCheckedItemCount());
        Assert.assertEquals("Usually", singleSelectQuestion.getItems().get(3).value);
        Assert.assertEquals(true, singleSelectQuestion.getItems().get(3).checked);
        Assert.assertTrue(singleSelectQuestion.isAnswered());
    }

    @Test
    public void load_multiple_select() {
        Assert.assertEquals(0, multiSelectQuestion.getCheckedItemCount());

        multiSelectQuestion.clearAllCheckedItems();
        multiSelectQuestion.checkItem("Asthma");
        saveAnswersAndLoadThem(multiSelectQuestion);
        Assert.assertEquals(1, multiSelectQuestion.getCheckedItemCount());
        Assert.assertEquals("Asthma", multiSelectQuestion.getItems().get(0).value);
        Assert.assertEquals(true, multiSelectQuestion.getItems().get(0).checked);

        multiSelectQuestion.clearAllCheckedItems();
        multiSelectQuestion.checkItem("Cancer");
        saveAnswersAndLoadThem(multiSelectQuestion);
        Assert.assertEquals(1, multiSelectQuestion.getCheckedItemCount());
        Assert.assertEquals("Cancer", multiSelectQuestion.getItems().get(1).value);
        Assert.assertEquals(true, multiSelectQuestion.getItems().get(1).checked);

        multiSelectQuestion.clearAllCheckedItems();
        multiSelectQuestion.checkItem("Asthma");
        multiSelectQuestion.checkItem("Cancer");
        saveAnswersAndLoadThem(multiSelectQuestion);
        Assert.assertEquals(2, multiSelectQuestion.getCheckedItemCount());
        Assert.assertEquals("Asthma", multiSelectQuestion.getItems().get(0).value);
        Assert.assertEquals(true, multiSelectQuestion.getItems().get(0).checked);
        Assert.assertEquals("Cancer", multiSelectQuestion.getItems().get(1).value);
        Assert.assertEquals(true, multiSelectQuestion.getItems().get(1).checked);

        multiSelectQuestion.clearAllCheckedItems();
        multiSelectQuestion.checkItem("1");
        saveAnswersAndLoadThem(multiSelectQuestion);
        Assert.assertEquals(1, multiSelectQuestion.getCheckedItemCount());
        Assert.assertEquals("1", multiSelectQuestion.getItems().get(2).value);
        Assert.assertEquals(true, multiSelectQuestion.getItems().get(2).checked);
    }

    @Test
    public void load_free_text() {
        freeTextQuestion.setValue("test");
        saveAnswersAndLoadThem(freeTextQuestion);
        Assert.assertEquals("test", freeTextQuestion.getValue());

        freeTextQuestion.setValue("hello");
        saveAnswersAndLoadThem(freeTextQuestion);
        Assert.assertEquals("hello", freeTextQuestion.getValue());
    }

    @Test
    public void set_isPrePopulatedValue() {
        AnswerModel answerModel = buildAnswerModel();

        Long questionId = yesNoQuestion.getIdentifier();
        HashMap<Long, Answer> answers = new HashMap<>();
        answers.put(questionId, answerModel.getAnswer());

        questionFactory.loadAnswers(questions, answers);

        Assert.assertEquals(true, yesNoQuestion.isPrePopulatedAnswer());
        Assert.assertEquals(1498082400000L, yesNoQuestion.getPrePopulatedDate());
        Assert.assertEquals(87, yesNoQuestion.getPrePopulatedSource());
    }

    @NonNull
    private AnswerModel buildAnswerModel() {
        ArrayList<PopulationValueModel> list = new ArrayList<>();
        PopulationValueModel e = new PopulationValueModel();
        e.eventDate = "2017-06-22T00:00:00.000000000+02:00";
        e.eventKey = 87;
        list.add(e);
        return new AnswerModel(list);
    }
}
