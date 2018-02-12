package com.vitalityactive.va.vhc.capture;

import com.vitalityactive.va.vhc.captureresults.ValueLimit;
import com.vitalityactive.va.vhc.dto.MeasurementBuilders;
import com.vitalityactive.va.vhc.dto.MeasurementItemField;
import com.vitalityactive.va.vhc.dto.MeasurementProperty;
import com.vitalityactive.va.vhc.dto.UnitAbbreviationDescription;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MeasurementPropertyValidateRangeTests {
    MeasurementProperty property;

    @Before
    public void setup() {
        property = new MeasurementProperty("", null);
        property.setValidValues(new UnitAbbreviationDescription.SubUnit("test", new ValueLimit(2f, 5f, "")));
    }

    @Test
    public void null_not_valid() {
        assertFalse(property.valueIsValid(null));
    }

    @Test
    public void empty_string_not_valid() {
        assertFalse(property.valueIsValid(""));
    }

    @Test
    public void too_large() {
        assertFalse(property.valueIsValid("10"));
    }

    @Test
    public void too_small() {
        assertFalse(property.valueIsValid("1"));
    }

    @Test
    public void valid_values_can_be_updated() {
        assertFalse(property.valueIsValid("12"));

        property.setValidValues(new UnitAbbreviationDescription.SubUnit("test", new ValueLimit(2f, 12f, "")));

        assertTrue(property.valueIsValid("12"));
    }

    @Test
    public void range_based_on_unit_of_measurement() {
        MeasurementItemField field = MeasurementBuilders.waistCircumference(0);
        List<UnitAbbreviationDescription> units = field.getUnits();

        // 25 in is allowed
        field.setUnitOfMeasurement(units.get(1));
        assertFalse(field.getPrimaryMeasurementProperty().valueIsValid("25"));

        // 25 cm is not allowed
        field.setUnitOfMeasurement(units.get(0));
        assertTrue(field.getPrimaryMeasurementProperty().valueIsValid("25"));
    }
}
