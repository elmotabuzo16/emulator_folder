package com.vitalityactive.va.questionnaire.validations;

import android.text.InputType;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ValidatorTest extends ValidatorBaseTest {
    @Test
    public void loads_rules_from_repository() {
        fruitPerDay.setValue("12");
        validator.validate(fruitPerDay);

        verify(repository, times(1)).getQuestionValidationRules(fruitPerDay.getIdentifier());
    }

    @Test
    public void validate_label_with_associations() {
        int typeKeysTest[] = {123,456,789};
        int inputType = InputType.TYPE_CLASS_NUMBER;

        smokingData.addItem("Years","5", inputType,typeKeysTest[0]);
        smokingData.addItem("Months","11", inputType,typeKeysTest[1]);
        smokingData.addItem("Days","54", inputType,typeKeysTest[2]);

        validator.validate(smokingData);

        verify(repository, times(1)).getQuestionValidationRules(smokingData.getIdentifier());
    }

    @Test
    public void unanswered_questions_invalid() {
        assertEquals(false, fruitPerDay.isAnswered());
        boolean isValid = validator.validate(fruitPerDay).isValid();

        assertEquals(false, isValid);
    }

    @Test
    public void all_must_pass() {
        fruitPerDay.setValue("1");
        assertEquals("expected to pass with no rules", true, validator.validate(fruitPerDay).isValid());

        rulesToReturn.add(ValidationRuleFactory.anything());
        assertEquals(true, validator.validate(fruitPerDay).isValid());

        rulesToReturn.add(ValidationRuleFactory.nothing("test"));
        assertEquals(false, validator.validate(fruitPerDay).isValid());

        rulesToReturn.clear();
        rulesToReturn.add(ValidationRuleFactory.nothing("test"));
        assertEquals(false, validator.validate(fruitPerDay).isValid());
    }
}
