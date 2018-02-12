package com.vitalityactive.va.activerewards.landing;

import android.support.annotation.NonNull;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.activerewards.Utils;
import com.vitalityactive.va.activerewards.dto.ActivityItem;
import com.vitalityactive.va.activerewards.dto.GoalTrackerOutDto;
import com.vitalityactive.va.activerewards.dto.ObjectiveTrackersGoalDto;
import com.vitalityactive.va.activerewards.landing.events.GoalsAndProgressLoadedLandingEvent;
import com.vitalityactive.va.activerewards.landing.events.LinkedDevicesEvent;
import com.vitalityactive.va.activerewards.rewards.interactor.RewardsInteractor;
import com.vitalityactive.va.activerewards.rewards.events.CurrentRewardsRequestCompletedEvent;
import com.vitalityactive.va.dependencyinjection.ar.ActiveRewardsScope;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.MeasurementContentFromResourceString;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

@ActiveRewardsScope
class ActiveRewardsLandingPresenter extends BasePresenter<ActiveRewardsLandingPresenter.UserInterface> {

    public interface UserInterface {

        void showLoadingIndicator();
        void hideLoadingIndicator();
        void showConnectionErrorMessage();
        void showGenericErrorMessage();
        void showNotStartedView(String startDate);

        void showLinkDeviceDialog();
        void showGoal(String startDate, String endDate, int pointsTarget, String pointsAchieved, int initialPoints);
        void showUnclaimedRewardsCount(long unclaimedRewardsCount);

    }
    private final ActiveRewardsLandingInteractor interactor;

    private final EventDispatcher eventDispatcher;
    private MainThreadScheduler scheduler;
    private boolean shouldCheckIfUserHasLinkedDevices = false;
    @NonNull
    private LinkedDevicesEvent linkedDevicesCheckResult = LinkedDevicesEvent.UNKNOWN;
    @NonNull
    private RequestResult goalProgressAndDetailsRequestResult = RequestResult.NONE;
    @NonNull
    private WheelInfo currentWheelInfo = WheelInfo.empty;
    private RewardsInteractor rewardsInteractor;
    private MeasurementContentFromResourceString uomStringsProvider;
    private EventListener<GoalsAndProgressLoadedLandingEvent> goalProgressAndDetailsLoadedEventListener = new EventListener<GoalsAndProgressLoadedLandingEvent>() {
        @Override
        public void onEvent(final GoalsAndProgressLoadedLandingEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    if (isUserInterfaceVisible()) {
                        userInterface.hideLoadingIndicator();
                        if (event.isSuccessful()) {
                            showGoalOrNotStartedView();
                        } else if (event.getRequestResult().equals(RequestResult.GENERIC_ERROR)) {
                            userInterface.showGenericErrorMessage();
                        } else if (event.getRequestResult().equals(RequestResult.CONNECTION_ERROR)) {
                            userInterface.showConnectionErrorMessage();
                        }
                    } else {
                        goalProgressAndDetailsRequestResult = event.getRequestResult();
                    }
                }
            });
        }
    };
    private EventListener<LinkedDevicesEvent> linkedDevicesEventListener = new EventListener<LinkedDevicesEvent>() {
        @Override
        public void onEvent(final LinkedDevicesEvent event) {
            if (event == LinkedDevicesEvent.DOES_NOT_HAVE_LINKED_DEVICES) {
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (isUserInterfaceVisible()) {
                            userInterface.showLinkDeviceDialog();
                        } else {
                            linkedDevicesCheckResult = event;
                        }
                    }
                });
            }
        }
    };
    private EventListener<CurrentRewardsRequestCompletedEvent> currentRewardsRequestCompletedEventListener = new EventListener<CurrentRewardsRequestCompletedEvent>() {
        @Override
        public void onEvent(CurrentRewardsRequestCompletedEvent event) {
            if (event == CurrentRewardsRequestCompletedEvent.SUCCESSFUL) {
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (isUserInterfaceVisible()) {
                            updateRewardsBadge();
                        }
                    }
                });
            }
        }
    };

    private void updateRewardsBadge() {
        userInterface.showUnclaimedRewardsCount(rewardsInteractor.getUnclaimedRewardsCount());
    }

    @Inject
    ActiveRewardsLandingPresenter(@NonNull ActiveRewardsLandingInteractor interactor,
                                  @NonNull EventDispatcher eventDispatcher,
                                  @NonNull MainThreadScheduler scheduler,
                                  @NonNull MeasurementContentFromResourceString uomStringsProvider,
                                  @NonNull RewardsInteractor rewardsInteractor) {
        this.interactor = interactor;
        this.eventDispatcher = eventDispatcher;
        this.scheduler = scheduler;
        this.uomStringsProvider = uomStringsProvider;
        this.rewardsInteractor = rewardsInteractor;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        resetWheelInfo();
        if (isNewNavigation) {
            removeEventListeners();
            addEventListeners();
            goalProgressAndDetailsRequestResult = RequestResult.NONE;
            linkedDevicesCheckResult = LinkedDevicesEvent.UNKNOWN;
            if (shouldCheckIfUserHasLinkedDevices) {
                interactor.checkIfUserHasLinkedDevices();
            }
        }
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();

        userInterface.hideLoadingIndicator();

        showGoalOrNotStartedView();
        updateRewardsBadge();
        fetchRewards();

        if (didLastGoalRequestCompleteWithAnError()) {
            showErrorMessage();
        } else {
            showLinkDeviceDialogIfNeeded();
            fetchGoalProgressAndDetails();
        }

    }

    private void showGoalOrNotStartedView() {
        if (interactor.isActivityTrackingStarted() || noCachedContent()) {
            showGoal();
        } else {
            resetWheelInfo();
            userInterface.showNotStartedView(interactor.getFirstGoalStartDate());
        }
    }

    private void resetWheelInfo() {
        currentWheelInfo = WheelInfo.empty;
    }

    private void fetchRewards() {
        rewardsInteractor.fetchCurrentRewards();
    }

    private void showLinkDeviceDialogIfNeeded() {
        if (noCachedContent()) {
            return;
        }
        if (linkedDevicesCheckResult == LinkedDevicesEvent.DOES_NOT_HAVE_LINKED_DEVICES) {
            userInterface.showLinkDeviceDialog();
            linkedDevicesCheckResult = LinkedDevicesEvent.UNKNOWN;
        }
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);
        if (isFinishing) {
            removeEventListeners();
        }
    }

    private void showErrorMessage() {
        switch (goalProgressAndDetailsRequestResult) {
            case GENERIC_ERROR:
                userInterface.showGenericErrorMessage();
                break;
            case CONNECTION_ERROR:
                userInterface.showConnectionErrorMessage();
                break;
        }
        goalProgressAndDetailsRequestResult = RequestResult.NONE;
    }

    private void showGoal() {
        GoalTrackerOutDto activeOrNextGoalTracker = interactor.getActiveOrNextGoalTracker();
        WheelInfo newWheelInfo = new WheelInfo(getStartDate(activeOrNextGoalTracker), getEndDate(activeOrNextGoalTracker), getPointsTarget(activeOrNextGoalTracker), getPointsAchieved(activeOrNextGoalTracker));
        if (currentWheelInfo == WheelInfo.empty || !newWheelInfo.equals(currentWheelInfo)) {
            int initialPoints = getInitialPoints();
            currentWheelInfo = newWheelInfo;
            userInterface.showGoal(currentWheelInfo.getStartDate(), currentWheelInfo.getEndDate(),
                    currentWheelInfo.getPointsTarget(),
                    currentWheelInfo.getPointsAchieved(),
                    initialPoints);
        }
    }

    private String getStartDate(GoalTrackerOutDto activeOrNextGoalTracker) {
        return activeOrNextGoalTracker == null ? "" : activeOrNextGoalTracker.getEffectiveFrom();
    }

    private String getEndDate(GoalTrackerOutDto activeOrNextGoalTracker) {
        return activeOrNextGoalTracker == null ? "" : activeOrNextGoalTracker.getEffectiveTo();
    }

    private int getInitialPoints() {
        return Integer.valueOf(currentWheelInfo.getPointsAchieved());
    }

    private boolean didLastGoalRequestCompleteWithAnError() {
        return goalProgressAndDetailsRequestResult != RequestResult.NONE &&
                goalProgressAndDetailsRequestResult != RequestResult.SUCCESSFUL;
    }

    private boolean noCachedContent() {
        return !interactor.hasCachedGoalProgressAndDetails();
    }

    void fetchGoalProgressAndDetails() {
        interactor.fetchGoalProgressAndDetails();
    }

    void onSwipeToRefresh() {
        interactor.fetchGoalProgressAndDetails();
    }

    void cancelGoalProgressAndDetailsRequest() {
        interactor.cancelGoalProgressAndDetailsRequest();
    }

    void setShouldCheckIfUserHasLinkedDevices(boolean shouldCheckIfUserHasLinkedDevices) {
        this.shouldCheckIfUserHasLinkedDevices = shouldCheckIfUserHasLinkedDevices;
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(GoalsAndProgressLoadedLandingEvent.class, goalProgressAndDetailsLoadedEventListener);
        eventDispatcher.addEventListener(LinkedDevicesEvent.class, linkedDevicesEventListener);
        eventDispatcher.addEventListener(CurrentRewardsRequestCompletedEvent.class, currentRewardsRequestCompletedEventListener);
    }

    private void removeEventListeners() {
        eventDispatcher.removeEventListener(GoalsAndProgressLoadedLandingEvent.class, goalProgressAndDetailsLoadedEventListener);
        eventDispatcher.removeEventListener(LinkedDevicesEvent.class, linkedDevicesEventListener);
        eventDispatcher.removeEventListener(CurrentRewardsRequestCompletedEvent.class, currentRewardsRequestCompletedEventListener);
    }

    List<ActivityItem> getActivityList() {
        GoalTrackerOutDto goalTrackerOutDto = interactor.getActiveOrNextGoalTracker();

        return Utils.getActivityListWithUom(uomStringsProvider, goalTrackerOutDto);
    }

    private int getPointsTarget(GoalTrackerOutDto goalTrackerOut) {
        if (goalTrackerOut == null) {
            return 0;
        }

        List<ObjectiveTrackersGoalDto> objectiveTrackers = goalTrackerOut.getObjectiveTrackers();

        int totalPointsTarget = 0;
        for (ObjectiveTrackersGoalDto item : objectiveTrackers) {
            totalPointsTarget += item.getPointsTarget();
        }
        return totalPointsTarget;
    }

    private String getPointsAchieved(GoalTrackerOutDto goalTrackerOut) {
        if (goalTrackerOut == null) {
            return "0";
        }
        List<ObjectiveTrackersGoalDto> objectiveTrackers = goalTrackerOut.getObjectiveTrackers();
        int totalPointsAchieved = 0;
        for (ObjectiveTrackersGoalDto item : objectiveTrackers) {
            totalPointsAchieved += item.getPointsAchieved();
        }
        return String.valueOf(totalPointsAchieved);
    }

    private static class WheelInfo {
        public static final WheelInfo empty = new WheelInfo("", "", 0, "0");
        private final String startDate;
        final String endDate;
        final int pointsTarget;
        final String pointsAchieved;

        WheelInfo(String startDate, String endDate,
                  int pointsTarget,
                  String pointsAchieved) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.pointsTarget = pointsTarget;
            this.pointsAchieved = pointsAchieved;
        }

        String getEndDate() {
            return endDate;
        }

        public int getPointsTarget() {
            return pointsTarget;
        }

        public String getPointsAchieved() {
            return pointsAchieved;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj != null && obj instanceof WheelInfo) {
                final WheelInfo other = (WheelInfo) obj;
                return this.startDate.equals(other.startDate) &&
                        this.endDate.equals(other.endDate) &&
                        this.pointsTarget == other.pointsTarget &&
                        this.pointsAchieved.equals(other.pointsAchieved);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(startDate, endDate, pointsTarget, pointsAchieved);
        }

        public String getStartDate() {
            return startDate;
        }
    }
}
