package com.vitalityactive.va.vhc.capture;

import com.vitalityactive.va.UnitsOfMeasure;
import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.vhc.captureresults.ValueLimit;
import com.vitalityactive.va.vhc.captureresults.models.GroupType;
import com.vitalityactive.va.vhc.dto.MeasurementItem;
import com.vitalityactive.va.vhc.dto.MeasurementItemField;
import com.vitalityactive.va.vhc.dto.UnitAbbreviationDescription;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CaptureResultsPresenterBMITests extends CaptureResultsPresenterBase {
    MeasurementItem item = MeasurementItemFieldTests.buildBodyMassIndex();
    private GroupType groupType = GroupType.UNKNOWN;

    @Before
    public void setup() {
        super.setup();
        item.measurementItemFields.clear();
        item.measurementItemFields.add(new MeasurementItemField.Builder(String.valueOf(EventType._HEIGHTCAPTURED), "H")
                .addUnit(new UnitAbbreviationDescription(UnitsOfMeasure.FOOT, "a", "a", new ValueLimit(0f, 10f, "")))
                .build());
        item.measurementItemFields.add(new MeasurementItemField.Builder(String.valueOf(EventType._WEIGHTCAPTURED), "w")
                .addUnit(new UnitAbbreviationDescription(UnitsOfMeasure.STONE, "a", "a", new ValueLimit(0f, 10f, "")))
                .build());
    }

    @Test
    public void both_fields_set_continue() {
        MeasurementItemField height = item.measurementItemFields.get(0);
        MeasurementItemField weight = item.measurementItemFields.get(1);

        captureResultsPresenter.updateDataItem(groupType, height, height.getPrimaryMeasurementProperty(), "1", viewHolderUI);
        captureResultsPresenter.updateDataItem(groupType, weight, weight.getPrimaryMeasurementProperty(), "2", viewHolderUI);

        captureResultsPresenter.submit();

        verify(userInterface, times(1)).navigateAway();
    }

    @Test
    public void not_all_fields_set_do_not_continue() {
        MeasurementItemField height = item.measurementItemFields.get(0);
        when(mockMeasurementPersister.isHeight(height)).thenReturn(true);

        captureResultsPresenter.updateDataItem(groupType, height, height.getPrimaryMeasurementProperty(), "1", viewHolderUI);

        captureResultsPresenter.submit();

        verify(userInterface, never()).navigateAway();
        verify(userInterface, times(1)).showIncompleteBMIInformationAlert();
    }
}
