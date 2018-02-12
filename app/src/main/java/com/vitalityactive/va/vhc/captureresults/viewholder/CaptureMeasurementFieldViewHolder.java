package com.vitalityactive.va.vhc.captureresults.viewholder;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.UnitsOfMeasure;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;
import com.vitalityactive.va.utilities.implemented.OnItemSelectedPositionChangedListener;
import com.vitalityactive.va.vhc.captureresults.CaptureResultsPresenter;
import com.vitalityactive.va.vhc.captureresults.models.GroupType;
import com.vitalityactive.va.vhc.dto.MeasurementItemField;
import com.vitalityactive.va.vhc.dto.UnitAbbreviationDescription;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class CaptureMeasurementFieldViewHolder extends GenericRecyclerViewAdapter.ViewHolder<MeasurementItemField> {
    private static final String VHC_CAPTURE_DIALOG_TYPE = "VHC_CAPTURE_DIALOG";
    private final int spinnerColor;
    private final GroupType groupType;
    private final CaptureResultsPresenter presenter;
    private final Date currentMembershipPeriodStart;
    private final int dialogValueSelectedColor;
    private final int dialogValueUnselectedColor;
    private PropertyViewHolder primaryProperty;
    private PropertyViewHolder secondaryProperty;
    private Spinner unitsSpinner;
    private TextView unitTextView;
    private TextView dateTested;
    private TextView subtitle;
    private View fieldWithDialog;
    private View propertiesLayout;
    private TextView dialogFieldTitle;
    private TextView dialogFieldSubtitle;

    private CaptureMeasurementFieldViewHolder(final View itemView, GroupType groupType, CaptureResultsPresenter presenter, CaptureMeasurementParameters parameters) {
        super(itemView);
        this.spinnerColor = parameters.spinnerColor;
        this.dialogValueSelectedColor = parameters.dialogValueSelectedColor;
        this.dialogValueUnselectedColor = parameters.dialogValueUnselectedColor;
        this.groupType = groupType;
        this.presenter = presenter;
        this.currentMembershipPeriodStart = parameters.currentMembershipPeriodStart;
        assignItemViewProperties(itemView);
    }

    @Override
    public void bindWith(final MeasurementItemField dataItem) {
        if (shouldUseNormalPropertiesForItem()) {
            setupPropertyFields(dataItem);
        } else {
            setupFieldWithDialogSelector(dataItem);
        }
        setupDateTestedCalendarControl(dataItem);
    }

    private boolean shouldUseNormalPropertiesForItem() {
        return groupType != GroupType.URINE_PROTEIN;
    }

    private void setupPropertyFields(MeasurementItemField dataItem) {
        fieldWithDialog.setVisibility(View.GONE);

        setupSubtitle(dataItem.getSubtitle());

        primaryProperty.bindWith(dataItem, dataItem.getPrimaryMeasurementProperty());
        secondaryProperty.bindWith(dataItem, dataItem.getSecondaryMeasurementProperty());

        setupUnitOfMeasurement(dataItem);

    }

    private void setupFieldWithDialogSelector(final MeasurementItemField dataItem) {
        propertiesLayout.setVisibility(View.GONE);
        dialogFieldTitle = fieldWithDialog.findViewById(R.id.vhc_field_with_dialog_text);
        dialogFieldSubtitle = fieldWithDialog.findViewById(R.id.vhc_field_with_dialog_subtitle);
        View dialogSpinner = fieldWithDialog.findViewById(R.id.vhc_field_with_dialog_spinner);
        ViewUtilities.setSpinnerColor(dialogSpinner, spinnerColor);
        setSelectedItemOnTextView(isNoneSelected(dataItem) ? getNoneString() : dataItem.getSelectedItem());

        final ArrayList<AlertDialogFragment.AlertDialogItem> alertDialogItems = new ArrayList<>();
        List<MeasurementItemField.SelectableItem> selectableItems = dataItem.getSelectableItems();
        for (int i = 0; i < selectableItems.size(); i++) {
            MeasurementItemField.SelectableItem item = selectableItems.get(i);
            boolean checked = item.value.equals(dataItem.getSelectedItem());
            alertDialogItems.add(new AlertDialogFragment.AlertDialogItem(item.title, item.description, 0, i, checked));
        }
        alertDialogItems.add(new AlertDialogFragment.AlertDialogItem(getNoneString(), 0, selectableItems.size(), isNoneSelected(dataItem)));

        fieldWithDialog.setOnClickListener(view -> {
            AlertDialogFragment.OnItemSelectedListener onItemSelectedListener = selected -> onFieldWithDialogItemSelected(dataItem, selected);
            AlertDialogFragment.create(VHC_CAPTURE_DIALOG_TYPE, dataItem.getTitle(), alertDialogItems)
                    .setItemSelectedListener(onItemSelectedListener)
                    .show(presenter.getFragmentManager(), VHC_CAPTURE_DIALOG_TYPE);
        });
    }

    private boolean isNoneSelected(MeasurementItemField dataItem) {
        return dataItem.getSelectedItem() == null;
    }

    @NonNull
    private String getNoneString() {
        return itemView.getContext().getString(R.string.capture_results_none_message_337);
    }

    private void onFieldWithDialogItemSelected(MeasurementItemField dataItem, AlertDialogFragment.AlertDialogItem selected) {
        String value;
        if (selected.getIdentifier() == dataItem.getSelectableItems().size()) {
            value = selected.getTitle();
            presenter.updateDataSelectedItem(groupType, dataItem, null);
        } else {
            MeasurementItemField.SelectableItem item = dataItem.getSelectableItems().get(selected.getIdentifier());
            value = item.value;
            presenter.updateDataSelectedItem(groupType, dataItem, value);
        }
        setSelectedItemOnTextView(value);
    }

    private void setSelectedItemOnTextView(@NonNull String value) {
        if (value.equals(getNoneString())) {
            setSelectedItemOnTextView(value, View.GONE, this.dialogValueUnselectedColor);
        } else {
            setSelectedItemOnTextView(value, View.VISIBLE, dialogValueSelectedColor);
            dialogFieldSubtitle.setText(getValidOptionDescription(value));
        }
    }

    private void setSelectedItemOnTextView(String value, int subtitleVisibility, int textColor) {
        dialogFieldTitle.setText(value);
        dialogFieldTitle.setTextColor(textColor);
        dialogFieldSubtitle.setVisibility(subtitleVisibility);
    }

    private String getValidOptionDescription(String validOptionValue) {
        return presenter.getValidOptionDescription(validOptionValue);
    }

    private void setupSubtitle(String subtitle) {
        boolean hasSubtitle = !TextUtils.isEmpty(subtitle);
        this.subtitle.setVisibility(hasSubtitle ? View.VISIBLE : View.GONE);
        this.subtitle.setText(subtitle);
        this.primaryProperty.hideSubtitleOnValidationFailures(this.subtitle, hasSubtitle);
        this.secondaryProperty.hideSubtitleOnValidationFailures(this.subtitle, hasSubtitle);
    }

    private void setupUnitOfMeasurement(MeasurementItemField dataItem) {
        boolean onlyHaveASingleUnit = dataItem.getUnits().size() == 1;
        setUnitOfMeasureVisibility(onlyHaveASingleUnit);

        if (onlyHaveASingleUnit) {
            setupUnitOfMeasurementTextView(dataItem);
        } else {
            setupUnitOfMeasurementSpinner(dataItem);
        }
    }

    private void setUnitOfMeasureVisibility(boolean onlyHaveASingleUnit) {
        unitTextView.setVisibility(onlyHaveASingleUnit ? View.VISIBLE : View.GONE);
        unitsSpinner.setVisibility(onlyHaveASingleUnit ? View.GONE : View.VISIBLE);
    }

    private void setupUnitOfMeasurementTextView(MeasurementItemField dataItem) {
        UnitAbbreviationDescription onlyUnit = dataItem.getUnits().get(0);
        unitTextView.setText(onlyUnit.getAbbreviation());
        evaluateDualFields(onlyUnit, false);
        presenter.onUnitOfMeasureChanged(groupType, dataItem, onlyUnit);
    }

    private void setupUnitOfMeasurementSpinner(final MeasurementItemField dataItem) {
        final List<UnitAbbreviationDescription> availableUnits = dataItem.getUnits();
        ArrayAdapter<UnitAbbreviationDescription> adapter =
                new ArrayAdapter<UnitAbbreviationDescription>(
                        itemView.getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        availableUnits) {
                    @NonNull
                    @Override
                    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                        TextView v = (TextView) super.getView(position, convertView, parent);
                        v.setText(availableUnits.get(position).getAbbreviation());
                        v.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                        return v;
                    }
                };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitsSpinner.setAdapter(adapter);

        unitsSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                itemView.findViewById(R.id.vhc_properties).requestFocus();
                return false;
            }
        });

        unitsSpinner.setOnItemSelectedListener(new OnItemSelectedPositionChangedListener() {
            @Override
            protected void onSelectedItemChanged(int position) {
                UnitAbbreviationDescription selectedUnit = availableUnits.get(position);
                if (presenter.onUnitOfMeasureChanged(groupType, dataItem, selectedUnit)) {

                    primaryProperty.bindWith(dataItem, dataItem.getPrimaryMeasurementProperty());
                    secondaryProperty.bindWith(dataItem, dataItem.getSecondaryMeasurementProperty());

                    evaluateDualFields(selectedUnit, true);
                }
            }
        });
        unitsSpinner.setSelection(dataItem.getSelectedUnitOfMeasurementIndex());
        ViewUtilities.setSpinnerColor(unitsSpinner, spinnerColor);

        //Evaluate the first position since it's the one that will be rendered first
        evaluateDualFields(availableUnits.get(0), true);
    }

    private void evaluateDualFields(UnitAbbreviationDescription selectedUnit, boolean clearfield) {
        if (clearfield) {
            primaryProperty.clearValue();
            secondaryProperty.clearValue();
        }

        primaryProperty.acceptOnlyDecimalDigits();
        secondaryProperty.acceptOnlyDecimalDigits();

        if (selectedUnit.getUnit2() != null && !TextUtils.isEmpty(selectedUnit.getUnit2().name)) {
            secondaryProperty.showLayout();
            primaryProperty.setHint(selectedUnit.getUnit1().name);
            secondaryProperty.setHint(selectedUnit.getUnit2().name);


            if (selectedUnit.getUnitOfMeasure().getTypeKey().equals(UnitsOfMeasure.STONEPOUND.getTypeKey())) {
                primaryProperty.acceptOnlyNumberDigits();
                secondaryProperty.acceptOnlyNumberDigits();
            }

            if (selectedUnit.getUnitOfMeasure().getTypeKey().equals(UnitsOfMeasure.FOOTINCH.getTypeKey())) {
                primaryProperty.acceptOnlyNumberDigits();
                secondaryProperty.acceptOnlyNumberDigits();
            }
        } else {
            secondaryProperty.hideLayout();
        }

    }

    private void setupDateTestedCalendarControl(final MeasurementItemField dataItem) {
        final Calendar calendar = Calendar.getInstance();
        Date capturedDateTested = dataItem.getDateTested();
        if (capturedDateTested.getTime() > 0) {
            calendar.setTime(capturedDateTested);
        }
        updateDateTested(calendar.getTime(), dataItem);
        dateTested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener = (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    updateDateTested(calendar.getTime(), dataItem);
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(dateTested.getContext(), listener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(getTimestampAFewSecondsFromNowSoThatCurrentTimestampCanBeBetweenMaxAndMin());
                datePickerDialog.getDatePicker().setMinDate(getLatestLowerDateLimit());
                datePickerDialog.show();
            }

            private long getTimestampAFewSecondsFromNowSoThatCurrentTimestampCanBeBetweenMaxAndMin() {
                return Calendar.getInstance().getTimeInMillis() + 10000;
            }
        });
        ViewUtilities.setSpinnerColor(dateTested, spinnerColor);
    }

    private long getLatestLowerDateLimit() {
        com.vitalityactive.va.utilities.date.Date todayMinusSixMonths = new TimeUtilities().now().minusMonths(6);
        return Math.max(currentMembershipPeriodStart.getTime(), todayMinusSixMonths.getMillisecondsSinceEpoch());
    }

    private void updateDateTested(Date now, MeasurementItemField dataItem) {
        dateTested.setText(DateFormattingUtilities.formatWeekdayDateMonthAbbreviatedYear(itemView.getContext(), now.getTime()));
        presenter.setDateCaptured(groupType, dataItem, now);
    }

    private void assignItemViewProperties(View itemView) {
        propertiesLayout = itemView.findViewById(R.id.vhc_properties);
        primaryProperty = new PropertyViewHolder(groupType, presenter,
                itemView.findViewById(R.id.vhc_primary_measurement_item_field_layout),
                itemView.findViewById(R.id.vhc_primary_measurement_item_field_text));
        secondaryProperty = new PropertyViewHolder(groupType, presenter,
                itemView.findViewById(R.id.vhc_secondary_measurement_item_field_layout),
                itemView.findViewById(R.id.vhc_secondary_measurement_item_field_text));

        primaryProperty.setSiblingProperty(secondaryProperty);
        secondaryProperty.setSiblingProperty(primaryProperty);

        subtitle = itemView.findViewById(R.id.subtitle);
        fieldWithDialog = itemView.findViewById(R.id.vhc_field_with_dialog);
        unitsSpinner = itemView.findViewById(R.id.vhc_measurement_unit_spinner);
        unitTextView = itemView.findViewById(R.id.vhc_measurement_unit_text);
        dateTested = itemView.findViewById(R.id.vhc_date_tested);
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<MeasurementItemField, CaptureMeasurementFieldViewHolder> {
        private final GroupType groupType;
        private CaptureResultsPresenter presenter;
        private final CaptureMeasurementParameters parameters;

        public Factory(GroupType groupType, CaptureResultsPresenter presenter, CaptureMeasurementParameters parameters) {
            this.groupType = groupType;
            this.presenter = presenter;
            this.parameters = parameters;
        }

        @Override
        public CaptureMeasurementFieldViewHolder createViewHolder(View itemView) {
            return new CaptureMeasurementFieldViewHolder(itemView, groupType, presenter, parameters);
        }
    }
}
