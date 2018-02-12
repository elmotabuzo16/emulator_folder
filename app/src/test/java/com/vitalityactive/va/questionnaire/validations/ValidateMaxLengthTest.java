package com.vitalityactive.va.questionnaire.validations;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ValidateMaxLengthTest extends ValidatorBaseTest{
    @Test
    public void greater_than_on_expected_unit() {
        name.setValue("Long value");
        assertEquals("Validation passes when longer than max length", false, validator.validate(name).isValid());

        name.setValue("short");
        assertEquals("Validation fails when shorter than max length", true, validator.validate(name).isValid());
    }
}
