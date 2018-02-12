package com.vitalityactive.va.register.view;

import android.support.annotation.NonNull;
import android.view.View;

import com.vitalityactive.va.register.presenter.CredentialPresenter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public abstract class BaseRegistrationCellViewHolder extends GenericRecyclerViewAdapter.ViewHolder<CredentialPresenter> implements CredentialUserInterface {
    public BaseRegistrationCellViewHolder(View itemView) {
        super(itemView);
    }

    @NonNull
    protected String getStringFromResources(int resourceId) {
        return itemView.getResources().getString(resourceId);
    }
}
