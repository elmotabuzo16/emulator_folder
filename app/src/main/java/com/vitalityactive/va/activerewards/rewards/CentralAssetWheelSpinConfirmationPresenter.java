package com.vitalityactive.va.activerewards.rewards;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.activerewards.rewards.interactor.RewardsInteractor;
import com.vitalityactive.va.dependencyinjection.ar.ActiveRewardsScope;

import javax.inject.Inject;

@ActiveRewardsScope
class CentralAssetWheelSpinConfirmationPresenter extends BasePresenter<CentralAssetWheelSpinConfirmationPresenter.UserInterface> {

    private long rewardUniqueId;
    private RewardsInteractor interactor;

    @Inject
    public CentralAssetWheelSpinConfirmationPresenter(RewardsInteractor interactor) {
        this.interactor = interactor;
    }

    public void setRewardUniqueId(long rewardUniqueId) {
        this.rewardUniqueId = rewardUniqueId;
    }

    void onSelectVoucherTapped() {
        interactor.fetchRewardVoucher(rewardUniqueId);
    }

    public interface UserInterface {
    }

}
