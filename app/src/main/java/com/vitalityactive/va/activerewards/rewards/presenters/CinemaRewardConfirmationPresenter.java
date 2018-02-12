package com.vitalityactive.va.activerewards.rewards.presenters;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.activerewards.rewards.dto.UnclaimedRewardDTO;
import com.vitalityactive.va.activerewards.rewards.events.RewardVoucherSelectedEvent;
import com.vitalityactive.va.activerewards.rewards.interactor.RewardsInteractor;
import com.vitalityactive.va.activerewards.rewards.service.MarkNoRewardWheelSpinUsedEvent;
import com.vitalityactive.va.constants.RewardId;
import com.vitalityactive.va.dependencyinjection.ar.ActiveRewardsScope;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;

import javax.inject.Inject;

@ActiveRewardsScope
public class CinemaRewardConfirmationPresenter extends BasePresenter<CinemaRewardConfirmationPresenter.UserInterface> {
    private RewardsInteractor interactor;
    private EventDispatcher eventDispatcher;
    private long rewardUniqueId;
    private UserInterface userInterface;
    private MainThreadScheduler scheduler;
    private RewardVoucherSelectedEvent voucherSelectedEvent = RewardVoucherSelectedEvent.NONE;
    private EventListener<RewardVoucherSelectedEvent> rewardVoucherSelectedEventListener = new EventListener<RewardVoucherSelectedEvent>() {
        @Override
        public void onEvent(final RewardVoucherSelectedEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    voucherSelectedEvent = event;
                    if (isUserInterfaceVisible()) {
                        handleVoucherSelectedEvent();
                    }
                }
            });
        }
    };
    private MarkNoRewardWheelSpinUsedEvent markNoRewardWheelSpinUsedEvent = MarkNoRewardWheelSpinUsedEvent.NONE;
    private EventListener<MarkNoRewardWheelSpinUsedEvent> markNoRewardWheelSpinUsedEventListener = new EventListener<MarkNoRewardWheelSpinUsedEvent>() {
        @Override
        public void onEvent(final MarkNoRewardWheelSpinUsedEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    markNoRewardWheelSpinUsedEvent = event;
                    if (isUserInterfaceVisible()) {
                        handleMarkNoRewardWheelSpinUsedEvent();
                    }
                }
            });
        }
    };

    private void handleVoucherSelectedEvent() {
        if (voucherSelectedEvent == RewardVoucherSelectedEvent.NONE) {
            return;
        }
        userInterface.hideLoadingIndicator();
        if (voucherSelectedEvent == RewardVoucherSelectedEvent.GENERIC_ERROR) {
            userInterface.showGenericErrorMessage();
        } else if (voucherSelectedEvent == RewardVoucherSelectedEvent.CONNECTION_ERROR) {
            userInterface.showConnectionErrorMessage();
        } else {
            userInterface.showSelectedVoucher(interactor.getRewardVoucherById(voucherSelectedEvent.getRewardUniqueId()));
        }
        voucherSelectedEvent = RewardVoucherSelectedEvent.NONE;
    }

    private void handleMarkNoRewardWheelSpinUsedEvent() {
        if (markNoRewardWheelSpinUsedEvent == MarkNoRewardWheelSpinUsedEvent.NONE) {
            return;
        }
        userInterface.hideLoadingIndicator();
        if (markNoRewardWheelSpinUsedEvent == MarkNoRewardWheelSpinUsedEvent.GENERIC_ERROR) {
            userInterface.showGenericErrorMessage();
        } else if (markNoRewardWheelSpinUsedEvent == MarkNoRewardWheelSpinUsedEvent.CONNECTION_ERROR) {
            userInterface.showConnectionErrorMessage();
        } else {
            userInterface.showNoReward();
        }
        markNoRewardWheelSpinUsedEvent = MarkNoRewardWheelSpinUsedEvent.NONE;
    }

    @Inject
    CinemaRewardConfirmationPresenter(RewardsInteractor interactor, EventDispatcher eventDispatcher, MainThreadScheduler scheduler) {
        this.interactor = interactor;
        this.eventDispatcher = eventDispatcher;
        this.scheduler = scheduler;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        if (isNewNavigation) {
            removeEventListeners();
            addEventListeners();
            voucherSelectedEvent = RewardVoucherSelectedEvent.NONE;
            selectReward();
        }
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();
        handleVoucherSelectedEvent();
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);
        if (isFinishing) {
            removeEventListeners();
        }
    }

    private void removeEventListeners() {
        eventDispatcher.removeEventListener(RewardVoucherSelectedEvent.class, rewardVoucherSelectedEventListener);
        eventDispatcher.removeEventListener(MarkNoRewardWheelSpinUsedEvent.class, markNoRewardWheelSpinUsedEventListener);
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(RewardVoucherSelectedEvent.class, rewardVoucherSelectedEventListener);
        eventDispatcher.addEventListener(MarkNoRewardWheelSpinUsedEvent.class, markNoRewardWheelSpinUsedEventListener);
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void setRewardUniqueId(long rewardUniqueId) {
        this.rewardUniqueId = rewardUniqueId;
    }

    private void selectReward() {
        UnclaimedRewardDTO unclaimedReward = interactor.getWheelSpin(rewardUniqueId);
        if (unclaimedReward == null) {
            return;
        }
        userInterface.showLoadingIndicator();
        if (unclaimedReward.outcomeRewardId == RewardId._NOWIN) {
            interactor.markNoRewardWheelSpinUsed(rewardUniqueId);
        } else {
            interactor.selectVoucher(rewardUniqueId);
        }
    }

    public void onTryAgain() {
        selectReward();
    }

    public interface UserInterface {

        void showSelectedVoucher(RewardVoucherDTO rewardVoucher);

        void showLoadingIndicator();

        void hideLoadingIndicator();

        void showGenericErrorMessage();

        void showConnectionErrorMessage();

        void showNoReward();
    }
}
