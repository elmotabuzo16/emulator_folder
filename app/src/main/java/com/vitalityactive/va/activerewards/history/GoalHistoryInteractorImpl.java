package com.vitalityactive.va.activerewards.history;

import android.support.annotation.NonNull;

import com.vitalityactive.va.activerewards.landing.service.GoalsProgressServiceClient;
import com.vitalityactive.va.connectivity.ConnectivityListener;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.goalandprogress.GetGoalProgressAndDetailsResponse;

public class GoalHistoryInteractorImpl implements GoalHistoryInteractor {
    private final GoalsProgressServiceClient goalsProgressServiceClient;
    private final EventDispatcher eventDispatcher;
    private final ConnectivityListener connectivityListener;
    private final GoalHistoryRepository goalHistoryRepository;
    private GoalsAndProgressLoadedHistoryEvent loadGoalsResponseEvent = null;

    public GoalHistoryInteractorImpl(@NonNull GoalsProgressServiceClient goalsProgressServiceClient,
                                     @NonNull EventDispatcher eventDispatcher,
                                     @NonNull ConnectivityListener connectivityListener,
                                     @NonNull GoalHistoryRepository goalHistoryRepository) {
        this.goalsProgressServiceClient = goalsProgressServiceClient;
        this.eventDispatcher = eventDispatcher;
        this.connectivityListener = connectivityListener;
        this.goalHistoryRepository = goalHistoryRepository;
    }

    @Override
    public boolean isRequestInProgress() {
        return goalsProgressServiceClient.isRequestInProgress();
    }

    @Override
    public void loadGoalProgressAndDetails(final String startDate, final String endDate, final boolean saveResultToDb) {
        if (connectivityListener.isOnline()) {
            loadGoalsResponseEvent = null;
            goalsProgressServiceClient.getGoalProgressAndDetails(new GoalsAndProgressResponseParser(startDate, endDate, saveResultToDb),
                    startDate, endDate);
        } else {
            passConnectionErrorToDispather(startDate, endDate);
        }
    }

    @Override
    public GetGoalProgressAndDetailsResponse getResponse() {
        return loadGoalsResponseEvent == null ? null : loadGoalsResponseEvent.getResponseBody();
    }

    @Override
    public void stopRequest() {
        goalsProgressServiceClient.stopRequest();
    }

    private void passConnectionErrorToDispather(String startDate, String endDate) {
        loadGoalsResponseEvent = new GoalsAndProgressLoadedHistoryEvent(RequestResult.CONNECTION_ERROR, startDate, endDate);
        eventDispatcher.dispatchEvent(loadGoalsResponseEvent);
    }

    private void passGenericErrorToDispather(String startDate, String endDate) {
        loadGoalsResponseEvent = new GoalsAndProgressLoadedHistoryEvent(RequestResult.GENERIC_ERROR, startDate, endDate);
        eventDispatcher.dispatchEvent(loadGoalsResponseEvent);
    }

    private class GoalsAndProgressResponseParser implements WebServiceResponseParser<GetGoalProgressAndDetailsResponse> {
        private final String effectiveFrom;
        private final String effectiveTo;
        private final boolean saveResultToDb;

        public GoalsAndProgressResponseParser(final String startDate,
                                              final String endDate,
                                              boolean saveResultInDb) {
            this.effectiveFrom = startDate;
            this.effectiveTo = endDate;
            this.saveResultToDb = saveResultInDb;
        }

        @Override
        public void parseResponse(GetGoalProgressAndDetailsResponse response) {
            if (saveResultToDb) {
                goalHistoryRepository.persistGoalsAndProgress(response);
            }
            loadGoalsResponseEvent = new GoalsAndProgressLoadedHistoryEvent(RequestResult.SUCCESSFUL, response, effectiveFrom, effectiveTo);
            eventDispatcher.dispatchEvent(loadGoalsResponseEvent);
        }

        @Override
        public void parseErrorResponse(String errorBody, int code) {
            passGenericErrorToDispather(effectiveFrom, effectiveTo);
        }

        @Override
        public void handleGenericError(Exception exception) {
            passGenericErrorToDispather(effectiveFrom, effectiveTo);
        }

        @Override
        public void handleConnectionError() {
            passConnectionErrorToDispather(effectiveFrom, effectiveTo);
        }
    }
}
