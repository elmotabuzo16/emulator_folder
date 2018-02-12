package com.vitalityactive.va.activerewards.rewards.presenters;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.activerewards.rewards.ActiveRewardsDataPrivacyUserInterface;
import com.vitalityactive.va.activerewards.rewards.dto.RewardSelectionDTO;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.activerewards.rewards.events.RewardVoucherSelectedEvent;
import com.vitalityactive.va.activerewards.rewards.interactor.RewardsInteractor;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenterImpl;

import javax.inject.Inject;

public class ActiveRewardsDataPrivacyPresenterImpl
        extends TermsAndConditionsPresenterImpl<ActiveRewardsDataPrivacyUserInterface>
        implements ActiveRewardsDataPrivacyPresenter {

    private final RewardsInteractor rewardsInteractor;

    private long rewardUniqueId;
    private int rewardId;
    private RewardSelectionDTO rewardSelection;
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
    public ActiveRewardsDataPrivacyPresenterImpl(final Scheduler scheduler,
                                                 TermsAndConditionsInteractor interactor,
                                                 EventDispatcher eventDispatcher,
                                                 TermsAndConditionsConsenter consenter,
                                                 RewardsInteractor rewardsInteractor) {
        super(scheduler, interactor, consenter, eventDispatcher);

        this.rewardsInteractor = rewardsInteractor;
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
                userInterface.navigateAfterRewardChoiceConfirmed(rewardVoucher.uniqueId);
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
        rewardSelection = rewardsInteractor.getRewardSelection(rewardUniqueId, rewardId);
    }

    @Override
    protected void addEventListeners() {
        super.addEventListeners();

        eventDispatcher.addEventListener(RewardVoucherSelectedEvent.class, rewardSelectedEventListener);
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);

        if (isFinishing) {
            eventDispatcher.removeEventListener(RewardVoucherSelectedEvent.class, rewardSelectedEventListener);
        }
    }

    @Override
    public void onTermsAndConditionsAccepted() {
        userInterface.showLoadingIndicator();
        rewardsInteractor.selectVoucher(rewardUniqueId, rewardSelection.rewardValueLinkId);
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();

        if (rewardSelectedEvent != RewardVoucherSelectedEvent.NONE) {
            userInterface.showLoadingIndicator();
            handleRewardSelectedEvent(rewardSelectedEvent);
        }
    }

    @Override
    public void onBackPressed() {
        configureUserInterfaceForRequestInProgress();
        if (rewardSelection != null) {
            configureUserInterfaceForRequestInProgress();
            didShowRequestErrorMessage = false;
            termsAndConditionsConsenter.disagreeToTermsAndConditions(rewardSelection.rewardName, String.valueOf(rewardSelection.rewardKey), instructionType);
        }
    }

    @Override
    public void setRewardUniqueId(long rewardUniqueId) {
        this.rewardUniqueId = rewardUniqueId;
    }

    @Override
    public void setRewardId(int rewardId) {
        this.rewardId = rewardId;
    }

    @Override
    public void onUserAgreesToTermsAndConditions() {
        if (rewardSelection != null) {
            configureUserInterfaceForRequestInProgress();
            didShowRequestErrorMessage = false;
            termsAndConditionsConsenter.agreeToTermsAndConditions(rewardSelection.rewardName, String.valueOf(rewardSelection.rewardKey), instructionType);
        }
    }

    @Override
    public void setEventSource(String instructionType) {
        this.instructionType = instructionType;
    }
}
