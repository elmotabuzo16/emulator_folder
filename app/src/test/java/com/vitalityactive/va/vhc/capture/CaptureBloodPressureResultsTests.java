package com.vitalityactive.va.vhc.capture;

import com.vitalityactive.va.vhc.captureresults.models.CapturedGroup;
import com.vitalityactive.va.vhc.captureresults.models.GroupType;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CaptureBloodPressureResultsTests extends CaptureResultsPresenterBase {
    @Before
    public void setup() {
        super.setup();

        // assuming all values are valid
        when(property.valueIsValid(anyString())).thenReturn(true);
        when(secondaryProperty.valueIsValid(anyString())).thenReturn(true);

        // another field is captured
        when(repository.getCapturedGroupsWithOneOrMoreCompletelyCapturedFields()).thenReturn(Collections.singletonList(new CapturedGroup()));

        when(mockMeasurementPersister.isBloodPressure(itemField)).thenReturn(true);
    }

    @Test
    public void blood_pressure_both_must_be_captured() {
        // neither captured: can continue
        captureResultsPresenter.updateDataItem(GroupType.BLOOD_PRESSURE, itemField, property, "", viewHolderUI);
        verify(userInterface, times(1)).canSubmit(true);

        // then diastolic captured: cannot continue
        captureResultsPresenter.updateDataItem(GroupType.BLOOD_PRESSURE, itemField, property, "1", viewHolderUI);
        verify(userInterface, times(1)).canSubmit(false);

        // then systolic captured: can continue
        captureResultsPresenter.updateDataItem(GroupType.BLOOD_PRESSURE, itemField, secondaryProperty, "1", viewHolderUI);
        verify(userInterface, times(2)).canSubmit(true);

        // then diastolic cleared: cannot continue
        captureResultsPresenter.updateDataItem(GroupType.BLOOD_PRESSURE, itemField, property, "", viewHolderUI);
        verify(userInterface, times(2)).canSubmit(false);

        // then systolic cleared: can continue
        captureResultsPresenter.updateDataItem(GroupType.BLOOD_PRESSURE, itemField, secondaryProperty, "", viewHolderUI);
        verify(userInterface, times(3)).canSubmit(true);
    }

    @Test
    public void diastolic_captured_show_systolic_required() {
        captureResultsPresenter.updateDataItem(GroupType.BLOOD_PRESSURE, itemField, property, "1", viewHolderUI);
        verify(secondaryViewHolderUI, times(1)).onValidationFailed("Required");
    }

    @Test
    public void systolic_captured_show_diastolic_required() {
        captureResultsPresenter.updateDataItem(GroupType.BLOOD_PRESSURE, itemField, secondaryProperty, "1", secondaryViewHolderUI);
        verify(viewHolderUI, times(1)).onValidationFailed("Required");
    }

    @Test
    public void both_entered_diastolic_cleared_so_required_on_it() {
        // both captured
        captureResultsPresenter.updateDataItem(GroupType.BLOOD_PRESSURE, itemField, property, "1", viewHolderUI);
        captureResultsPresenter.updateDataItem(GroupType.BLOOD_PRESSURE, itemField, secondaryProperty, "1", secondaryViewHolderUI);

        // then cleared
        captureResultsPresenter.updateDataItem(GroupType.BLOOD_PRESSURE, itemField, property, "", viewHolderUI);

        // show validation errors
        verify(viewHolderUI, times(1)).onValidationFailed("Required");
    }

    @Test
    public void both_entered_systolic_cleared_so_required_on_it() {
        // both captured
        captureResultsPresenter.updateDataItem(GroupType.BLOOD_PRESSURE, itemField, secondaryProperty, "1", secondaryViewHolderUI);
        captureResultsPresenter.updateDataItem(GroupType.BLOOD_PRESSURE, itemField, property, "1", viewHolderUI);

        // then cleared
        captureResultsPresenter.updateDataItem(GroupType.BLOOD_PRESSURE, itemField, secondaryProperty, "", secondaryViewHolderUI);

        // show validation errors
        verify(secondaryViewHolderUI, times(1)).onValidationFailed("Required");
    }

    @Test
    public void diastolic_cleared_remove_systolic_required() {
        // captured
        captureResultsPresenter.updateDataItem(GroupType.BLOOD_PRESSURE, itemField, property, "1", viewHolderUI);
        // then cleared
        captureResultsPresenter.updateDataItem(GroupType.BLOOD_PRESSURE, itemField, property, "", viewHolderUI);
        // hide validation errors
        verify(secondaryViewHolderUI, times(1)).onValidationPassed();
    }

    @Test
    public void systolic_cleared_remove_diastolic_required() {
        // captured
        captureResultsPresenter.updateDataItem(GroupType.BLOOD_PRESSURE, itemField, secondaryProperty, "1", secondaryViewHolderUI);
        // then cleared
        captureResultsPresenter.updateDataItem(GroupType.BLOOD_PRESSURE, itemField, secondaryProperty, "", secondaryViewHolderUI);
        // hide validation errors
        verify(viewHolderUI, times(1)).onValidationPassed();
    }
}
