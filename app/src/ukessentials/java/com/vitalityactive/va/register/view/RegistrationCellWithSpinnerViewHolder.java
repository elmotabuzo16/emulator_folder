package com.vitalityactive.va.register.view;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.register.presenter.CredentialPresenter;
import com.vitalityactive.va.register.presenter.DateOfBirthPresenter;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.NonUserFacingDateFormatter;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

public class RegistrationCellWithSpinnerViewHolder extends BaseRegistrationCellViewHolder {
    private final TextInputLayout fieldLayout;
    private final TextView field;
    private DateOfBirthPresenter presenter;
    private int black;
    private TextView label;

    public RegistrationCellWithSpinnerViewHolder(View itemView, int black) {
        super(itemView);
        fieldLayout = itemView.findViewById(R.id.field_layout);
        field = itemView.findViewById(R.id.field);
        label = itemView.findViewById(R.id.label);
        this.black = black;
    }

    @Override
    public void bindWith(CredentialPresenter presenter) {
        this.presenter = (DateOfBirthPresenter) presenter;

        setupTextField(presenter);
        ViewUtilities.setSpinnerColor(field, black);
        setFieldDate();
        setupDateDialog();
    }

    private void setupTextField(CredentialPresenter presenter) {
        field.setCompoundDrawablesRelativeWithIntrinsicBounds(presenter.getDisabledIconResourceId(), 0, 0, 0);
        field.setHint(getStringFromResources(R.string.UKE_activate_enter_date_354));
        field.setInputType(InputType.TYPE_CLASS_DATETIME);
        label.setText(presenter.getHintResourceId());
    }

    private void setFieldDate() {
        LocalDate date = presenter.getDate();
        if (date != null) {
            field.setText(getFormattedDate(date));
        }
    }

    private String getFormattedDate(LocalDate date) {
        return new DateFormattingUtilities(field.getContext()).formatDateMonthYear(date);
    }

    private String getFormattedDateForService() {
        return NonUserFacingDateFormatter.getYearMonthDayFormatter().format(presenter.getDate());
    }

    private void setupDateDialog() {
        field.setOnClickListener(getDateDialogOnClickListener());
    }

    @NonNull
    private View.OnClickListener getDateDialogOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtilities.hideKeyboard(fieldLayout.getContext(), fieldLayout.getRootView());

                field.setCompoundDrawablesRelativeWithIntrinsicBounds(presenter.getIconResourceId(), 0, 0, 0);

                DatePickerDialog dialog = getDatePickerDialog(getOnDateSetListener());
                dialog.show();
            }
        };
    }

    @NonNull
    private DatePickerDialog.OnDateSetListener getOnDateSetListener() {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                presenter.setDate(year, month + 1, dayOfMonth);
                setFieldDate();
                presenter.onValueChanged(getFormattedDateForService());
            }
        };
    }

    @NonNull
    private DatePickerDialog getDatePickerDialog(DatePickerDialog.OnDateSetListener listener) {
        LocalDate date = presenter.getDate();
        if (date == null) {
            date = LocalDate.now();
        }

        DatePickerDialog dialog = new DatePickerDialog(field.getContext(),
                listener,
                date.getYear(),
                date.getMonth() - 1 % 12,
                date.getDayOfMonth());

        dialog.setOnDismissListener(getOnDismissListener());

        return dialog;
    }

    @NonNull
    private DialogInterface.OnDismissListener getOnDismissListener() {
        return new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                field.setCompoundDrawablesRelativeWithIntrinsicBounds(presenter.getDisabledIconResourceId(), 0, 0, 0);
            }
        };
    }

    @Override
    public void showValidationErrorMessage() {
        fieldLayout.setError(presenter.getValidationMessage());
    }

    @Override
    public void hideValidationErrorMessage() {
        fieldLayout.setErrorEnabled(false);
    }

    @Override
    public void setValue(CharSequence value) {
        field.setText(value);
    }
}
