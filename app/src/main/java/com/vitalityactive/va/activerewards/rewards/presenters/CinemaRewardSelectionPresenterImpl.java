package com.vitalityactive.va.activerewards.rewards.presenters;

import android.support.annotation.VisibleForTesting;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.activerewards.rewards.dto.RewardSelectionDTO;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.activerewards.rewards.events.RewardVoucherSelectedEvent;
import com.vitalityactive.va.activerewards.rewards.interactor.RewardsInteractor;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;

public class CinemaRewardSelectionPresenterImpl extends BasePresenter<CinemaRewardSelectionPresenter.UserInterface> implements CinemaRewardSelectionPresenter, EventListener<RewardVoucherSelectedEvent> {

    private final RewardsInteractor interactor;
    private final EventDispatcher eventDispatcher;
    private final MainThreadScheduler scheduler;

    private long rewardUniqueId;
    private RewardVoucherSelectedEvent rewardSelectionsRequestResult = RewardVoucherSelectedEvent.NONE;

    public CinemaRewardSelectionPresenterImpl(RewardsInteractor interactor, EventDispatcher eventDispatcher, MainThreadScheduler scheduler) {
        this.interactor = interactor;
        this.eventDispatcher = eventDispatcher;
        this.scheduler = scheduler;
    }

    @Override
    public void onEvent(final RewardVoucherSelectedEvent event) {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                setRewardSelectionsRequestResult(event);
                if (isUserInterfaceVisible()) {
                    handleRewardSelectionsRequestResult();
                }
            }
        });
    }

    @VisibleForTesting
    void setRewardSelectionsRequestResult(RewardVoucherSelectedEvent event) {
        this.rewardSelectionsRequestResult = event;
    }

    @VisibleForTesting
    void handleRewardSelectionsRequestResult() {
        if (rewardSelectionsRequestResult == RewardVoucherSelectedEvent.GENERIC_ERROR) {
            userInterface.showGenericErrorMessage();
        } else if (rewardSelectionsRequestResult == RewardVoucherSelectedEvent.CONNECTION_ERROR) {
            userInterface.showConnectionErrorMessage();
        } else if (rewardSelectionsRequestResult != RewardVoucherSelectedEvent.NONE) {
            if (rewardSelectionsRequestResult.getRewardUniqueId() == rewardUniqueId) {
                userInterface.hideLoadingIndicator();
                userInterface.showRewardSelections(interactor.getSelectionsForCinemaRewardWithId(rewardUniqueId));
            } else {
                RewardVoucherDTO rewardVoucher = interactor.getRewardVoucherById(rewardSelectionsRequestResult.getRewardUniqueId());
                if (rewardVoucher == null) {
                    userInterface.showGenericErrorMessage();
                } else {
                    userInterface.navigateAfterRewardVoucherSelected(rewardVoucher.uniqueId);
                }
            }
        }
        rewardSelectionsRequestResult = RewardVoucherSelectedEvent.NONE;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        if (isNewNavigation) {
            removeEventListeners();
            addEventListeners();
            fetchRewardVoucher();
        }
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();
        if (interactor.isFetchingRewardSelections(rewardUniqueId)) {
            userInterface.showLoadingIndicator();
        } else {
            userInterface.hideLoadingIndicator();
            handleRewardSelectionsRequestResult();
        }
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);
        if (isFinishing) {
            removeEventListeners();
        }
    }

    private void removeEventListeners() {
        eventDispatcher.removeEventListener(RewardVoucherSelectedEvent.class, this);
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(RewardVoucherSelectedEvent.class, this);
    }

    @Override
    public void setRewardUniqueId(long rewardUniqueId) {
        this.rewardUniqueId = rewardUniqueId;
    }

    @Override
    public void fetchRewardVoucher() {
        interactor.fetchRewardVoucher(rewardUniqueId);
    }

    @Override
    public void onConfirm(RewardSelectionDTO selectedRewardVoucher) {
        userInterface.showLoadingIndicator();
        interactor.selectVoucher(rewardUniqueId, selectedRewardVoucher.rewardValueLinkId);
    }

}
