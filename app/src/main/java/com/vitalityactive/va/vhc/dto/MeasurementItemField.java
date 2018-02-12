package com.vitalityactive.va.vhc.dto;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.vitalityactive.va.UnitsOfMeasure;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MeasurementItemField {
    private String fieldKey;
    private String title;
    private String subtitle;
    private MeasurementProperty primaryMeasurementProperty;
    private MeasurementProperty secondaryMeasurementProperty;
    private List<UnitAbbreviationDescription> units;
    private Date dateTested;
    private int selectedUnitOfMeasurementIndex;
    private String selectedItem;
    private List<SelectableItem> selectableItems = new ArrayList<>();

    MeasurementItemField(String title, List<UnitAbbreviationDescription> units) {
        this.title = title;
        this.units = units;
        this.selectedUnitOfMeasurementIndex = 0;
        this.dateTested = Calendar.getInstance().getTime();
    }

    public MeasurementItemField(String fieldKey, String title, ArrayList<UnitAbbreviationDescription> units, List<SelectableItem> selectableItems) {
        this(title, units);
        this.fieldKey = fieldKey;
        this.selectableItems.addAll(selectableItems);
    }

    public MeasurementItemField(String title, List<UnitAbbreviationDescription> units, float value) {
        this(title, units);
        this.primaryMeasurementProperty = new MeasurementProperty(title, value);
        this.secondaryMeasurementProperty = MeasurementProperty.hiddenSecondaryProperty();
        applyUnitOfMeasureRanges(units.get(0));
    }

    public MeasurementItemField(String title, ArrayList<UnitAbbreviationDescription> units) {
        this(title, units, 0);
    }

    public String getTitle() {
        return title;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public List<UnitAbbreviationDescription> getUnits() {
        return units;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public MeasurementProperty getSecondaryMeasurementProperty() {
        return secondaryMeasurementProperty;
    }

    public MeasurementProperty getPrimaryMeasurementProperty() {
        return primaryMeasurementProperty;
    }

    public Date getDateTested() {
        return dateTested;
    }

    public void setDateTested(long dateTested) {
        this.dateTested = new Date(dateTested);
    }

    public boolean setUnitOfMeasurement(UnitAbbreviationDescription unitOfMeasurement) {
        int newIndex = this.units.indexOf(unitOfMeasurement);
        if (newIndex == selectedUnitOfMeasurementIndex)
            return false;
        this.selectedUnitOfMeasurementIndex = newIndex;
        applyUnitOfMeasureRanges(unitOfMeasurement);
        return true;
    }

    void applyUnitOfMeasureRanges(UnitAbbreviationDescription unitOfMeasurement) {
        this.primaryMeasurementProperty.setValidValues(unitOfMeasurement.getUnit1());
        this.secondaryMeasurementProperty.setValidValues(unitOfMeasurement.getUnit2());

        boolean isASpecialMultiValuedUnit = unitOfMeasurement.isASpecialMultiValuedUnit();
        secondaryMeasurementProperty.setVisible(isASpecialMultiValuedUnit);
    }

    public int getSelectedUnitOfMeasurementIndex() {
        return selectedUnitOfMeasurementIndex;
    }

    public UnitAbbreviationDescription getSelectedUnitOfMeasurement() {
        return units.get(selectedUnitOfMeasurementIndex);
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public List<SelectableItem> getSelectableItems() {
        return selectableItems;
    }

    public static class SelectableItem {
        public final String title;
        public final String description;
        public final String value;

        public SelectableItem(String title, String description, String value) {
            this.title = title;
            this.description = description;
            this.value = value;
        }
    }

    public static class Builder {
        private final String fieldKey;
        private final String name;
        private String primaryPropertyName;
        private String secondaryPropertyName;
        private final ArrayList<UnitAbbreviationDescription> units = new ArrayList<>();
        private Float primaryValue;
        private Float secondaryValue;
        private UnitsOfMeasure selectedUnit;
        private String unitSymbol;
        private long dateTested;
        private String subtitle;
        private String selectedItem;
        private final List<SelectableItem> selectableItems = new ArrayList<>();

        public Builder(String key, String name) {
            this.fieldKey = key;
            this.name = name;
            this.primaryPropertyName = name;
        }

        public Builder addUnit(UnitAbbreviationDescription unit) {
            units.add(unit);
            return this;
        }

        public Builder addSecondaryUnit(UnitAbbreviationDescription unit) {
            for (UnitAbbreviationDescription primaryUnit : units) {
                //// TODO: 2017/07/24 Find another way to compare units
                if (Objects.equals(primaryUnit.getAbbreviation(), unit.getAbbreviation())) {
                    primaryUnit.setUnit2(unit);
                }
            }
            return this;
        }

        @NonNull
        public Builder addSelectableItem(String title, String description, String value) {
            return addSelectableItem(new SelectableItem(title, description, value));
        }

        @NonNull
        public Builder addSelectableItem(SelectableItem item) {
            selectableItems.add(item);
            return this;
        }

        public Builder setSelectedUnit(UnitsOfMeasure selectedUnit, String unitAbbreviation) {
            this.selectedUnit = selectedUnit;
            this.unitSymbol = unitAbbreviation;
            return this;
        }

        public Builder setValues(Float primaryValue, Float secondaryValue) {
            this.primaryValue = primaryValue;
            this.secondaryValue = secondaryValue;
            return this;
        }

        public Builder setSelectedItem(String selectedItem) {
            this.selectedItem = selectedItem;
            return this;
        }

        public void setPropertyNames(String primaryPropertyName, String secondaryPropertyName) {
            this.primaryPropertyName = primaryPropertyName;
            this.secondaryPropertyName = secondaryPropertyName;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public Builder setDateTested(long dateTested) {
            this.dateTested = dateTested;
            return this;
        }


        public boolean isFieldMultiValuedUnit() {
            return !TextUtils.isEmpty(this.primaryPropertyName) && !TextUtils.isEmpty(this.secondaryPropertyName);
        }


        public MeasurementItemField build() {
            MeasurementItemField field = new MeasurementItemField(fieldKey, name, units, selectableItems);
            field.subtitle = subtitle;


            boolean isFieldMultiValued = isFieldMultiValuedUnit();
            if ((!units.isEmpty() && units.get(0).isASpecialMultiValuedUnit()) || isFieldMultiValued) {
                field.primaryMeasurementProperty = new MeasurementProperty(this.primaryPropertyName , primaryValue);
                field.secondaryMeasurementProperty = new MeasurementProperty("", secondaryValue, false);
            } else {
                field.primaryMeasurementProperty = new MeasurementProperty(primaryPropertyName, primaryValue);
                if (TextUtils.isEmpty(secondaryPropertyName)) {
                    field.secondaryMeasurementProperty = MeasurementProperty.hiddenSecondaryProperty();
                } else {
                    field.secondaryMeasurementProperty = new MeasurementProperty(secondaryPropertyName, secondaryValue, false);
                }
            }

            UnitAbbreviationDescription unit = getUnitOfMeasurementForSelectedUnit();
            field.setUnitOfMeasurement(unit);
            field.applyUnitOfMeasureRanges(unit);
            field.setDateTested(dateTested);
            field.setSelectedItem(selectedItem);
            return field;
        }

        private UnitAbbreviationDescription getUnitOfMeasurementForSelectedUnit() {
            if (selectedUnit == null && !units.isEmpty()) {
                return units.get(0);
            }

            for (UnitAbbreviationDescription unit : units) {
                if (unitSymbol.equals(unit.getAbbreviation()))
                    return unit;
            }

            // TODO: do something else here
            return units.isEmpty() ? new UnitAbbreviationDescription("missing", "missing", null) : units.get(0);
        }
    }
}
