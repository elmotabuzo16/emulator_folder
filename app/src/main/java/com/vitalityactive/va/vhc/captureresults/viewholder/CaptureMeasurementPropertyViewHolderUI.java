package com.vitalityactive.va.vhc.captureresults.viewholder;

public interface CaptureMeasurementPropertyViewHolderUI {
    void onValidationFailed(String failureMessage);
    void onValidationPassed();
    void clearValue();
    CaptureMeasurementPropertyViewHolderUI getSiblingProperty();
}
