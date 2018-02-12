package com.vitalityactive.va.questionnaire.types.loadAnswers;

import org.junit.Assert;
import org.junit.Test;

public class YesNoTests extends BaseLoadAnswer {
    @Test
    public void load() {
        Assert.assertFalse(yesNoQuestion.isAnswered());

        yesNoQuestion.setValue(true);
        saveAnswersAndLoadThem(yesNoQuestion);
        Assert.assertEquals(true, yesNoQuestion.getValue());
        Assert.assertTrue(yesNoQuestion.isAnswered());

        yesNoQuestion.setValue(false);
        saveAnswersAndLoadThem(yesNoQuestion);
        Assert.assertEquals(false, yesNoQuestion.getValue());
        Assert.assertTrue(yesNoQuestion.isAnswered());
    }

    @Test
    public void unanswered_if_invalid_value() {
        saveAnswerStringAndLoadThem(yesNoQuestion, "TRUE");

        Assert.assertFalse(yesNoQuestion.isAnswered());
    }
}
