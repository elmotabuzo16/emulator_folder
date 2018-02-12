package com.vitalityactive.va.vhc.capture;

import com.vitalityactive.va.vhc.captureresults.models.GroupType;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CaptureResultsPresenterCaptureTests extends CaptureResultsPresenterBase {
    private GroupType groupType = GroupType.UNKNOWN;

    @Test
    public void failed_update_ui() {
        when(property.valueIsValid(anyString())).thenReturn(false);

        captureResultsPresenter.updateDataItem(groupType, itemField, property, "1", viewHolderUI);

        verify(viewHolderUI, never()).onValidationPassed();
        verify(viewHolderUI, times(1)).onValidationFailed(anyString());
    }

    @Test
    public void empty_string_update_ui() {
        when(property.valueIsValid(anyString())).thenReturn(false);

        captureResultsPresenter.updateDataItem(groupType, itemField, property, "", viewHolderUI);

        verify(viewHolderUI, times(1)).onValidationPassed();
        verify(viewHolderUI, never()).onValidationFailed(anyString());
    }

    @Test
    public void passes_string_update_ui() {
        when(property.valueIsValid(anyString())).thenReturn(true);

        captureResultsPresenter.updateDataItem(groupType, itemField, property, "1", viewHolderUI);

        verify(viewHolderUI, times(1)).onValidationPassed();
        verify(viewHolderUI, never()).onValidationFailed(anyString());
    }

    @Test
    public void passes_string_persists() {
        when(property.valueIsValid(anyString())).thenReturn(true);

        captureResultsPresenter.updateDataItem(groupType, itemField, property, "1", viewHolderUI);

        verify(repository, times(1)).persistCapturedData(eq(groupType), eq(itemField), eq(property), eq("1"), eq(true));
    }

    @Test
    public void unit_of_measure_changes_persisted() {
        when(itemField.getSelectedUnitOfMeasurement()).thenReturn(unitAbbreviationDescription);

        captureResultsPresenter.onUnitOfMeasureChanged(groupType, itemField, unitAbbreviationDescription);

        verify(repository, times(1)).persistUnitOfMeasure(eq(groupType), eq(itemField), eq(unitAbbreviationDescription));
    }

    @Test
    public void date_captured_changes_persisted() {
        Date date = Calendar.getInstance().getTime();
        captureResultsPresenter.setDateCaptured(groupType, itemField, date);

        verify(repository, times(1)).persistDateCaptured(eq(groupType), eq(itemField), eq(date));
    }
}
