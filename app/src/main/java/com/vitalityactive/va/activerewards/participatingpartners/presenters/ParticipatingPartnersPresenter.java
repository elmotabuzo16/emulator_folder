package com.vitalityactive.va.activerewards.participatingpartners.presenters;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.activerewards.participatingpartners.ParticipatingPartnersInteractor;
import com.vitalityactive.va.dependencyinjection.ar.ActiveRewardsScope;
import com.vitalityactive.va.dto.PartnerItemDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.partnerjourney.service.PartnerListRequestEvent;

import java.util.List;

import javax.inject.Inject;

@ActiveRewardsScope
public class ParticipatingPartnersPresenter
        extends BasePresenter<ParticipatingPartnersPresenter.UserInterface>
        implements EventListener<PartnerListRequestEvent> {

    private ParticipatingPartnersInteractor interactor;
    private EventDispatcher eventDispatcher;
    private MainThreadScheduler scheduler;

    @Inject
    ParticipatingPartnersPresenter(ParticipatingPartnersInteractor interactor,
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
        eventDispatcher.removeEventListener(PartnerListRequestEvent.class, this);
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(PartnerListRequestEvent.class, this);
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();
        fetchRewardPartners();
    }

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

    private void showRewardPartners() {
        userInterface.showRewardPartners(interactor.getActiveRewardsPartners());
    }

    public void onRetry() {
        interactor.fetchRewardPartners();
    }

    public void fetchRewardPartners() {
        List<PartnerItemDTO> rewardPartners = interactor.getActiveRewardsPartners();
        if (rewardPartners != null && !rewardPartners.isEmpty()) {
            userInterface.hideLoadingIndicator();
            userInterface.showRewardPartners(rewardPartners);
        } else {
            userInterface.showLoadingIndicator();
            interactor.fetchRewardPartners();
        }
    }

    public interface UserInterface {

        void showRewardPartners(List<PartnerItemDTO> rewardPartners);

        void showGenericErrorMessage();

        void showConnectionErrorMessage();


        void showLoadingIndicator();

        void hideLoadingIndicator();
    }
}
