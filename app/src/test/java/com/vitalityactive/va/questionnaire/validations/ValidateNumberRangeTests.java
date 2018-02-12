package com.vitalityactive.va.questionnaire.validations;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ValidateNumberRangeTests extends ValidatorBaseTest {
    @Test
    public void pass_within_range() {
        rulesToReturn.add(ValidationRuleFactory.numberGreaterThanOrEqual(1));
        rulesToReturn.add(ValidationRuleFactory.numberLessThanOrEqual(10));

        fruitPerDay.setValue("1");
        assertEquals(true, validator.validate(fruitPerDay).isValid());

        fruitPerDay.setValue("5");
        assertEquals(true, validator.validate(fruitPerDay).isValid());

        fruitPerDay.setValue("10");
        assertEquals(true, validator.validate(fruitPerDay).isValid());
    }
    @Test
    public void fail_outside_range() {
        rulesToReturn.add(ValidationRuleFactory.numberGreaterThanOrEqual(1));
        rulesToReturn.add(ValidationRuleFactory.numberLessThanOrEqual(10));

        fruitPerDay.setValue("0");
        assertEquals(false, validator.validate(fruitPerDay).isValid());

        fruitPerDay.setValue("11");
        assertEquals(false, validator.validate(fruitPerDay).isValid());
    }

    @Test
    public void fail_non_number() {
        rulesToReturn.add(ValidationRuleFactory.numberGreaterThanOrEqual(1));

        fruitPerDay.setValue("hello");
        assertEquals(false, validator.validate(fruitPerDay).isValid());
    }
}
