package com.vitalityactive.va.vhc.captureresults;

import android.support.v4.app.FragmentManager;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.vhc.captureresults.models.GroupType;
import com.vitalityactive.va.vhc.captureresults.viewholder.CaptureMeasurementPropertyViewHolderUI;
import com.vitalityactive.va.vhc.dto.MeasurementItem;
import com.vitalityactive.va.vhc.dto.MeasurementItemField;
import com.vitalityactive.va.vhc.dto.MeasurementProperty;
import com.vitalityactive.va.vhc.dto.UnitAbbreviationDescription;

import java.util.Date;
import java.util.List;

public interface CaptureResultsPresenter extends Presenter<CaptureResultsPresenter.UserInterface> {
    void updateDataItem(GroupType groupType,
                        MeasurementItemField dataItem,
                        MeasurementProperty property,
                        String value,
                        CaptureMeasurementPropertyViewHolderUI fieldUI);
    void updateDataSelectedItem(GroupType groupType, MeasurementItemField dataItem, String value);
    void setDateCaptured(GroupType groupType, MeasurementItemField dataItem, Date date);
    boolean onUnitOfMeasureChanged(GroupType groupType, MeasurementItemField dataItem, UnitAbbreviationDescription selectedUnit);
    void submit();

    FragmentManager getFragmentManager();

    String getValidOptionDescription(String validOptionValue);

    interface UserInterface {
        void showIncompleteBMIInformationAlert();
        void navigateAway();
        void updateMeasurementFields(List<MeasurementItem> items);
        void canSubmit(boolean allowed);
        FragmentManager getSupportFragmentManager();
    }
}
