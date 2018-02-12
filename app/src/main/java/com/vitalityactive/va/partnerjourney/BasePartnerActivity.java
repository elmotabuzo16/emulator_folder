package com.vitalityactive.va.partnerjourney;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.Presenter;

public abstract class BasePartnerActivity<TUI, TPresenter extends Presenter<TUI>> extends BasePresentedActivity<TUI, TPresenter> {
    public static final String EXTRA_PARTNER_TYPE = "EXTRA_PARTNER_TYPE";
    public static final String KEY_PARTNER_ID = "PARTNER_NAME";

    @Override
    @CallSuper
    protected void create(@Nullable Bundle savedInstanceState) {
        setTheme(getScreenTheme());
    }

    @StyleRes
    protected int getScreenTheme() {
        return getPartnerType().theme;
    }

    @StringRes
    protected int getScreenTitle() {
        return getPartnerType().listScreenTitle;
    }

    protected PartnerType getPartnerType() {
        return PartnerType.valueOf(getIntent().getStringExtra(EXTRA_PARTNER_TYPE));
    }
}
