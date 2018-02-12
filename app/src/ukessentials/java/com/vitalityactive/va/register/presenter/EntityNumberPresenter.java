package com.vitalityactive.va.register.presenter;

import com.vitalityactive.va.R;

public class EntityNumberPresenter extends ActivateBaseCredentialPresenter {
    @Override
    public int getHintResourceId() {
        return R.string.UKE_activate_entity_number_358;
    }

    @Override
    public CharSequence getFieldDescription() {
        return null;
    }

    @Override
    public int getIconResourceId() {
        return R.drawable.card_active;
    }

    @Override
    public int getDisabledIconResourceId() {
        return R.drawable.card_inactive;
    }

    @Override
    public boolean shouldShowValidationErrorMessage() {
        if (value == null || value.isEmpty())
            return false;
        // do any specific validation checks on the value
        return false;
    }
}
