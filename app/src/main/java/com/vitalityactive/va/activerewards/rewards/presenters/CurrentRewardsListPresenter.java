package com.vitalityactive.va.activerewards.rewards.presenters;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.activerewards.rewards.dto.UnclaimedRewardDTO;
import com.vitalityactive.va.activerewards.rewards.events.CurrentRewardsRequestCompletedEvent;
import com.vitalityactive.va.activerewards.rewards.interactor.RewardsInteractor;
import com.vitalityactive.va.dependencyinjection.ar.ActiveRewardsScope;
import com.vitalityactive.va.dto.PartnerItemDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.partnerjourney.service.PartnerListRequestEvent;

import java.util.List;

import javax.inject.Inject;

@ActiveRewardsScope
public class CurrentRewardsListPresenter extends BasePresenter<CurrentRewardsListPresenter.UserInterface> {

    private RewardsInteractor interactor;
    private EventDispatcher eventDispatcher;
    private MainThreadScheduler scheduler;

    private EventListener<CurrentRewardsRequestCompletedEvent> rewardsRequestCompletedEventListener = new EventListener<CurrentRewardsRequestCompletedEvent>() {
        @Override
        public void onEvent(final CurrentRewardsRequestCompletedEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    if (isUserInterfaceVisible()) {
                        showRewards();
                        if (event == CurrentRewardsRequestCompletedEvent.GENERIC_ERROR) {
                            userInterface.showGenericErrorMessage();
                        } else if (event == CurrentRewardsRequestCompletedEvent.CONNECTION_ERROR) {
                            userInterface.showConnectionErrorMessage();
                        }
                    }
                }
            });
        }
    };

    private EventListener<PartnerListRequestEvent> partnerListRequestEventListener = new EventListener<PartnerListRequestEvent>() {
        @Override
        public void onEvent(final PartnerListRequestEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    if (isUserInterfaceVisible()) {
                        userInterface.hideLoadingIndicator();
                        switch (event.result) {
                            case SUCCESSFUL:
                                showRewardPartners();
                                break;
                            case CONNECTION_ERROR:
                                userInterface.showConnectionErrorMessage();
                                break;
                            case GENERIC_ERROR:
                                userInterface.showGenericErrorMessage();
                                break;
                        }
                    }
                }
            });
        }
    };

    @Inject
    CurrentRewardsListPresenter(RewardsInteractor interactor,
                                EventDispatcher eventDispatcher,
                                MainThreadScheduler scheduler) {
        this.interactor = interactor;
        this.eventDispatcher = eventDispatcher;
        this.scheduler = scheduler;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        if (isNewNavigation) {
            removeEventListeners();
            addEventListeners();
        }
    }

    private void removeEventListeners() {
        eventDispatcher.removeEventListener(CurrentRewardsRequestCompletedEvent.class, rewardsRequestCompletedEventListener);
        eventDispatcher.removeEventListener(PartnerListRequestEvent.class, partnerListRequestEventListener);
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(CurrentRewardsRequestCompletedEvent.class, rewardsRequestCompletedEventListener);
        eventDispatcher.addEventListener(PartnerListRequestEvent.class, partnerListRequestEventListener);
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();
        userInterface.showLoadingIndicator();
        showRewards();
        showRewardPartners();
        interactor.fetchCurrentRewards();
    }

    private void showRewards() {
        userInterface.showRewards(interactor.getAvailableUnclaimedRewards(), interactor.getAvailableRewardVouchers());
    }

    private void showRewardPartners() {
        List<PartnerItemDTO> rewardPartners = interactor.getActiveRewardsPartners();
        if (rewardPartners != null && !rewardPartners.isEmpty()) {
            userInterface.showRewardPartners(rewardPartners);
        }
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);
    }

    public void onRetry() {
        interactor.fetchCurrentRewards();
    }

    public void fetchRewardPartners() {
        List<PartnerItemDTO> rewardPartners = interactor.getActiveRewardsPartners();
        if (rewardPartners != null && !rewardPartners.isEmpty()) {
            userInterface.hideLoadingIndicator();
            userInterface.showRewardPartners(rewardPartners);
        } else {
            interactor.fetchRewardPartners();
        }
    }

    public interface UserInterface {
        void showRewards(List<UnclaimedRewardDTO> availableUnclaimedRewards, List<RewardVoucherDTO> availableRewardVouchers);

        void showRewardPartners(List<PartnerItemDTO> rewardPartners);

        void showGenericErrorMessage();

        void showConnectionErrorMessage();

        void showLoadingIndicator();

        void hideLoadingIndicator();

    }
}
