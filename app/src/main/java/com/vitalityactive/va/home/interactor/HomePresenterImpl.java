package com.vitalityactive.va.home.interactor;

import android.support.annotation.NonNull;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.dto.HomeCardDTO;
import com.vitalityactive.va.dto.RewardHomeCardDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.home.HomeSectionType;
import com.vitalityactive.va.home.activerewardsrewardscard.RewardsSelectionCardFragment;
import com.vitalityactive.va.home.activerewardsrewardscard.RewardsVoucherCardFragment;
import com.vitalityactive.va.home.cards.HomeScreenCardFragment;
import com.vitalityactive.va.home.cards.HomeScreenCardSection;
import com.vitalityactive.va.home.events.GetCardCollectionResponseEvent;
import com.vitalityactive.va.home.events.GetEventStatusByPartyIdResponseEvent;
import com.vitalityactive.va.vitalitystatus.VitalityStatusDTO;
import com.vitalityactive.va.vitalitystatus.repository.VitalityStatusRepository;

import java.util.List;

public class HomePresenterImpl implements HomePresenter {
    private final HomeInteractor interactor;
    private final EventDispatcher eventDispatcher;
    private final EventListener<GetCardCollectionResponseEvent> getCardCollectionResponseEventListener;
    private final EventListener<GetEventStatusByPartyIdResponseEvent> getEventStatusByPartyIdResponseEventListener;
    private final VitalityStatusRepository statusRepository;
    private Ui userInterface;

    public HomePresenterImpl(final HomeInteractor interactor,
                             final EventDispatcher eventDispatcher,
                             final Scheduler scheduler,
                             VitalityStatusRepository statusRepository) {
        this.interactor = interactor;
        this.eventDispatcher = eventDispatcher;
        this.getCardCollectionResponseEventListener = buildEventListenerForGetCardCollectionRequest(scheduler);
        this.getEventStatusByPartyIdResponseEventListener = buildEventListenerForGetEventStatusByPartyIdRequest(scheduler);
        this.statusRepository = statusRepository;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        interactor.clearVhrStatus();
    }

    @Override
    public void onUserInterfaceAppeared() {
        addEventListeners();
        loadCardsFromService();
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        removeEventListeners();
    }

    @Override
    public void onUserInterfaceDestroyed() {
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(GetCardCollectionResponseEvent.class, getCardCollectionResponseEventListener);
        eventDispatcher.addEventListener(GetEventStatusByPartyIdResponseEvent.class, getEventStatusByPartyIdResponseEventListener);
    }

    private void removeEventListeners() {
        eventDispatcher.removeEventListener(GetCardCollectionResponseEvent.class, getCardCollectionResponseEventListener);
        eventDispatcher.removeEventListener(GetEventStatusByPartyIdResponseEvent.class, getEventStatusByPartyIdResponseEventListener);
    }

    @Override
    public void setUserInterface(Ui userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void retryLoadingHomeScreenCardSections() {
        loadCardsFromService();
    }

    private void loadCardsFromService() {
        userInterface.showLoadingIndicator();
        interactor.fetchHomeCards();
    }

    private List<HomeScreenCardSection> createHomeScreenCardSections() {
        List<HomeCardDTO> cards = interactor.getHomeCards();

        HomeScreenCardSection.Builder builder = new HomeScreenCardSection.Builder();

        for (HomeCardDTO card : cards) {
            builder.add(card.section, card.type);
        }

        List<HomeScreenCardFragment.Factory> factories = builder.getFactories(HomeSectionType.GET_REWARDED);
        for (RewardHomeCardDTO card : interactor.getRewardHomeCards(HomeCardType.VOUCHERS)) {
            factories.add(new RewardsVoucherCardFragment.Factory(card.rewardId, card.awardedRewardId));
        }
        for (RewardHomeCardDTO card : interactor.getRewardHomeCards(HomeCardType.REWARD_SELECTION)) {
            factories.add(new RewardsSelectionCardFragment.Factory(card.rewardId, card.awardedRewardId));
        }

        return builder.build();
    }

    @Override
    public void startCheckingVhrStatus() {
        userInterface.showLoadingIndicator();
        interactor.checkEventStatusByPartyId(EventType._VHRASSMNTCOMPLETED);
    }

    private void onHomeScreenCardsLoaded() {
        userInterface.hideLoadingIndicator();
        userInterface.onHomeScreenCardSectionsReady(createHomeScreenCardSections());
        VitalityStatusDTO vitalityStatus = statusRepository.getVitalityStatus();

        if (vitalityStatus == null) {
            return;
        }

        userInterface.setStatusInfo(vitalityStatus);
    }

    private void onHomeScreenCardsFailedToLoad(GetCardCollectionResponseEvent event) {
        userInterface.hideLoadingIndicator();
        userInterface.onHomeScreenCardSectionsFailed(event);
    }

    private void onEventStatusObtained() {
        if (interactor.getVhrStatus() == HomeInteractor.STARTED) {
            userInterface.navigateToActiveRewardsLanding();
        } else {
            userInterface.navigateToVhrLandingScreen();
        }
    }

    private void onEventStatusRequestFailed(GetEventStatusByPartyIdResponseEvent event) {
        userInterface.hideLoadingIndicator();
        userInterface.showVhrStatusValidationFailedAlert(event);
    }

    @NonNull
    private EventListener<GetCardCollectionResponseEvent> buildEventListenerForGetCardCollectionRequest(final Scheduler scheduler) {
        return new EventListener<GetCardCollectionResponseEvent>() {
            @Override
            public void onEvent(final GetCardCollectionResponseEvent event) {
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        handleCardResponseEvent(event);
                    }
                });
            }
        };
    }

    private void handleCardResponseEvent(GetCardCollectionResponseEvent event) {
        if (event.isSuccessful()) {
//            if (statusRepository.hasInstruction()) {
                //fetch data for level increased modal
//            } else {
                userInterface.hideLoadingIndicator();
                onHomeScreenCardsLoaded();
//            }
        } else {
            onHomeScreenCardsFailedToLoad(event);
        }
    }

    @NonNull
    private EventListener<GetEventStatusByPartyIdResponseEvent> buildEventListenerForGetEventStatusByPartyIdRequest(final Scheduler scheduler) {
        return new EventListener<GetEventStatusByPartyIdResponseEvent>() {
            @Override
            public void onEvent(final GetEventStatusByPartyIdResponseEvent event) {
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (event.isSuccessful()) {
                            onEventStatusObtained();
                        } else {
                            onEventStatusRequestFailed(event);
                        }
                    }
                });
            }
        };
    }

}
