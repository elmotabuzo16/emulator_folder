package com.vitalityactive.va.register.view;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.register.presenter.CredentialPresenter;
import com.vitalityactive.va.utilities.TextUtilities;

public class RegistrationCellViewHolder extends BaseRegistrationCellViewHolder {
    private final TextInputLayout fieldLayout;
    private final TextInputEditText field;
    private final ImageView icon;
    private final TextView fieldDescription;
    private TextWatcher textWatcher;
    private CredentialPresenter presenter;

    public RegistrationCellViewHolder(View itemView) {
        super(itemView);
        fieldLayout = (TextInputLayout) itemView.findViewById(R.id.field_layout);
        field = (TextInputEditText) itemView.findViewById(R.id.field);
        icon = (ImageView) itemView.findViewById(R.id.register_item_icon);
        fieldDescription = (TextView)itemView.findViewById(R.id.field_description_text);
    }

    @Override
    public void bindWith(final CredentialPresenter presenter) {
        presenter.bindWith(this);
        this.presenter = presenter;

        icon.setImageResource(TextUtilities.isNullOrWhitespace(field.getText()) ? presenter.getDisabledIconResourceId() : presenter.getIconResourceId());
        fieldLayout.setPasswordVisibilityToggleDrawable(R.drawable.password_visible_toggle_not_focused);

        field.setInputType(presenter.getInputType());
        fieldLayout.setHint(getStringFromResources(presenter.getHintResourceId()));

        CharSequence fieldDescription = presenter.getFieldDescription();
        if (!TextUtilities.isNullOrWhitespace(fieldDescription)) {
            this.fieldDescription.setText(fieldDescription);
            this.fieldDescription.setVisibility(View.VISIBLE);
            field.setCustomSelectionActionModeCallback(getCallbackPreventingSelection());
        }

        if (presenter.shouldShowValidationErrorMessage()) {
            showValidationErrorMessage();
        } else {
            hideValidationErrorMessage();
        }

        if (textWatcher != null) {
            field.removeTextChangedListener(textWatcher);
        }

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.onValueChanged(s);
                toggleValidationMessage(presenter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        field.addTextChangedListener(textWatcher);

        field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    presenter.onValueEntered();
                    toggleValidationMessage(presenter);
                }

                TextView fieldTextView = (TextView) view;
                int passwordToggleIconResourceId;
                int iconResourceId;

                if (hasFocus || !TextUtilities.isNullOrWhitespace(fieldTextView.getText())) {
                    iconResourceId = presenter.getIconResourceId();
                    passwordToggleIconResourceId = R.drawable.password_visible_toggle_focused;
                } else {
                    iconResourceId = presenter.getDisabledIconResourceId();
                    passwordToggleIconResourceId = R.drawable.password_visible_toggle_not_focused;
                }

                icon.setImageResource(iconResourceId);
                fieldLayout.setPasswordVisibilityToggleDrawable(passwordToggleIconResourceId);
            }
        });
    }

    @NonNull
    private ActionMode.Callback getCallbackPreventingSelection() {
        return new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        };
    }

    private void toggleValidationMessage(CredentialPresenter presenter) {
        if (presenter.shouldShowValidationErrorMessage()) {
            showValidationErrorMessage();
            this.fieldDescription.setVisibility(View.GONE);
        } else {
            hideValidationErrorMessage();
            this.fieldDescription.setVisibility(View.GONE);
        }
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
