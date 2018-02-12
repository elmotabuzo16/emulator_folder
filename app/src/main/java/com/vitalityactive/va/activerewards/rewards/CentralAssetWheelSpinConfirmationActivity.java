package com.vitalityactive.va.activerewards.rewards;

import android.os.Bundle;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;

import javax.inject.Inject;

public class CentralAssetWheelSpinConfirmationActivity extends BasePresentedActivity<CentralAssetWheelSpinConfirmationPresenter.UserInterface, CentralAssetWheelSpinConfirmationPresenter> implements CentralAssetWheelSpinConfirmationPresenter.UserInterface {

    public static final String REWARD_UNIQUE_ID = "REWARD_UNIQUE_ID";

    @Inject
    CentralAssetWheelSpinConfirmationPresenter wheelSpinConfirmationPresenter;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_central_asset_wheel_spin_confirmation);
        presenter.setRewardUniqueId(getIntent().getLongExtra(REWARD_UNIQUE_ID, 0L));
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        navigationCoordinator.getActiveRewardsDependencyInjector().inject(this);
    }

    @Override
    protected CentralAssetWheelSpinConfirmationPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected CentralAssetWheelSpinConfirmationPresenter getPresenter() {
        return wheelSpinConfirmationPresenter;
    }

    public void handleSelectVoucherButtonTapped(View view) {
        presenter.onSelectVoucherTapped();
    }
}
