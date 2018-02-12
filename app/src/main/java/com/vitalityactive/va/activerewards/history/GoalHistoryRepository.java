package com.vitalityactive.va.activerewards.history;

import com.vitalityactive.va.activerewards.dto.GoalTrackerOutDto;
import com.vitalityactive.va.networking.model.goalandprogress.GetGoalProgressAndDetailsResponse;

import java.util.List;

public interface GoalHistoryRepository {
    boolean persistGoalsAndProgress(GetGoalProgressAndDetailsResponse response);
    List<GoalTrackerOutDto> loadGoalProgressAndDetails(String startDate, String endDate);
    boolean doesRangeExistsInDb(String startDate, String endDate);
}
