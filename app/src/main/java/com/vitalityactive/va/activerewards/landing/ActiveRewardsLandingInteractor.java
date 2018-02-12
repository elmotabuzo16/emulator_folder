package com.vitalityactive.va.activerewards.landing;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vitalityactive.va.activerewards.dto.GoalTrackerOutDto;
import com.vitalityactive.va.activerewards.landing.events.GoalsAndProgressLoadedLandingEvent;
import com.vitalityactive.va.activerewards.landing.events.LinkedDevicesEvent;
import com.vitalityactive.va.activerewards.landing.repository.GoalsAndProgressRepository;
import com.vitalityactive.va.activerewards.landing.service.GoalsProgressServiceClient;
import com.vitalityactive.va.connectivity.ConnectivityListener;
import com.vitalityactive.va.dependencyinjection.ar.ActiveRewardsScope;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.goalandprogress.GetGoalProgressAndDetailsResponse;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;
import com.vitalityactive.va.wellnessdevices.landing.service.GetFullListResponse;
import com.vitalityactive.va.wellnessdevices.landing.service.WellnessDevicesServiceClient;

import javax.inject.Inject;

@ActiveRewardsScope
public class ActiveRewardsLandingInteractor {
    private final GoalsProgressServiceClient goalsProgressServiceClient;
    private final WellnessDevicesServiceClient wellnessDevicesServiceClient;
    private final EventDispatcher eventDispatcher;
    private final ConnectivityListener connectivityListener;
    private final GoalsAndProgressRepository goalsAndProgressRepository;
    private final DateFormattingUtilities dateFormattingUtilities;

    @Inject
    public ActiveRewardsLandingInteractor(@NonNull GoalsProgressServiceClient goalsProgressServiceClient,
                                          @NonNull WellnessDevicesServiceClient wellnessDevicesServiceClient,
                                          @NonNull EventDispatcher eventDispatcher,
                                          @NonNull ConnectivityListener connectivityListener,
                                          @NonNull GoalsAndProgressRepository goalsAndProgressRepository,
                                          @NonNull DateFormattingUtilities dateFormattingUtilities) {
        this.goalsProgressServiceClient = goalsProgressServiceClient;
        this.wellnessDevicesServiceClient = wellnessDevicesServiceClient;
        this.eventDispatcher = eventDispatcher;
        this.connectivityListener = connectivityListener;
        this.goalsAndProgressRepository = goalsAndProgressRepository;
        this.dateFormattingUtilities = dateFormattingUtilities;
    }

    void checkIfUserHasLinkedDevices() {
        if (wellnessDevicesServiceClient.isRequestInProgress()) {
            return;
        }
        if (connectivityListener.isOnline()) {
            wellnessDevicesServiceClient.getFullList(new FetchDeviceListResponseParser());
        }
    }

    boolean hasCachedGoalProgressAndDetails() {
        return getActiveOrNextGoalTracker() != null;
    }

    @Nullable
    GoalTrackerOutDto getActiveOrNextGoalTracker() {
        LocalDate now = LocalDate.now();
        LocalDate closestEffectiveTo = new LocalDate("9999-01-01");
        GoalTrackerOutDto closestGoalTracker = null;
        for (GoalTrackerOutDto goalTracker : goalsAndProgressRepository.getAllGoalTrackers()) {
            LocalDate other = new LocalDate(goalTracker.getEffectiveTo());
            if (now.compareTo(other) <= 0 && other.compareTo(closestEffectiveTo) <= 0) {
                closestEffectiveTo = other;
                closestGoalTracker = goalTracker;
            }
        }
        return closestGoalTracker;
    }

    boolean isActivityTrackingStarted() {
        GoalTrackerOutDto activeOrNext = getActiveOrNextGoalTracker();
        return activeOrNext != null && TimeUtilities.isTodayInPeriod(new LocalDate(activeOrNext.getEffectiveFrom()), new LocalDate(activeOrNext.getEffectiveTo()));
    }

    @NonNull
    String getFirstGoalStartDate() {
        GoalTrackerOutDto activeOrNextGoalTracker = getActiveOrNextGoalTracker();
        return activeOrNextGoalTracker == null ? "" :
                dateFormattingUtilities.formatWeekdayDateMonthYear(new LocalDate(activeOrNextGoalTracker.getEffectiveFrom()));
    }

    void fetchGoalProgressAndDetails() {
        if (goalsProgressServiceClient.isRequestInProgress()) {
            return;
        }
        if (connectivityListener.isOnline()) {
            goalsProgressServiceClient.getGoalProgressAndDetails(new GoalsAndProgressResponseParser());
        } else {
            passConnectionErrorToDispatcher();
        }
    }

    void cancelGoalProgressAndDetailsRequest() {
        goalsProgressServiceClient.stopRequest();
    }

    private void passConnectionErrorToDispatcher() {
        eventDispatcher.dispatchEvent(new GoalsAndProgressLoadedLandingEvent(RequestResult.CONNECTION_ERROR));
    }

    private void passGenericErrorToDispatcher() {
        eventDispatcher.dispatchEvent(new GoalsAndProgressLoadedLandingEvent(RequestResult.GENERIC_ERROR));
    }

    private class GoalsAndProgressResponseParser implements WebServiceResponseParser<GetGoalProgressAndDetailsResponse> {
        @Override
        public void parseResponse(GetGoalProgressAndDetailsResponse response) {
            goalsAndProgressRepository.persistGoalsAndProgress(response);
            eventDispatcher.dispatchEvent(new GoalsAndProgressLoadedLandingEvent(RequestResult.SUCCESSFUL));
        }

        @Override
        public void parseErrorResponse(String errorBody, int code) {
            passGenericErrorToDispatcher();
        }

        @Override
        public void handleGenericError(Exception exception) {
            passGenericErrorToDispatcher();
        }

        @Override
        public void handleConnectionError() {
            passConnectionErrorToDispatcher();
        }
    }

    private class FetchDeviceListResponseParser implements WebServiceResponseParser<GetFullListResponse> {
        @Override
        public void parseResponse(GetFullListResponse response) {
            if (response.markets == null) {
                eventDispatcher.dispatchEvent(LinkedDevicesEvent.UNKNOWN);
                return;
            }
            for (GetFullListResponse.Market market : response.markets) {
                if (hasLinkedDevice(market)) {
                    eventDispatcher.dispatchEvent(LinkedDevicesEvent.HAS_LINKED_DEVICES);
                    return;
                }
            }
            eventDispatcher.dispatchEvent(LinkedDevicesEvent.DOES_NOT_HAVE_LINKED_DEVICES);
        }

        @Override
        public void parseErrorResponse(String errorBody, int code) {
            eventDispatcher.dispatchEvent(LinkedDevicesEvent.UNKNOWN);
        }

        @Override
        public void handleGenericError(Exception exception) {
            eventDispatcher.dispatchEvent(LinkedDevicesEvent.UNKNOWN);
        }

        @Override
        public void handleConnectionError() {
            eventDispatcher.dispatchEvent(LinkedDevicesEvent.UNKNOWN);
        }

        private boolean hasLinkedDevice(GetFullListResponse.Market market) {
            return !"UNLINKED".equals(market.partner.partnerLinkedStatus);
        }
    }
}
