package com.vitalityactive.va.vhc.capture;

import com.vitalityactive.va.vhc.HealthAttributeGroup;
import com.vitalityactive.va.vhc.dto.MeasurementBuilders;
import com.vitalityactive.va.vhc.dto.MeasurementItem;
import com.vitalityactive.va.vhc.dto.MeasurementItemField;
import com.vitalityactive.va.vhc.dto.UnitAbbreviationDescription;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MeasurementItemFieldTests {
    MeasurementItem waist = buildWaistCircumference();
    MeasurementItem bloodPressure = buildBloodPressure();
    MeasurementItem bmi = buildBodyMassIndex();

    @Test
    public void field_valid_only_if_both_properties_valid() {
        MeasurementItemField field = bloodPressure.measurementItemFields.get(0);

        field.getSecondaryMeasurementProperty().setValue(-1f);
        field.getPrimaryMeasurementProperty().setValue(-1f);
        assertFalse(field.getPrimaryMeasurementProperty().isValid());

        field.getPrimaryMeasurementProperty().setValue(1f);
        assertTrue(field.getPrimaryMeasurementProperty().isValid());
        assertFalse(field.getPrimaryMeasurementProperty().isValid());

        assertTrue(field.getPrimaryMeasurementProperty().isValid());
        field.getSecondaryMeasurementProperty().setValue(1f);
        assertTrue(field.getPrimaryMeasurementProperty().isValid());
    }

    @Test
    public void field_valid_if_only_property_is_valid() {
        MeasurementItemField field = waist.measurementItemFields.get(0);

        field.getPrimaryMeasurementProperty().setValue(-1f);
        assertFalse(field.getPrimaryMeasurementProperty().isValid());

        field.getPrimaryMeasurementProperty().setValue(20f);
        assertTrue(field.getPrimaryMeasurementProperty().isValid());
        assertTrue(field.getPrimaryMeasurementProperty().isValid());
    }

    @Test
    public void field_valid_if_total_value_correct_on_combined_properties() {
        MeasurementItemField height = bmi.measurementItemFields.get(0);

        // set to feet and inches
        UnitAbbreviationDescription feetAndInches = height.getUnits().get(2);
        assertTrue(feetAndInches.isASpecialMultiValuedUnit());
        height.setUnitOfMeasurement(feetAndInches);

        // set to max: valid
        float max = feetAndInches.getMaxValue();
        height.getPrimaryMeasurementProperty().setValue((float) Math.floor(max));
        height.getSecondaryMeasurementProperty().setValue((max - (int) max) * 12);
        assertTrue(height.getPrimaryMeasurementProperty().isValid());

        // set 1 inch higher than max: invalid
        height.getSecondaryMeasurementProperty().setValue((max - (int) max) * 12 + 1);
        assertFalse(height.getPrimaryMeasurementProperty().isValid());
    }

    public static MeasurementItem buildBodyMassIndex() {
        List<MeasurementItemField> measurementItemFields = new ArrayList<>();
        measurementItemFields.add(MeasurementBuilders.height(0));
        measurementItemFields.add(MeasurementBuilders.weight(0));
        return MeasurementItem.Builder.bodyMassIndex(new HealthAttributeGroup(), measurementItemFields);
    }

    public static MeasurementItem buildWaistCircumference() {
        List<MeasurementItemField> measurementItemFields = new ArrayList<>();
        measurementItemFields.add(MeasurementBuilders.waistCircumference(-1));
        return MeasurementItem.Builder.waistCircumference(new HealthAttributeGroup(), measurementItemFields);
    }

    public static MeasurementItem buildBloodPressure() {
        List<MeasurementItemField> measurementItemFields = new ArrayList<>();
        measurementItemFields.add(MeasurementBuilders.bloodPressure(-1));
        return MeasurementItem.Builder.bloodPressure(new HealthAttributeGroup(), measurementItemFields);
    }
}
