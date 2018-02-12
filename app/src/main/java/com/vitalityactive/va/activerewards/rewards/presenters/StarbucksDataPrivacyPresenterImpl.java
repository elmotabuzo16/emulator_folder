package com.vitalityactive.va.activerewards.rewards.presenters;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.activerewards.rewards.StarbucksDataPrivacyUserInterface;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.activerewards.rewards.dto.UnclaimedRewardDTO;
import com.vitalityactive.va.activerewards.rewards.events.PartnerRegisteredEmailUpdatedEvent;
import com.vitalityactive.va.activerewards.rewards.events.RewardVoucherSelectedEvent;
import com.vitalityactive.va.activerewards.rewards.interactor.RewardsInteractor;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenterImpl;

import javax.inject.Inject;

public class StarbucksDataPrivacyPresenterImpl
        extends TermsAndConditionsPresenterImpl<StarbucksDataPrivacyUserInterface>
        implements StarbucksDataPrivacyPresenter {

    private final RewardsInteractor rewardsInteractor;

    private long rewardUniqueId;
    private PartnerRegisteredEmailUpdatedEvent emailUpdatedEvent = PartnerRegisteredEmailUpdatedEvent.NONE;
    private EventListener<PartnerRegisteredEmailUpdatedEvent> registeredEmailUpdateEventListener = new EventListener<PartnerRegisteredEmailUpdatedEvent>() {
        @Override
        public void onEvent(final PartnerRegisteredEmailUpdatedEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    if (isUserInterfaceVisible()) {
                        handleEmailUpdatedEvent(event);
                    } else {
                        emailUpdatedEvent = event;
                    }
                }
            });
        }
    };
    private String userEmailAddress;
    private RewardVoucherSelectedEvent rewardSelectedEvent = RewardVoucherSelectedEvent.NONE;
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
    private String instructionType;

    @Inject
    public StarbucksDataPrivacyPresenterImpl(final Scheduler scheduler,
                                             TermsAndConditionsInteractor interactor,
                                             EventDispatcher eventDispatcher,
                                             TermsAndConditionsConsenter consenter,
                                             RewardsInteractor rewardsInteractor) {
        super(scheduler, interactor, consenter, eventDispatcher);

        this.rewardsInteractor = rewardsInteractor;
    }

    private void handleEmailUpdatedEvent(PartnerRegisteredEmailUpdatedEvent event) {
        if (event == PartnerRegisteredEmailUpdatedEvent.SUCCESSFUL) {
            rewardsInteractor.selectVoucher(rewardUniqueId);
        } else if (event == PartnerRegisteredEmailUpdatedEvent.CONNECTION_ERROR) {
            userInterface.showConnectionErrorMessage();
        } else {
            userInterface.showGenericErrorMessage();
        }

        emailUpdatedEvent = PartnerRegisteredEmailUpdatedEvent.NONE;
    }

    private void handleRewardSelectedEvent(RewardVoucherSelectedEvent event) {
        if (event == RewardVoucherSelectedEvent.GENERIC_ERROR) {
            userInterface.showGenericErrorMessage();
        } else if (event == RewardVoucherSelectedEvent.CONNECTION_ERROR) {
            userInterface.showConnectionErrorMessage();
        } else {
            RewardVoucherDTO rewardVoucher = rewardsInteractor.getRewardVoucherById(event.getRewardUniqueId());
            if (rewardVoucher == null) {
                userInterface.showGenericErrorMessage();
            } else {
                userInterface.navigateAfterStarbucksRewardConfirmed(rewardVoucher.uniqueId);
            }
        }

        rewardSelectedEvent = RewardVoucherSelectedEvent.NONE;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        super.onUserInterfaceCreated(isNewNavigation);

        if (isNewNavigation) {
            addEventListeners();
        }
    }

    @Override
    protected void addEventListeners() {
        super.addEventListeners();

        eventDispatcher.addEventListener(RewardVoucherSelectedEvent.class, rewardSelectedEventListener);
        eventDispatcher.addEventListener(PartnerRegisteredEmailUpdatedEvent.class, registeredEmailUpdateEventListener);
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);

        if (isFinishing) {
            eventDispatcher.removeEventListener(RewardVoucherSelectedEvent.class, rewardSelectedEventListener);
            eventDispatcher.removeEventListener(PartnerRegisteredEmailUpdatedEvent.class, registeredEmailUpdateEventListener);
        }
    }

    @Override
    public void onTermsAndConditionsAccepted() {
        navigateAfterTermsAndConditionsAccepted();
        userInterface.showLoadingIndicator();
        rewardsInteractor.updatePartnerRegisteredEmail(userEmailAddress);
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();

        if (emailUpdatedEvent != PartnerRegisteredEmailUpdatedEvent.NONE) {
            userInterface.showLoadingIndicator();
            handleEmailUpdatedEvent(emailUpdatedEvent);
        } else if (rewardSelectedEvent != RewardVoucherSelectedEvent.NONE) {
            userInterface.showLoadingIndicator();
            handleRewardSelectedEvent(rewardSelectedEvent);
        }
    }

    @Override
    public void onBackPressed() {
        configureUserInterfaceForRequestInProgress();
        UnclaimedRewardDTO wheelSpin = rewardsInteractor.getWheelSpin(rewardUniqueId);
        if (wheelSpin != null) {
            configureUserInterfaceForRequestInProgress();
            didShowRequestErrorMessage = false;
            termsAndConditionsConsenter.disagreeToTermsAndConditions(wheelSpin.name, String.valueOf(wheelSpin.outcomeRewardKey), instructionType);
        }
    }

    @Override
    public void setRewardUniqueId(long rewardUniqueId) {
        this.rewardUniqueId = rewardUniqueId;
    }

    @Override
    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    @Override
    public void onUserAgreesToTermsAndConditions() {
        UnclaimedRewardDTO wheelSpin = rewardsInteractor.getWheelSpin(rewardUniqueId);
        if (wheelSpin != null) {
            configureUserInterfaceForRequestInProgress();
            didShowRequestErrorMessage = false;
            termsAndConditionsConsenter.agreeToTermsAndConditions(wheelSpin.name, String.valueOf(wheelSpin.outcomeRewardKey), instructionType);
        }
    }

    @Override
    public void setEventSource(String instructionType) {
        this.instructionType = instructionType;
    }
}
