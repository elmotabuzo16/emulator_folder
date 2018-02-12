package com.vitalityactive.va.activerewards.rewards.presenters;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.activerewards.rewards.dto.UnclaimedRewardDTO;
import com.vitalityactive.va.activerewards.rewards.events.WheelSpinRequestCompletedEvent;
import com.vitalityactive.va.activerewards.rewards.interactor.RewardsInteractor;
import com.vitalityactive.va.activerewards.rewards.dto.RewardSelectionDTO;
import com.vitalityactive.va.dependencyinjection.ar.ActiveRewardsScope;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;

import java.util.List;

import javax.inject.Inject;

@ActiveRewardsScope
public class WheelSpinPresenter extends BasePresenter<WheelSpinPresenter.UserInterface> {
    private RewardsInteractor interactor;
    private long rewardUniqueId;
    private EventDispatcher eventDispatcher;
    private MainThreadScheduler scheduler;
    private WheelSpinRequestCompletedEvent wheelSpinRequestCompletedEvent = WheelSpinRequestCompletedEvent.NONE;
    private EventListener<WheelSpinRequestCompletedEvent> wheelSpinRequestCompletedEventListener = new EventListener<WheelSpinRequestCompletedEvent>() {
        @Override
        public void onEvent(final WheelSpinRequestCompletedEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    wheelSpinRequestCompletedEvent = event;
                    if (isUserInterfaceVisible()) {
                        handleWheelSpinRequestCompletedEvent();
                    }
                }
            });
        }
    };

    private void handleWheelSpinRequestCompletedEvent() {
        if (wheelSpinRequestCompletedEvent == WheelSpinRequestCompletedEvent.NONE) {
            return;
        }
        userInterface.hideLoadingIndicator();
        switch (wheelSpinRequestCompletedEvent) {
            case SUCCESSFUL:
                showRewardSelections();
                break;
            case GENERIC_ERROR:
                userInterface.showGenericErrorMessage();
                break;
            case CONNECTION_ERROR:
                userInterface.showConnectionErrorMessage();
                break;
        }
        resetWheelSpinRequestCompletedEvent();
    }

    private void resetWheelSpinRequestCompletedEvent() {
        wheelSpinRequestCompletedEvent = WheelSpinRequestCompletedEvent.NONE;
    }

    private boolean showRewardSelections() {
        List<RewardSelectionDTO> selections = interactor.getSelectionsForWheelSpinWithId(rewardUniqueId);
        if (selections.isEmpty()) {
            return false;
        }
        userInterface.showRewardSelections(selections, getOutcomeSelectionIndex(selections));
        return true;
    }

    private int getOutcomeSelectionIndex(List<RewardSelectionDTO> selections) {
        UnclaimedRewardDTO wheelSpin = interactor.getWheelSpin(rewardUniqueId);
        if (wheelSpin == null) {
            return 0;
        }
        for (int i = 0; i < selections.size(); ++i) {
            RewardSelectionDTO selection = selections.get(i);
            if (selection.rewardValueLinkId == wheelSpin.outcomeRewardValueLinkId) {
                return i;
            }
        }
        return 0;
    }

    @Inject
    WheelSpinPresenter(RewardsInteractor interactor, EventDispatcher eventDispatcher, MainThreadScheduler scheduler) {
        this.interactor = interactor;
        this.eventDispatcher = eventDispatcher;
        this.scheduler = scheduler;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        if (isNewNavigation) {
            removeEventListeners();
            addEventListeners();
            resetWheelSpinRequestCompletedEvent();
            if (!showRewardSelections()) {
                fetchWheelSpin();
            }
        }
    }

    private void fetchWheelSpin() {
        userInterface.showLoadingIndicator();
        interactor.fetchWheelSpinWithId(rewardUniqueId);
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();
        handleWheelSpinRequestCompletedEvent();
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);
        if (isFinishing) {
            removeEventListeners();
        }
    }

    private void removeEventListeners() {
        eventDispatcher.removeEventListener(WheelSpinRequestCompletedEvent.class, wheelSpinRequestCompletedEventListener);
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(WheelSpinRequestCompletedEvent.class, wheelSpinRequestCompletedEventListener);
    }

    public void setRewardUniqueId(long rewardUniqueId) {
        this.rewardUniqueId = rewardUniqueId;
    }

    public UnclaimedRewardDTO getWheelSpin() {
        return interactor.getWheelSpin(rewardUniqueId);
    }

    public void onTryAgain() {
        fetchWheelSpin();
    }

    public interface UserInterface {
        void showRewardSelections(List<RewardSelectionDTO> rewardSelections, int outcomeSelectionIndex);

        void showLoadingIndicator();

        void hideLoadingIndicator();

        void showGenericErrorMessage();

        void showConnectionErrorMessage();
    }
}
