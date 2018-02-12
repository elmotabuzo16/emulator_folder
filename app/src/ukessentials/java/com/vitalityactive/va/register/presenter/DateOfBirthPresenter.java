package com.vitalityactive.va.register.presenter;

import com.vitalityactive.va.R;
import com.vitalityactive.va.utilities.date.LocalDate;

public class DateOfBirthPresenter extends ActivateBaseCredentialPresenter {
    private LocalDate date;

    @Override
    public int getHintResourceId() {
        return R.string.UKE_activate_date_of_birth_363;
    }

    @Override
    public CharSequence getFieldDescription() {
        return null;
    }

    @Override
    public int getIconResourceId() {
        return R.drawable.calendar_active;
    }

    @Override
    public int getDisabledIconResourceId() {
        return R.drawable.calendar_inactive;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(int year, int month, int dayOfMonth) {
        date = new LocalDate(year, month, dayOfMonth);
    }

    public void setDate(String date) {
        this.date = new LocalDate(date);
        onValueChanged(date);
    }
}
