package com.vitalityactive.va.questionnaire.model;

import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.shared.questionnaire.repository.model.AnswerModel;
import com.vitalityactive.va.shared.questionnaire.repository.model.PopulationValueModel;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class PrePopulatedAnswerUnsetWhenUserAnswer {
    @Test
    public void isPrePopulatedValue_not_set_for_new() {
        AnswerModel answerModel = new AnswerModel(1, new Answer("value"), 123);

        assertEquals(false, answerModel.getAnswer().isPrePopulatedValue());
    }

    @Test
    public void isPrePopulatedValue_set_when_created() {
        ArrayList<PopulationValueModel> list = new ArrayList<>();
        list.add(new PopulationValueModel());

        AnswerModel answerModel = new AnswerModel(list);

        assertEquals(true, answerModel.getAnswer().isPrePopulatedValue());
    }

    @Test
    public void isPrePopulatedValue_cleared_when_ansered() {
        ArrayList<PopulationValueModel> list = new ArrayList<>();
        list.add(new PopulationValueModel());

        AnswerModel answerModel = new AnswerModel(list);

        Answer answer = answerModel.getAnswer();
        AnswerModel recreatedAnswerModel = new AnswerModel(1, answer, 123);

        assertEquals(false, recreatedAnswerModel.getAnswer().isPrePopulatedValue());
    }
}
