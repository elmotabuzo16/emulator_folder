package com.vitalityactive.va.activerewards.rewards.presenters;

import android.support.annotation.Nullable;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.activerewards.rewards.dto.UnclaimedRewardDTO;
import com.vitalityactive.va.activerewards.rewards.events.PartnerRegisteredEmailFetchedEvent;
import com.vitalityactive.va.activerewards.rewards.events.PartnerRegisteredEmailUpdatedEvent;
import com.vitalityactive.va.activerewards.rewards.events.RewardVoucherSelectedEvent;
import com.vitalityactive.va.activerewards.rewards.interactor.RewardsInteractor;
import com.vitalityactive.va.dependencyinjection.ar.ActiveRewardsScope;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.utilities.TextUtilities;

import javax.inject.Inject;

@ActiveRewardsScope
public class StarbucksPartnerRegistrationPresenter extends BasePresenter<StarbucksPartnerRegistrationPresenter.UserInterface> {
    private long rewardUniqueId;
    private RewardsInteractor interactor;
    private MainThreadScheduler scheduler;
    private EventDispatcher eventDispatcher;
    private RewardVoucherSelectedEvent rewardSelectedEvent = RewardVoucherSelectedEvent.NONE;
    private PartnerRegisteredEmailUpdatedEvent emailUpdatedEvent = PartnerRegisteredEmailUpdatedEvent.NONE;
    private EventListener<PartnerRegisteredEmailFetchedEvent> partnerRegisteredEmailEventListener = new EventListener<PartnerRegisteredEmailFetchedEvent>() {
        @Override
        public void onEvent(final PartnerRegisteredEmailFetchedEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    if (isUserInterfaceVisible()) {
                        userInterface.hideLoadingIndicator();
                        showPartnerRegisteredEmail();
                    }
                }
            });
        }
    };

    private EventListener<RewardVoucherSelectedEvent> rewardSelectedEventListener = new EventListener<RewardVoucherSelectedEvent>() {
        @Override
        public void onEvent(final RewardVoucherSelectedEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    if (isUserInterfaceVisible()) {
                        handleRewardSelectedEvent(event);
                    } else {
                        rewardSelectedEvent = event;
                    }
                }
            });
        }
    };
    private EventListener<PartnerRegisteredEmailUpdatedEvent> partnerRegisteredEmailUpdatedEventEventListener = new EventListener<PartnerRegisteredEmailUpdatedEvent>() {
        @Override
        public void onEvent(PartnerRegisteredEmailUpdatedEvent event) {
            if (!isUserInterfaceVisible()) {
                emailUpdatedEvent = event;
            } else {
                handleEmailUpdatedEvent(event);
            }
        }
    };

    @Inject
    StarbucksPartnerRegistrationPresenter(RewardsInteractor interactor, MainThreadScheduler scheduler, EventDispatcher eventDispatcher) {
        this.interactor = interactor;
        this.scheduler = scheduler;
        this.eventDispatcher = eventDispatcher;
    }

    private void handleRewardSelectedEvent(RewardVoucherSelectedEvent event) {
        if (event == RewardVoucherSelectedEvent.GENERIC_ERROR) {
            userInterface.showGenericErrorMessage();
        } else if (event == RewardVoucherSelectedEvent.CONNECTION_ERROR) {
            userInterface.showConnectionErrorMessage();
        } else {
            RewardVoucherDTO rewardVoucher = interactor.getRewardVoucherById(event.getRewardUniqueId());
            if (rewardVoucher == null) {
                userInterface.showGenericErrorMessage();
            } else {
                userInterface.navigateAfterStarbucksRewardConfirmed(rewardVoucher.uniqueId);
            }
        }

        rewardSelectedEvent = RewardVoucherSelectedEvent.NONE;
    }

    private void handleEmailUpdatedEvent(PartnerRegisteredEmailUpdatedEvent event) {
        if (event == PartnerRegisteredEmailUpdatedEvent.SUCCESSFUL) {
            interactor.selectVoucher(rewardUniqueId);
        } else {
            if (event == PartnerRegisteredEmailUpdatedEvent.CONNECTION_ERROR) {
                userInterface.showConnectionErrorMessage();
            } else {
                userInterface.showGenericErrorMessage();
            }
        }

        emailUpdatedEvent = PartnerRegisteredEmailUpdatedEvent.NONE;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        if (isNewNavigation) {
            removeEventListeners();
            addEventListeners();
            fetchPartnerRegisteredEmailIfNeeded();
        }
        setRewardData();
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();

        if (emailUpdatedEvent != PartnerRegisteredEmailUpdatedEvent.NONE) {
            handleEmailUpdatedEvent(emailUpdatedEvent);
        } else if (rewardSelectedEvent != RewardVoucherSelectedEvent.NONE) {
            handleRewardSelectedEvent(rewardSelectedEvent);
        }

        if (!interactor.isFetchingPartnerRegisteredEmail()) {
            userInterface.hideLoadingIndicator();
            showPartnerRegisteredEmail();
        }
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);
        if (isFinishing) {
            removeEventListeners();
        }
    }

    private void showPartnerRegisteredEmail() {
        userInterface.showPartnerRegisteredEmail(interactor.getPartnerRegisteredEmail());
    }

    private void fetchPartnerRegisteredEmailIfNeeded() {
        if (TextUtilities.isNullOrWhitespace(interactor.getPartnerRegisteredEmail())) {
            userInterface.showLoadingIndicator();
            interactor.fetchPartnerRegisteredEmail();
        }
    }

    private void setRewardData() {
        userInterface.setRewardData(interactor.getWheelSpin(rewardUniqueId));
    }

    private void addEventListeners() {
        if (!interactor.shouldShowDataSharingConsentForWheelSpinOutcome(rewardUniqueId)) {
            eventDispatcher.addEventListener(RewardVoucherSelectedEvent.class, rewardSelectedEventListener);
            eventDispatcher.addEventListener(PartnerRegisteredEmailUpdatedEvent.class, partnerRegisteredEmailUpdatedEventEventListener);
        }
        eventDispatcher.addEventListener(PartnerRegisteredEmailFetchedEvent.class, partnerRegisteredEmailEventListener);
    }

    private void removeEventListeners() {
        eventDispatcher.removeEventListener(RewardVoucherSelectedEvent.class, rewardSelectedEventListener);
        eventDispatcher.removeEventListener(PartnerRegisteredEmailUpdatedEvent.class, partnerRegisteredEmailUpdatedEventEventListener);
        eventDispatcher.removeEventListener(PartnerRegisteredEmailFetchedEvent.class, partnerRegisteredEmailEventListener);
    }

    public void setRewardUniqueId(long rewardUniqueId) {
        this.rewardUniqueId = rewardUniqueId;
    }

    public long getUniqueRewardId() {
        return rewardUniqueId;
    }

    public void onUserConfirmsEmailAddress(String userEmailAddress) {
        if (interactor.shouldShowDataSharingConsentForWheelSpinOutcome(rewardUniqueId)) {
            userInterface.showDataSharingConsent();
        } else {
            userInterface.showLoadingIndicator();

            if (userEmailAddress.equals(interactor.getPartnerRegisteredEmail())) {
                interactor.selectVoucher(rewardUniqueId);
            } else {
                interactor.updatePartnerRegisteredEmail(userEmailAddress);
            }
        }
    }

    public interface UserInterface {
        void setRewardData(UnclaimedRewardDTO reward);

        void showLoadingIndicator();

        void hideLoadingIndicator();

        void showPartnerRegisteredEmail(@Nullable String partnerRegisteredEmail);

        void navigateAfterStarbucksRewardConfirmed(long uniqueID);

        void showGenericErrorMessage();

        void showConnectionErrorMessage();

        void showDataSharingConsent();
    }
}
