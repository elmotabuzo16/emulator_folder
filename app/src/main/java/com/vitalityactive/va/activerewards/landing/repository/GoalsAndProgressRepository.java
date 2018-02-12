package com.vitalityactive.va.activerewards.landing.repository;

import android.support.annotation.NonNull;

import com.vitalityactive.va.activerewards.dto.GoalTrackerActiveDto;
import com.vitalityactive.va.activerewards.dto.GoalTrackerOutDto;
import com.vitalityactive.va.networking.model.goalandprogress.GetGoalProgressAndDetailsResponse;

import java.util.List;

public interface GoalsAndProgressRepository {
    boolean persistGoalsAndProgress(GetGoalProgressAndDetailsResponse response);

    GoalTrackerActiveDto getGoalTrackerActive();

    @NonNull
    List<GoalTrackerOutDto> getAllGoalTrackers();

}
