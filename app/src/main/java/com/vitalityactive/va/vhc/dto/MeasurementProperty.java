package com.vitalityactive.va.vhc.dto;

import android.text.TextUtils;

public class MeasurementProperty {
    private UnitAbbreviationDescription.SubUnit unit;
    private Float value;
    private final boolean isPrimaryProperty;
    private String name = "";
    private boolean visible = true;

    public MeasurementProperty(String title, Float value) {
        this(title, value, true);
    }

    public MeasurementProperty(String title, Float value, boolean isPrimaryProperty) {
        this.name = title;
        this.value = value;
        this.isPrimaryProperty = isPrimaryProperty;
    }

    public static MeasurementProperty hiddenSecondaryProperty() {
        MeasurementProperty property = new MeasurementProperty("", null, false);
        property.visible = false;
        return property;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public void setValidValues(UnitAbbreviationDescription.SubUnit unit) {
        this.unit = unit;
    }

    public boolean valueIsValid(String value) {
        if (TextUtils.isEmpty(value))
            return false;

        float floatValue;
        try {
            floatValue = Float.parseFloat(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        return isValid(floatValue);
    }

    public boolean isValid() {
        return this.value != null && isValid(this.value);
    }

    private boolean isValid(float value) {
        return unit.isValid(value);
    }

    public boolean isPrimaryProperty() {
        return isPrimaryProperty;
    }

    public String getRangeDetails() {
        return unit.getRangeDetails();
    }
}
