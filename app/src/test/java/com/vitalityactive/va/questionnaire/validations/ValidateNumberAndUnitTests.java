package com.vitalityactive.va.questionnaire.validations;

import com.vitalityactive.va.constants.UnitOfMeasure;
import com.vitalityactive.va.vhc.dto.UnitAbbreviationDescription;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ValidateNumberAndUnitTests extends ValidatorBaseTest {
    @Test
    public void greater_than_on_expected_unit() {
        UnitAbbreviationDescription unit1 = waist.getUnits().get(0);
        waist.setSelectedUnit(unit1);

        // >= 10 unit1
        rulesToReturn.add(ValidationRuleFactory.numberGreaterThanOrEqual(10, unit1.getUnitOfMeasure().getTypeKey()));

        waist.setValue("10");
        assertEquals("passes when >= expected unit's lower value", true, validator.validate(waist).isValid());

        waist.setValue("9");
        assertEquals("fails when < expected unit's lower value", false, validator.validate(waist).isValid());
    }

    @Test
    public void always_pass_on_different_unit() {
        UnitAbbreviationDescription unit1 = waist.getUnits().get(0);
        UnitAbbreviationDescription unit2 = waist.getUnits().get(1);
        waist.setSelectedUnit(unit2);           // 2 selected

        // >= 10 unit1, nothing for unit2
        rulesToReturn.add(ValidationRuleFactory.numberGreaterThanOrEqual(10, unit1.getUnitOfMeasure().getTypeKey()));

        waist.setValue("10");
        assertEquals("should always pass", true, validator.validate(waist).isValid());

        waist.setValue("9");
        assertEquals("should always pass", true, validator.validate(waist).isValid());
    }

    @Test
    public void number_pass_based_on_selected_unit() {
        UnitAbbreviationDescription unit1 = waist.getUnits().get(0);
        UnitAbbreviationDescription unit2 = waist.getUnits().get(1);

        // >= 10 unit1, >= 8 for unit2
        rulesToReturn.add(ValidationRuleFactory.numberGreaterThanOrEqual(10, unit1.getUnitOfMeasure().getTypeKey()));
        rulesToReturn.add(ValidationRuleFactory.numberGreaterThanOrEqual(8, unit2.getUnitOfMeasure().getTypeKey()));

        // pass when 11 unit1
        waist.setValue("11");
        waist.setSelectedUnit(unit1);
        assertEquals(true, validator.validate(waist).isValid());

        // fail for 9 unit1
        waist.setValue("9");
        waist.setSelectedUnit(unit1);
        assertEquals(false, validator.validate(waist).isValid());

        // pass for 9 unit2
        waist.setValue("9");
        waist.setSelectedUnit(unit2);
        assertEquals(true, validator.validate(waist).isValid());

        // fail for 7 unit2
        waist.setValue("7");
        waist.setSelectedUnit(unit2);
        assertEquals(false, validator.validate(waist).isValid());
    }

    @Test
    public void pass_if_single_unit() {
        rulesToReturn.add(ValidationRuleFactory.numberLessThanOrEqual(10, String.valueOf(UnitOfMeasure._CENTIMETER)));

        fruitPerDay.setValue("10");
        assertEquals(true, validator.validate(fruitPerDay).isValid());
    }
}
