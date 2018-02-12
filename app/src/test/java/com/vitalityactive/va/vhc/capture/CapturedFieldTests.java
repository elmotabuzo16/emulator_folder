package com.vitalityactive.va.vhc.capture;

import com.vitalityactive.va.UnitsOfMeasure;
import com.vitalityactive.va.vhc.captureresults.models.CapturedField;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class CapturedFieldTests {
    @Test
    public void valid_when_all_fields_are_valid() {
        CapturedField field = new CapturedField("field");
        assertFalse(field.isFieldCaptureComplete());

        field.setPrimaryValue(0f, true);
        assertFalse(field.isFieldCaptureComplete());

        field.setSelectedUnitOfMeasure(UnitsOfMeasure.CENTIMETER);
        assertFalse(field.isFieldCaptureComplete());

        field.setDateTested(123);
        assertTrue(field.isFieldCaptureComplete());
    }

    @Test
    public void valid_when_all_fields_are_valid_secondary_defaults_to_valid() {
        CapturedField field = new CapturedField("field");
        assertFalse(field.isFieldCaptureComplete());

        field.setPrimaryValue(0f, true);
        assertFalse(field.isFieldCaptureComplete());

        field.setSelectedUnitOfMeasure(UnitsOfMeasure.CENTIMETER);
        assertFalse(field.isFieldCaptureComplete());

        field.setDateTested(123);
        assertTrue(field.isFieldCaptureComplete());

        field.setSecondaryValue(0f, false);
        assertFalse(field.isFieldCaptureComplete());
    }

    @Test
    public void valid_when_selected_item_set() {
        CapturedField field = new CapturedField("field");
        assertFalse(field.isFieldCaptureComplete());

        field.setSelectedItem("selected");
        assertFalse(field.isFieldCaptureComplete());

        field.setDateTested(123);
        assertTrue(field.isFieldCaptureComplete());
    }

    @Test
    public void submission_value_for_value_fields() {
        CapturedField field = new CapturedField("field");
        field.setPrimaryValue(10f, true);

        assertEquals("10.0", field.getValueForSubmission());
    }

    @Test
    public void submission_value_for_selected_fields() {
        CapturedField field = new CapturedField("field");
        field.setSelectedItem("one");

        assertEquals("one", field.getValueForSubmission());
    }

    @Test
    public void type_for_value_fields_is_values() {
        CapturedField field = new CapturedField("field");
        field.setPrimaryValue(10f, true);

        assertEquals(CapturedField.CaptureType.VALUE, field.getTypeOfCapture());
    }

    @Test
    public void type_for_value_fields_is_selection() {
        CapturedField field = new CapturedField("field");
        field.setSelectedItem("one");

        assertEquals(CapturedField.CaptureType.SELECTION, field.getTypeOfCapture());
    }

    @Test
    public void is_primary_value_captured() {
        CapturedField field = new CapturedField("field");
        assertFalse(field.isPrimaryValueCaptured());

        field.setPrimaryValue(10f, true);
        assertTrue(field.isPrimaryValueCaptured());
    }

    @Test
    public void is_secondary_value_captured() {
        CapturedField field = new CapturedField("field");
        assertFalse(field.isSecondaryValueCaptured());

        field.setSecondaryValue(10f, true);
        assertTrue(field.isSecondaryValueCaptured());
    }

    @Test
    public void is_selection_captured() {
        CapturedField field = new CapturedField("field");
        assertFalse(field.isSelectionCaptured());

        field.setSelectedItem("one");
        assertTrue(field.isSelectionCaptured());
    }
}
