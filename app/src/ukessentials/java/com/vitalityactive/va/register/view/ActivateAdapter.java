package com.vitalityactive.va.register.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vitalityactive.va.R;
import com.vitalityactive.va.register.ActivationValues;
import com.vitalityactive.va.register.presenter.ActivateBaseCredentialPresenter;
import com.vitalityactive.va.register.presenter.DateOfBirthPresenter;

public class ActivateAdapter extends RecyclerView.Adapter<BaseRegistrationCellViewHolder> {
    final LayoutInflater inflater;
    private final ActivateUserInterface userInterface;
    final ActivateBaseCredentialPresenter dateOfBirth;
    final ActivateBaseCredentialPresenter authenticationCode;
    final ActivateBaseCredentialPresenter entityNumber;
    private int black;

    public ActivateAdapter(Context context, ActivateUserInterface userInterface,
                           ActivateBaseCredentialPresenter dateOfBirth,
                           ActivateBaseCredentialPresenter authenticationCode,
                           ActivateBaseCredentialPresenter entityNumber, int black) {
        inflater = LayoutInflater.from(context);
        this.userInterface = userInterface;
        this.dateOfBirth = dateOfBirth.setAdapter(this);
        this.authenticationCode = authenticationCode.setAdapter(this);
        this.entityNumber = entityNumber.setAdapter(this);
        this.black = black;
        onValuesChanged();
    }

    @Override
    public BaseRegistrationCellViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        int viewType = getViewType(position);
        View itemView = inflater.inflate(viewType, parent, false);
        if (position == 0)
            return new RegistrationCellWithSpinnerViewHolder(itemView, black);
        return new RegistrationCellViewHolder(itemView);
    }

    protected int getViewType(int position) {
        switch (position) {
            case 0:
                return R.layout.registration_list_item_with_spinner;
            case 4:
                return R.layout.entity_number_footer;
            case 2:
                return R.layout.authentication_code_footer;
            case 1:
            case 3:
            default:
                return R.layout.registration_list_item;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(BaseRegistrationCellViewHolder holder, int position) {
        if (position == 0) {
            holder.bindWith(dateOfBirth);
        } else if (position == 1) {
            holder.bindWith(authenticationCode);
        } else if (position == 3) {
            holder.bindWith(entityNumber);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public void onValuesChanged() {
        if (dateOfBirth.shouldShowValidationErrorMessage() || authenticationCode.shouldShowValidationErrorMessage() || entityNumber.shouldShowValidationErrorMessage()) {
            userInterface.disallowActivation();
        } else {
            userInterface.allowActivation();
        }
    }

    public ActivationValues getActivationValues() {
        return new ActivationValues(
                dateOfBirth.getStringValue(),
                authenticationCode.getStringValue(),
                entityNumber.getStringValue());
    }

    public void loadValues(@Nullable ActivationValues activationValues) {
        if (activationValues == null)
            return;
        ((DateOfBirthPresenter) dateOfBirth).setDate(activationValues.dateOfBirth);
        authenticationCode.onValueChanged(activationValues.authenticationCode);
        entityNumber.onValueChanged(activationValues.entityNumber);
    }
}
