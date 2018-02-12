package com.vitalityactive.va.activerewards.rewards.presenters;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.activerewards.rewards.dto.RewardSelectionDTO;
import com.vitalityactive.va.dependencyinjection.ar.ActiveRewardsScope;

import java.util.List;

@ActiveRewardsScope
public interface CinemaRewardSelectionPresenter
        extends Presenter<CinemaRewardSelectionPresenter.UserInterface> {

    void setRewardUniqueId(long rewardUniqueId);

    void fetchRewardVoucher();

    void onConfirm(RewardSelectionDTO selectedRewardVoucher);

    interface UserInterface {
        void showRewardSelections(List<RewardSelectionDTO> selections);

        void showGenericErrorMessage();

        void showConnectionErrorMessage();

        void showLoadingIndicator();

        void hideLoadingIndicator();

        void navigateAfterRewardVoucherSelected(long uniqueId);
    }
}
