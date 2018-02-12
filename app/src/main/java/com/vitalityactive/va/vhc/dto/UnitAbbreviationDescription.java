package com.vitalityactive.va.vhc.dto;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.vitalityactive.va.UnitsOfMeasure;
import com.vitalityactive.va.vhc.captureresults.ValueLimit;

public class UnitAbbreviationDescription {
    private final Float minValue;
    private final Float maxValue;
    private final String rangeDetails;
    private UnitsOfMeasure unitOfMeasure = UnitsOfMeasure.UNKNOWN;
    private SubUnit unit1;
    private SubUnit unit2;
    private String abbreviation;
    private String display;

    public UnitAbbreviationDescription(String abbreviation, String display, ValueLimit valueLimit, SubUnit unit1, SubUnit unit2) {
        this.abbreviation = abbreviation;
        this.display = display;
        this.minValue = valueLimit.getMinValue();
        this.maxValue = valueLimit.getMaxValue();
        this.rangeDetails = valueLimit.getRangeDetails();
        this.unit1 = (unit1 == null) ? new SubUnit(null, valueLimit) : unit1;
        this.unit2 = (unit2 == null) ? new SubUnit(null, valueLimit) : unit2;
    }

    public UnitAbbreviationDescription(final UnitsOfMeasure unit, String abbreviation, String display, ValueLimit valueLimit, final SubUnit unit1, final SubUnit unit2) {
        this(abbreviation, display, valueLimit, unit1, unit2);
        this.unitOfMeasure = unit;
    }

    public UnitAbbreviationDescription(String abbreviation,
                                       String display,
                                       ValueLimit valueLimit) {
        this(abbreviation, display, valueLimit,
                null,
                null);
    }

    public UnitAbbreviationDescription(UnitsOfMeasure unit,
                                       String unitAbbreviation,
                                       String unitDisplayName,
                                       ValueLimit valueLimit) {
        this(unitAbbreviation, unitDisplayName, valueLimit);
        this.unitOfMeasure = unit;
    }

    public void setUnitOfMeasure(UnitsOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    @Override
    public String toString() {
        return display;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public Float getMinValue() {
        return minValue;
    }

    public Float getMaxValue() {
        return maxValue;
    }

    public SubUnit getUnit1() {
        return unit1;
    }

    public SubUnit getUnit2() {
        return unit2;
    }

    public boolean isASpecialMultiValuedUnit() {
        return !TextUtils.isEmpty(this.unit1.name) && !TextUtils.isEmpty(this.unit2.name);
    }

    public float calculateTotalFromSubUnits(float unit1, float unit2, float maxRange) {
        return unit1 + (unit2 / maxRange + 1);
    }

    public UnitsOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnit2(UnitAbbreviationDescription unit) {
        this.unit2 = new SubUnit(unit.display, new ValueLimit(unit.getMinValue(), unit.getMaxValue(), unit.getRangeDetails()));
    }

    public void setUnit1(UnitAbbreviationDescription unit) {
        this.unit1 = new SubUnit(unit.display, new ValueLimit(unit.getMinValue(), unit.getMaxValue(), unit.getRangeDetails()));
    }

    public String getRangeDetails() {
        return rangeDetails;
    }

    public static class SubUnit {
        public final String name;
        public final Float min;
        public final Float max;
        private final String rangeDetails;

        public SubUnit(String name, @NonNull ValueLimit valueLimit) {
            this.name = name;
            this.min = valueLimit.getMinValue();
            this.max = valueLimit.getMaxValue();
            this.rangeDetails = valueLimit.getRangeDetails();
        }

        public final String getRangeDetails() {
            return rangeDetails;
        }

        public boolean isValid(float value) {
            if (min == null) {
                return value <= max;
            }

            if (max == null) {
                return value >= min;
            }

            return value >= min && value <= max;
        }
    }
}
