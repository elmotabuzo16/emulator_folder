package com.vitalityactive.va.activerewards.rewards.presenters;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.activerewards.rewards.events.RewardVoucherSelectedEvent;
import com.vitalityactive.va.activerewards.rewards.interactor.RewardsInteractor;
import com.vitalityactive.va.dependencyinjection.ar.ActiveRewardsScope;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.utilities.PackageManagerUtilities;

import javax.inject.Inject;

import static com.vitalityactive.va.constants.RewardId._STARBUCKSVOUCHER;

@ActiveRewardsScope
public class RewardVoucherPresenter extends BasePresenter<RewardVoucherPresenter.UserInterface> {
    private final String starbucksPackageName = "com.starbucks.mobilecard";
    private PackageManagerUtilities packageManagerUtilities;
    private RewardsInteractor interactor;
    private EventDispatcher eventDispatcher;
    private MainThreadScheduler scheduler;
    private UserInterface userInterface;
    private long uniqueId;
    private RewardVoucherSelectedEvent rewardVoucherSelectedEvent = RewardVoucherSelectedEvent.NONE;
    private EventListener<RewardVoucherSelectedEvent> voucherSelectedEventListener = new EventListener<RewardVoucherSelectedEvent>() {
        @Override
        public void onEvent(final RewardVoucherSelectedEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    if (isUserInterfaceVisible()) {
                        handleRewardVoucherSelectedEvent(event);
                    } else {
                        rewardVoucherSelectedEvent = event;
                    }
                }
            });
        }
    };

    @Inject
    RewardVoucherPresenter(PackageManagerUtilities packageManagerUtilities,
                           RewardsInteractor interactor,
                           EventDispatcher eventDispatcher,
                           MainThreadScheduler scheduler) {
        this.packageManagerUtilities = packageManagerUtilities;
        this.interactor = interactor;
        this.eventDispatcher = eventDispatcher;
        this.scheduler = scheduler;
    }

    private void handleRewardVoucherSelectedEvent(RewardVoucherSelectedEvent event) {
        if (event.equals(RewardVoucherSelectedEvent.GENERIC_ERROR)) {
            userInterface.showGenericError();
        } else if (event.equals(RewardVoucherSelectedEvent.CONNECTION_ERROR)) {
            userInterface.showConnectionError();
        } else {
            RewardVoucherDTO rewardVoucherDTO = interactor.getRewardVoucherById(event.getRewardUniqueId());
            if (rewardVoucherDTO == null) {
                userInterface.showGenericError();
            } else {
                handleVoucherDisplay(rewardVoucherDTO);
            }
        }

        rewardVoucherSelectedEvent = RewardVoucherSelectedEvent.NONE;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        if (isNewNavigation) {
            removeEventListeners();
            addEventListeners();
            rewardVoucherSelectedEvent = RewardVoucherSelectedEvent.NONE;
        }
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();

        if (rewardVoucherSelectedEvent == RewardVoucherSelectedEvent.NONE) {
            RewardVoucherDTO rewardVoucherDTO = interactor.getRewardVoucherById(uniqueId);

            if (rewardVoucherDTO == null) {
                fetchRewardVoucher();
            } else {
                handleVoucherDisplay(rewardVoucherDTO);
            }
        } else {
            handleRewardVoucherSelectedEvent(rewardVoucherSelectedEvent);
        }
    }

    private void handleVoucherDisplay(RewardVoucherDTO rewardVoucherDTO) {
        userInterface.hideLoadingIndicator();

        if (rewardVoucherDTO.issueFailed) {
            userInterface.showNotAwardedScreen(rewardVoucherDTO);
        } else if (rewardVoucherDTO.showCodePending) {
            userInterface.showPendingRewardState(rewardVoucherDTO);
        } else if (rewardVoucherDTO.rewardId == _STARBUCKSVOUCHER) {
            userInterface.showStarbucksAvailableRewardState(rewardVoucherDTO);
            if (packageManagerUtilities.isPackageInstalled(starbucksPackageName)) {
                userInterface.displayAppOpen();
            } else {
                userInterface.displayAppInstall();
            }
        } else {
            userInterface.showVoucherNumberState(rewardVoucherDTO);
        }
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(RewardVoucherSelectedEvent.class, voucherSelectedEventListener);
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);

        if (isFinishing) {
            removeEventListeners();
        }
    }

    private void removeEventListeners() {
        eventDispatcher.removeEventListener(RewardVoucherSelectedEvent.class, voucherSelectedEventListener);
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void handleInstallOrOpenApp() {
        if (packageManagerUtilities.isPackageInstalled(starbucksPackageName)) {
            userInterface.launchApplication(starbucksPackageName);
        } else {
            packageManagerUtilities.installPackageFromPlayStore(starbucksPackageName);
        }
    }

    public void setUniqueId(long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void fetchRewardVoucher() {
        userInterface.showLoadingIndicator();
        interactor.fetchRewardVoucher(uniqueId);
    }

    public interface UserInterface {
        void showPendingRewardState(RewardVoucherDTO rewardVoucherDTO);

        void showStarbucksAvailableRewardState(RewardVoucherDTO rewardVoucherDTO);

        void showNotAwardedScreen(RewardVoucherDTO rewardVoucherDTO);

        void showVoucherNumberState(RewardVoucherDTO rewardVoucherDTO);

        void launchApplication(String packageName);

        void displayAppOpen();

        void displayAppInstall();

        void showLoadingIndicator();

        void hideLoadingIndicator();

        void showGenericError();

        void showConnectionError();
    }
}
