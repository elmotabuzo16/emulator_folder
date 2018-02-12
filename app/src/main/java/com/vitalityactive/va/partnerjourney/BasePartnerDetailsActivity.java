package com.vitalityactive.va.partnerjourney;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.vitalityactive.va.Presenter;

abstract class BasePartnerDetailsActivity<TUI, TPresenter extends Presenter<TUI>> extends BasePartnerActivity<TUI, TPresenter> {
    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        long partnerId = getIntent().getLongExtra(BasePartnerActivity.KEY_PARTNER_ID, 0);
        create(savedInstanceState, getPartnerType(), partnerId);
    }

    protected abstract void create(Bundle savedInstanceState, PartnerType partnerType, long partnerId);
}
