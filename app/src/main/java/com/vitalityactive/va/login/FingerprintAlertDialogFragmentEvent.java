package com.vitalityactive.va.login;

/**
 * Created by jayellos on 12/4/17.
 */

public class FingerprintAlertDialogFragmentEvent {
    private FingerprintAlertDialogFragment.DialogType dialogType;
    private ClickedButtonType clickedButtonType;
    private boolean isCheck;

    public enum ClickedButtonType {
        POSITIVE,
        NEGATIVE
    }

    FingerprintAlertDialogFragmentEvent(FingerprintAlertDialogFragment.DialogType dialogType, ClickedButtonType clickedButtonType, boolean isCheck) {
        this.dialogType = dialogType;
        this.clickedButtonType = clickedButtonType;
        this.isCheck = isCheck;
    }

    FingerprintAlertDialogFragmentEvent(FingerprintAlertDialogFragment.DialogType dialogType) {
         this(dialogType, FingerprintAlertDialogFragmentEvent.ClickedButtonType.POSITIVE,true);
    }

    public FingerprintAlertDialogFragment.DialogType getDialogType() {
        return dialogType;
    }

    public ClickedButtonType getClickedButtonType() {
        return clickedButtonType;
    }

    public boolean isCheck() {
        return isCheck;
    }

}
