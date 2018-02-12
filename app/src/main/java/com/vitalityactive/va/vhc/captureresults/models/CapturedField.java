package com.vitalityactive.va.vhc.captureresults.models;

import android.text.TextUtils;

import com.vitalityactive.va.UnitsOfMeasure;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class CapturedField extends RealmObject implements Model {
    public String key;
    private Float primaryValue;
    private Float secondaryValue;
    private String selectedItem;
    private String selectedUnitOfMeasureKey;
    private long dateTested;
    private boolean primaryValueValid;
    private boolean secondaryValueValid = true;
    private boolean fieldCaptureComplete = false;

    public CapturedField() {
    }

    public CapturedField(String key) {
        this.key = key;
    }

    public String getValueForSubmission() {
        if (getTypeOfCapture() == CaptureType.SELECTION)
            return selectedItem;

        String valuePrimary = primaryValue.toString();

        if (!TextUtils.isEmpty(selectedUnitOfMeasureKey) && selectedUnitOfMeasureKey.equals(UnitsOfMeasure.FOOTINCH.getTypeKey())) {
            valuePrimary = getPrimaryValue().intValue() + " F " + getSecondaryValue() + " INCH";
        }

        if (!TextUtils.isEmpty(selectedUnitOfMeasureKey) && selectedUnitOfMeasureKey.equals(UnitsOfMeasure.STONEPOUND.getTypeKey())) {
            valuePrimary = getPrimaryValue().intValue() + " ST " + getSecondaryValue() + " LB";
        }

        return valuePrimary;
    }

    public String getValueForDisplay() {
        if (getTypeOfCapture() == CaptureType.SELECTION)
            return selectedItem;
        return primaryValue.toString();
    }

    public Float getPrimaryValue() {
        return primaryValue;
    }

    public CapturedField setPrimaryValue(Float primaryValue, boolean valueIsValid) {
        this.primaryValue = primaryValue;
        this.primaryValueValid = valueIsValid;
        this.selectedItem = null;
        updateFieldCaptureComplete();
        return this;
    }

    public Float getSecondaryValue() {
        return secondaryValue;
    }

    public CapturedField setSecondaryValue(Float secondaryValue, boolean valueIsValid) {
        this.secondaryValue = secondaryValue;
        this.secondaryValueValid = valueIsValid;
        this.selectedItem = null;
        updateFieldCaptureComplete();
        return this;
    }

    public UnitsOfMeasure getSelectedUnitOfMeasure() {
        return UnitsOfMeasure.fromValue(selectedUnitOfMeasureKey);
    }

    public CapturedField setSelectedUnitOfMeasure(UnitsOfMeasure selectedUnitOfMeasure) {
        this.selectedUnitOfMeasureKey = selectedUnitOfMeasure.getTypeKey();
        updateFieldCaptureComplete();
        return this;
    }

    public CapturedField setDateTested(long dateTested) {
        this.dateTested = dateTested;
        updateFieldCaptureComplete();
        return this;
    }

    public long getDateTested() {
        return dateTested;
    }

    public boolean isFieldCaptureComplete() {
        return fieldCaptureComplete;
    }

    private void updateFieldCaptureComplete() {
        boolean unitOfMeasureValid = !TextUtils.isEmpty(selectedUnitOfMeasureKey);
        boolean dateTestedValid = dateTested > 0;
        if (getTypeOfCapture() == CaptureType.SELECTION) {
            fieldCaptureComplete = dateTestedValid;
        } else {
            fieldCaptureComplete = primaryValueValid && (secondaryValueValid || secondaryValue == null) && unitOfMeasureValid && dateTestedValid;
        }
    }

    public String getKey() {
        return key;
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
        this.primaryValue = null;
        this.primaryValueValid = true;
        this.secondaryValue = null;
        this.secondaryValueValid = true;
        updateFieldCaptureComplete();
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public CaptureType getTypeOfCapture() {
        return selectedItem != null ? CaptureType.SELECTION : CaptureType.VALUE;
    }

    public boolean isPrimaryValueCaptured() {
        return getTypeOfCapture() == CaptureType.VALUE && primaryValue != null;
    }

    public boolean isSecondaryValueCaptured() {
        return getTypeOfCapture() == CaptureType.VALUE && secondaryValue != null;
    }

    public boolean isSelectionCaptured() {
        return getTypeOfCapture() == CaptureType.SELECTION;
    }

    public enum CaptureType {
        SELECTION, VALUE
    }
}
