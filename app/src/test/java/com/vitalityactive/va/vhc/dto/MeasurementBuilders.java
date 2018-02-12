package com.vitalityactive.va.vhc.dto;

import android.support.annotation.NonNull;

import com.vitalityactive.va.vhc.captureresults.ValueLimit;

import java.util.ArrayList;

public class MeasurementBuilders {
    public static MeasurementItemField height(float heightValue) {
        ArrayList<UnitAbbreviationDescription> availableUnits = buildLongLengthUnits();
        MeasurementItemField field = new MeasurementItemField("Height", availableUnits, 1);
        field.getPrimaryMeasurementProperty().setValue(heightValue);
        return field;
    }

    @NonNull
    public static ArrayList<UnitAbbreviationDescription> buildLongLengthUnits() {
        ArrayList<UnitAbbreviationDescription> availableUnits = new ArrayList<>();
        UnitAbbreviationDescription.SubUnit feetSubUnit = new UnitAbbreviationDescription.SubUnit("Feet", new ValueLimit(2f, 9f, ""));
        UnitAbbreviationDescription.SubUnit inchesSubUnit = new UnitAbbreviationDescription.SubUnit("Inches", new ValueLimit(0f, 12f, ""));

        availableUnits.add(new UnitAbbreviationDescription("m", "Meters", new ValueLimit(0.79f, 3f, "")));
        availableUnits.add(new UnitAbbreviationDescription("cm", "Centimeters", new ValueLimit(79f, 300f, "")));
        availableUnits.add(new UnitAbbreviationDescription("ft", "Feet", new ValueLimit(2.583333333f, 9.833333333f, ""), feetSubUnit, inchesSubUnit));

        return availableUnits;
    }

    public static MeasurementItemField weight(float weightValue) {
        ArrayList<UnitAbbreviationDescription> availableUnits = buildWeightUnits();

        return new MeasurementItemField("Weight", availableUnits, weightValue);
    }

    public static ArrayList<UnitAbbreviationDescription> buildWeightUnits() {
        ArrayList<UnitAbbreviationDescription> availableUnits = new ArrayList<>();

        availableUnits.add(new UnitAbbreviationDescription("kg", "Kilograms", new ValueLimit(0f, 0f, "")));

        return availableUnits;
    }

    public static MeasurementItemField waistCircumference(float waistCircumferenceValue) {
        ArrayList<UnitAbbreviationDescription> availableUnits = buildShortLengthUnits();

        return new MeasurementItemField("Waist Circumference", availableUnits, waistCircumferenceValue);
    }

    static ArrayList<UnitAbbreviationDescription> buildShortLengthUnits() {
        ArrayList<UnitAbbreviationDescription> availableUnits = new ArrayList<>();
        availableUnits.add(new UnitAbbreviationDescription("cm", "Centimeters", new ValueLimit(0f, 30f, "")));
        availableUnits.add(new UnitAbbreviationDescription("in", "Inches", new ValueLimit(0f, 11.8f, "")));
        return availableUnits;
    }

    public static MeasurementItemField bloodPressure(float bloodPressureValue) {
        ArrayList<UnitAbbreviationDescription> availableUnits = buildPressureUnits();

        final MeasurementItemField measurementItemField = new MeasurementItemField("Blood Pressure", availableUnits);
        measurementItemField.applyUnitOfMeasureRanges(availableUnits.get(0));
        return measurementItemField;
    }

    static ArrayList<UnitAbbreviationDescription> buildPressureUnits() {
        ArrayList<UnitAbbreviationDescription> availableUnits = new ArrayList<>();

        availableUnits.add(new UnitAbbreviationDescription("mmHg", "Millimeter of mercury", new ValueLimit(0f, 10f, "")));

        return availableUnits;
    }
}
