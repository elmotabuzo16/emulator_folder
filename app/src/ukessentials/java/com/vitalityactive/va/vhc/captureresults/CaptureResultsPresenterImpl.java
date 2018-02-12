package com.vitalityactive.va.vhc.captureresults;

import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.utilities.MethodCallTrace;
import com.vitalityactive.va.vhc.captureresults.models.GroupType;
import com.vitalityactive.va.vhc.captureresults.viewholder.CaptureMeasurementPropertyViewHolderUI;
import com.vitalityactive.va.vhc.content.VHCHealthAttributeContent;
import com.vitalityactive.va.vhc.content.VHCHealthAttributeContentFromStringResource;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepository;
import com.vitalityactive.va.vhc.dto.MeasurementItem;
import com.vitalityactive.va.vhc.dto.MeasurementItemField;
import com.vitalityactive.va.vhc.dto.MeasurementProperty;

import java.util.List;

import javax.inject.Inject;

public class CaptureResultsPresenterImpl extends BaseCaptureResultsPresenterImpl {

    private BothOrNeitherFieldsCapturedState cholesterolRatioState = new BothOrNeitherFieldsCapturedState();

    public CaptureResultsPresenterImpl(HealthAttributeRepository repository,
                                       VHCHealthAttributeContent content,
                                       MeasurementPersister measurementPersister) {
        super(repository, content, measurementPersister);
    }

    @Override
    public void onUserInterfaceAppeared() {
        MethodCallTrace.start();
        List<MeasurementItem> items = measurementPersister.buildMeasurementItems();

        //UKE: Remove Cholesterol Ratio for the Capture
        for (MeasurementItem item: items) {
            if (item.type == GroupType.CHOLESTEROL) {
                for (int i=0; i<item.measurementItemFields.size(); i++) {
                    if (item.measurementItemFields.get(i).getFieldKey().equalsIgnoreCase(String.valueOf(EventType._LIPIDRATIOCALCULATED))) {
                        item.measurementItemFields.remove(i);
                    }
                }
            }
        }

        userInterface.updateMeasurementFields(items);
        MethodCallTrace.stop();
    }

    @Override
    public void updateDataItem(GroupType groupType, MeasurementItemField field, MeasurementProperty property,
                               String value, CaptureMeasurementPropertyViewHolderUI fieldUI) {
        boolean valueIsValid = property.valueIsValid(value);

        if (valueIsValid) {
            fieldUI.onValidationPassed();
        } else if (value.equals("")) {
            if (groupType == GroupType.CHOLESTEROL) {
                fieldUI.onValidationFailed(content.getValidationRequiredForPointsString());
            } else {
                fieldUI.onValidationPassed();
            }
        } else {
            fieldUI.onValidationFailed(property.getRangeDetails());
        }

        repository.persistCapturedData(groupType, field, property, value, valueIsValid);

        updateBMIState(field, !value.isEmpty());
        updateBloodPressureState(field, property.isPrimaryProperty(), !value.isEmpty(), fieldUI);
        updateCholesterolRatioState(field, !value.isEmpty());
        updateCanContinue();
    }

    private void updateCholesterolRatioState(MeasurementItemField dataItem, boolean isCaptured) {
        if (measurementPersister.isHDLCholesterol(dataItem.getFieldKey())) {
            cholesterolRatioState.field1Captured = isCaptured;
        } else if (measurementPersister.isTotalCholesterol(dataItem)) {
            cholesterolRatioState.field2Captured = isCaptured;
        }
    }

    @Override
    public void submit() {
        if (bmiState.bothFieldsCaptured() && cholesterolRatioState.bothFieldsCaptured()) {
            // if the values are captured but invalid, then the button would be disabled
            userInterface.navigateAway();
        } else {
            userInterface.showIncompleteBMIInformationAlert();
        }
    }
}
