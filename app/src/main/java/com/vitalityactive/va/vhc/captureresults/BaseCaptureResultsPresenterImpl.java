package com.vitalityactive.va.vhc.captureresults;

import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.vitalityactive.va.utilities.MethodCallTrace;
import com.vitalityactive.va.vhc.captureresults.models.CapturedField;
import com.vitalityactive.va.vhc.captureresults.models.CapturedGroup;
import com.vitalityactive.va.vhc.captureresults.models.GroupType;
import com.vitalityactive.va.vhc.captureresults.viewholder.CaptureMeasurementPropertyViewHolderUI;
import com.vitalityactive.va.vhc.content.VHCHealthAttributeContent;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepository;
import com.vitalityactive.va.vhc.dto.MeasurementItem;
import com.vitalityactive.va.vhc.dto.MeasurementItemField;
import com.vitalityactive.va.vhc.dto.MeasurementProperty;
import com.vitalityactive.va.vhc.dto.UnitAbbreviationDescription;

import java.util.Date;
import java.util.List;

public class BaseCaptureResultsPresenterImpl implements CaptureResultsPresenter {
    protected final HealthAttributeRepository repository;
    protected final VHCHealthAttributeContent content;
    protected UserInterface userInterface;
    protected BothOrNeitherFieldsCapturedState bmiState = new BothOrNeitherFieldsCapturedState();
    private BothOrNeitherFieldsCapturedState bloodPressureState = new BothOrNeitherFieldsCapturedState();
    protected MeasurementPersister measurementPersister;

    public BaseCaptureResultsPresenterImpl(HealthAttributeRepository repository,
                                       VHCHealthAttributeContent content,
                                       MeasurementPersister measurementPersister) {
        this.repository = repository;
        this.content = content;
        this.measurementPersister = measurementPersister;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
    }

    @Override
    public void onUserInterfaceAppeared() {
        MethodCallTrace.start();
        List<MeasurementItem> items = measurementPersister.buildMeasurementItems();
        userInterface.updateMeasurementFields(items);
        MethodCallTrace.stop();
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
    }

    @Override
    public void onUserInterfaceDestroyed() {
    }

    @Override
    public void updateDataItem(GroupType groupType, MeasurementItemField field, MeasurementProperty property,
                               String value, CaptureMeasurementPropertyViewHolderUI fieldUI) {

        boolean valueIsValid = property.valueIsValid(value);

        if (valueIsValid || TextUtils.isEmpty(value)) {
            fieldUI.onValidationPassed();
        } else {
            fieldUI.onValidationFailed(property.getRangeDetails());
        }

        repository.persistCapturedData(groupType, field, property, value, valueIsValid);

        updateBMIState(field, !value.isEmpty());
        updateBloodPressureState(field, property.isPrimaryProperty(), !value.isEmpty(), fieldUI);
        updateCanContinue();
    }

    @Override
    public void updateDataSelectedItem(GroupType groupType, MeasurementItemField field, String value) {
        repository.persistSelectedItem(groupType, field, value);
        updateCanContinue();
    }

    @Override
    public void setDateCaptured(GroupType groupType, MeasurementItemField dataItem, Date date) {
        repository.persistDateCaptured(groupType, dataItem, date);
        updateCanContinue();
    }

    protected void updateBMIState(MeasurementItemField dataItem, boolean isCaptured) {
        if (measurementPersister.isHeight(dataItem)) {
            bmiState.field1Captured = isCaptured;
        } else if (measurementPersister.isWeight(dataItem)) {
            bmiState.field2Captured = isCaptured;
        }
    }

    protected void updateBloodPressureState(MeasurementItemField field, boolean updatedDiastolic, boolean isCaptured, CaptureMeasurementPropertyViewHolderUI fieldUI) {
        if (!measurementPersister.isBloodPressure(field))
            return;

        if (updatedDiastolic) {
            bloodPressureState.field1Captured = isCaptured;
        } else {
            bloodPressureState.field2Captured = isCaptured;
        }

        if (updatedDiastolic && isCaptured && !bloodPressureState.field2Captured) {
            fieldUI.getSiblingProperty().onValidationFailed(content.getValidationRequiredString());
        } else if (!updatedDiastolic && isCaptured && !bloodPressureState.field1Captured) {
            fieldUI.getSiblingProperty().onValidationFailed(content.getValidationRequiredString());
        } else if (updatedDiastolic && !isCaptured && bloodPressureState.field2Captured) {
            fieldUI.onValidationFailed(content.getValidationRequiredString());
        } else if (!updatedDiastolic && !isCaptured && bloodPressureState.field1Captured) {
            fieldUI.onValidationFailed(content.getValidationRequiredString());
        } else if (updatedDiastolic && !isCaptured && !bloodPressureState.field2Captured) {
            fieldUI.getSiblingProperty().onValidationPassed();
        } else if (!updatedDiastolic && !isCaptured && !bloodPressureState.field1Captured) {
            fieldUI.getSiblingProperty().onValidationPassed();
        }
    }

    @Override
    public boolean onUnitOfMeasureChanged(GroupType groupType, MeasurementItemField dataItem, UnitAbbreviationDescription selectedUnit) {
        repository.persistUnitOfMeasure(groupType, dataItem, selectedUnit);
        updateCanContinue();
        return dataItem.setUnitOfMeasurement(selectedUnit);
    }

    protected void updateCanContinue() {
        boolean anyInvalidFields = hasAnyIncompleteFieldThatIsCaptured();
        boolean hasAtLeastOneCapturedGroup = repository.getCapturedGroupsWithOneOrMoreCompletelyCapturedFields().size() > 0;
        boolean bloodPressureOk = bloodPressureState.bothFieldsCaptured() || bloodPressureState.bothFieldsNotCaptured();
        userInterface.canSubmit(hasAtLeastOneCapturedGroup && !anyInvalidFields && bloodPressureOk);
    }

    private boolean hasAnyIncompleteFieldThatIsCaptured() {
        List<CapturedGroup> groups = repository.getCapturedGroupWithFieldsThatAreNotCompleted();
        for (CapturedGroup group : groups) {
            for (CapturedField field : group.getCapturedFields()) {
                if (field.isFieldCaptureComplete())
                    continue;

                if (field.isPrimaryValueCaptured() || field.isSecondaryValueCaptured()) {
                    return true;
                }
            }
        }

        // no group with fields that are incomplete, but with some value captured
        // ie either: all fields are complete || no fields are captured
        return false;
    }

    @Override
    public void submit() {
        if (bmiState.bothFieldsCaptured() || bmiState.bothFieldsNotCaptured()) {
            // if the values are captured but invalid, then the button would be disabled
            userInterface.navigateAway();
        } else {
            userInterface.showIncompleteBMIInformationAlert();
        }
    }

    @Override
    public FragmentManager getFragmentManager() {
        return userInterface.getSupportFragmentManager();
    }

    @Override
    public String getValidOptionDescription(String validOptionValue) {
        return content.getValidOptionDescription(validOptionValue);
    }

    @Override
    public void setUserInterface(CaptureResultsPresenter.UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    protected class BothOrNeitherFieldsCapturedState {
        public boolean field1Captured;
        public boolean field2Captured;

        // can combine in single == check, but separate methods read clearer
        protected boolean bothFieldsCaptured() {
            return field1Captured && field2Captured;
        }

        protected boolean bothFieldsNotCaptured() {
            return !field1Captured && !field2Captured;
        }
    }
}
