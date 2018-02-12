package com.vitalityactive.va.dependencyinjection;

import com.vitalityactive.va.uicomponents.AlertDialogFragment;

public enum BaseUrl {
    DEV,
    TEST,
    QA,
    QA_FF,
    PROD;

    public AlertDialogFragment.AlertDialogItem buildAlertDialogItem() {
        return new AlertDialogFragment.AlertDialogItem(getFriendlyName(), 0, ordinal(), false);
    }

    public String getFriendlyName() {
        switch (this) {
            case DEV:
                return "Dev";
            case TEST:
                return "Test";
            case QA:
                return "QA";
            case QA_FF:
                return "QA (Frankfurt)";
            case PROD:
                return "Production";
        }
        return this.name();
    }
}
