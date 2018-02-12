package com.vitalityactive.va.vhc.captureresults.viewholder;

import android.support.annotation.ColorInt;

import java.util.Date;

public class CaptureMeasurementParameters {
    public final int tintColor;
    public final int spinnerColor;
    public final int dialogValueSelectedColor;
    public final Date currentMembershipPeriodStart;
    public final int dialogValueUnselectedColor;

    public CaptureMeasurementParameters(@ColorInt int tintColor, @ColorInt int spinnerColor, @ColorInt int dialogValueSelectedColor,
                                        @ColorInt int dialogValueUnselectedColor, Date currentMembershipPeriodStart)
    {
        this.tintColor = tintColor;
        this.spinnerColor = spinnerColor;
        this.dialogValueSelectedColor = dialogValueSelectedColor;
        this.currentMembershipPeriodStart = currentMembershipPeriodStart;
        this.dialogValueUnselectedColor = dialogValueUnselectedColor;
    }
}
