package com.vitalityactive.va.activerewards.history;

import com.vitalityactive.va.networking.model.goalandprogress.GetGoalProgressAndDetailsResponse;

public interface GoalHistoryInteractor {
    boolean isRequestInProgress();
    void loadGoalProgressAndDetails(String startDate, String endDate, boolean saveResultToDb);
    GetGoalProgressAndDetailsResponse getResponse();
    void stopRequest();
}
