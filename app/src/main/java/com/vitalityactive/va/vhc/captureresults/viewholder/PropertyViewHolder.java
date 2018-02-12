package com.vitalityactive.va.vhc.captureresults.viewholder;

import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vitalityactive.va.utilities.implemented.OnLostFocusListener;
import com.vitalityactive.va.utilities.implemented.TextWatcherWithDefaultBeforeAndAfter;
import com.vitalityactive.va.vhc.captureresults.CaptureResultsPresenter;
import com.vitalityactive.va.vhc.captureresults.models.GroupType;
import com.vitalityactive.va.vhc.dto.MeasurementItemField;
import com.vitalityactive.va.vhc.dto.MeasurementProperty;

public class PropertyViewHolder implements CaptureMeasurementPropertyViewHolderUI {
    private GroupType groupType;
    private CaptureResultsPresenter presenter;
    private TextInputLayout layout;
    private EditText field;
    private TextView subtitle;
    private boolean hasSubtitle = false;
    private CaptureMeasurementPropertyViewHolderUI siblingProperty;

    public PropertyViewHolder(GroupType groupType, CaptureResultsPresenter presenter, TextInputLayout layout, EditText field) {
        this.groupType = groupType;
        this.presenter = presenter;
        this.layout = layout;
        this.field = field;
    }


    public void acceptOnlyNumberDigits() {
        this.field.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    public void acceptOnlyDecimalDigits() {
        this.field.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    public void bindWith(MeasurementItemField dataItem, MeasurementProperty property) {

        setupTextAndFocusListeners(dataItem, property);
        if (!property.isVisible()) {
            layout.setVisibility(View.GONE);
            return;
        }

        layout.setVisibility(View.VISIBLE);
        layout.setHint(property.getName());

        if (property.getValue() == null) {
            field.setText("");
        } else {
            if (field.getInputType() == InputType.TYPE_CLASS_NUMBER) {
                field.setText(String.valueOf(property.getValue().intValue()));
            } else {
                field.setText(String.valueOf(property.getValue()));
            }
        }
    }

    public void showLayout() {
        layout.setVisibility(View.VISIBLE);
    }

    public void hideLayout() {
        layout.setVisibility(View.GONE);
    }

    public void hideSubtitleOnValidationFailures(TextView subtitle, boolean hasSubtitle) {
        this.subtitle = subtitle;
        this.hasSubtitle = hasSubtitle;
    }

    @Override
    public void onValidationFailed(String failureMessage) {
        layout.setError(failureMessage);
        layout.setErrorEnabled(true);
        if (hasSubtitle) {
            subtitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onValidationPassed() {
        layout.setErrorEnabled(false);
        if (hasSubtitle) {
            subtitle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void clearValue() {
        field.setText("");
    }

    @Override
    public CaptureMeasurementPropertyViewHolderUI getSiblingProperty() {
        return siblingProperty;
    }

    public void setSiblingProperty(CaptureMeasurementPropertyViewHolderUI siblingProperty) {
        this.siblingProperty = siblingProperty;
    }

    private void setupTextAndFocusListeners(final MeasurementItemField dataItem, final MeasurementProperty property) {
        field.setOnFocusChangeListener(new OnLostFocusListener() {
            @Override
            protected void onLostFocus(View v) {
                String value = field.getText().toString();
                presenter.updateDataItem(groupType, dataItem, property, value, PropertyViewHolder.this);
            }
        });

        field.addTextChangedListener(new TextWatcherWithDefaultBeforeAndAfter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.updateDataItem(groupType, dataItem, property, s.toString(), PropertyViewHolder.this);
            }
        });
    }

    public void setHint(String hint) {
        layout.setHint(hint);
    }
}
