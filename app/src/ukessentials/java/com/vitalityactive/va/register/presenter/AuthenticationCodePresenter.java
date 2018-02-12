package com.vitalityactive.va.register.presenter;

import com.vitalityactive.va.R;

public class AuthenticationCodePresenter extends ActivateBaseCredentialPresenter {
    @Override
    public int getHintResourceId() {
        return R.string.UKE_activate_authentication_code_355;
    }

    @Override
    public CharSequence getFieldDescription() {
        return null;
    }

    @Override
    public int getIconResourceId() {
        return R.drawable.code_active;
    }

    @Override
    public int getDisabledIconResourceId() {
        return R.drawable.code_inactive;
    }
}
