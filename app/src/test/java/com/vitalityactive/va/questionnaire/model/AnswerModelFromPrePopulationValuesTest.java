package com.vitalityactive.va.questionnaire.model;

import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.shared.questionnaire.repository.model.AnswerModel;
import com.vitalityactive.va.shared.questionnaire.repository.model.PopulationValueModel;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class AnswerModelFromPrePopulationValuesTest extends BaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initialiseAndroidThreeTen();
    }

    @Test
    public void unit_of_measure_and_value() {
        ArrayList<PopulationValueModel> list = new ArrayList<>();
        PopulationValueModel value = new PopulationValueModel();
        value.unitOfMeasure = "unitKey";
        value.value = "456";
        list.add(value);

        AnswerModel answerModel = new AnswerModel(list);

        assertEquals("unitKey", answerModel.getAnswer().getUnitOfMeasureKey());
        assertEquals(456, answerModel.getAnswer().getIntValue());
    }

    @Test
    public void from_fromValue() {
        ArrayList<PopulationValueModel> list = new ArrayList<>();
        PopulationValueModel value = new PopulationValueModel();
        value.fromValue = "456";
        list.add(value);

        AnswerModel answerModel = new AnswerModel(list);

        assertEquals(456, answerModel.getAnswer().getIntValue());
    }

    @Test
    public void with_no_event_date() {
        ArrayList<PopulationValueModel> list = new ArrayList<>();
        PopulationValueModel value = new PopulationValueModel();
        value.eventDate = null;
        list.add(value);

        AnswerModel answerModel = new AnswerModel(list);

        assertEquals(0, answerModel.getAnswer().getPrePopulationValueDate());
    }

    @Test
    public void with_event_date() {
        ArrayList<PopulationValueModel> list = new ArrayList<>();
        PopulationValueModel value = new PopulationValueModel();
        value.eventDate = "2017-06-22T00:00:00.000000000+02:00[Africa/Johannesburg]";
        list.add(value);

        AnswerModel answerModel = new AnswerModel(list);

        assertEquals(1498082400000L, answerModel.getAnswer().getPrePopulationValueDate());
    }

    @Test
    public void with_event_source() {
        ArrayList<PopulationValueModel> list = new ArrayList<>();
        PopulationValueModel value = new PopulationValueModel();
        value.eventKey = 87;
        list.add(value);

        AnswerModel answerModel = new AnswerModel(list);

        assertEquals(87, answerModel.getAnswer().getPrePopulationValueSource());
    }
}
