package com.vitalityactive.va.questionnaire.types.setAnswers;

import com.vitalityactive.va.questionnaire.types.YesNoQuestionDto;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class YesNoQuestionDtoTests {
    private YesNoQuestionDto dto;

    @Before
    public void setUp() {
        dto = new YesNoQuestionDto(0, 0, 0, "", "", "", 0, "dawn", "dusk", "Yes", "No");
    }

    @Test
    public void default_unanswered() {
        assertFalse(dto.isAnswered());
    }

    @Test
    public void set_answer_marks_as_answered() {
        dto.setValue(true);
        assertTrue(dto.isAnswered());
    }

    @Test
    public void can_get_answer_that_was_set() {
        dto.setValue(true);
        assertTrue(dto.getValue());

        dto.setValue(false);
        assertFalse(dto.getValue());
    }
}
