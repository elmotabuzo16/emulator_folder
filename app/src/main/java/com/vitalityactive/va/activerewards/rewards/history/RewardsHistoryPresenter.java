package com.vitalityactive.va.activerewards.rewards.history;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.activerewards.rewards.history.dto.HistoricalRewardDTO;
import com.vitalityactive.va.dependencyinjection.ar.ActiveRewardsScope;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;

import java.util.List;

import javax.inject.Inject;

@ActiveRewardsScope
public class RewardsHistoryPresenter extends BasePresenter<RewardsHistoryPresenter.UserInterface> {

    private RewardsHistoryInteractor interactor;
    private EventDispatcher eventDispatcher;
    private MainThreadScheduler scheduler;
    private EventListener<RewardsHistoryRequestCompletedEvent> rewardsHistoryRequestCompletedEventListener = new EventListener<RewardsHistoryRequestCompletedEvent>() {
        @Override
        public void onEvent(final RewardsHistoryRequestCompletedEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    if (isUserInterfaceVisible()) {
                        if (event == RewardsHistoryRequestCompletedEvent.SUCCESSFUL) {
                            userInterface.showRewardsHistory(interactor.getRewardsHistory());
                        } else if (event == RewardsHistoryRequestCompletedEvent.GENERIC_ERROR) {
                            userInterface.showGenericError();
                        } else if (event == RewardsHistoryRequestCompletedEvent.CONNECTION_ERROR) {
                            userInterface.showConnectionError();
                        }
                    }
                }
            });

        }
    };

    @Inject
    RewardsHistoryPresenter(RewardsHistoryInteractor interactor, EventDispatcher eventDispatcher, MainThreadScheduler scheduler) {
        this.interactor = interactor;
        this.eventDispatcher = eventDispatcher;
        this.scheduler = scheduler;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        super.onUserInterfaceCreated(isNewNavigation);
        if (isNewNavigation) {
            removeEventListeners();
            addEventListeners();
            interactor.fetchRewardsHistory();
        }
    }

    private void removeEventListeners() {
        eventDispatcher.removeEventListener(RewardsHistoryRequestCompletedEvent.class, rewardsHistoryRequestCompletedEventListener);
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(RewardsHistoryRequestCompletedEvent.class, rewardsHistoryRequestCompletedEventListener);
    }

    public interface UserInterface {
        void showRewardsHistory(List<HistoricalRewardDTO> rewardsHistory);

        void showGenericError();

        void showConnectionError();
    }

}
