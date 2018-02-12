package com.vitalityactive.va.activerewards.landing.repository;

import android.support.annotation.NonNull;

import com.vitalityactive.va.activerewards.dto.GoalTrackerActiveDto;
import com.vitalityactive.va.activerewards.dto.GoalTrackerOutDto;
import com.vitalityactive.va.networking.model.goalandprogress.GetGoalProgressAndDetailsResponse;
import com.vitalityactive.va.networking.parsing.Persister;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.persistence.models.ar.GoalTrackerActive;
import com.vitalityactive.va.persistence.models.goalandprogress.EntryMetadata;
import com.vitalityactive.va.persistence.models.goalandprogress.GoalProgressAndDetailsOutbound;
import com.vitalityactive.va.persistence.models.goalandprogress.GoalTrackerOut;
import com.vitalityactive.va.persistence.models.goalandprogress.ObjectivePointsEntries;
import com.vitalityactive.va.persistence.models.goalandprogress.ObjectiveTrackersGoal;

import java.util.ArrayList;
import java.util.List;

public class GoalsAndProgressRepositoryImpl implements GoalsAndProgressRepository {
    private final DataStore dataStore;
    private final Persister persister;

    public GoalsAndProgressRepositoryImpl(DataStore dataStore) {
        this.dataStore = dataStore;
        this.persister = new Persister(dataStore);
    }

    @Override
    public boolean persistGoalsAndProgress(GetGoalProgressAndDetailsResponse response) {
        if (response.getGoalProgressAndDetailsResponse == null ||
                response.getGoalProgressAndDetailsResponse.goalTrackerOuts == null) {
            return false;
        }
        
        dataStore.removeAll(GoalTrackerOut.class);
        dataStore.removeAll(ObjectiveTrackersGoal.class);
        dataStore.removeAll(GoalProgressAndDetailsOutbound.class);
        dataStore.removeAll(ObjectivePointsEntries.class);
        dataStore.removeAll(EntryMetadata.class);

        persister.addOrUpdateModels(response.getGoalProgressAndDetailsResponse.goalTrackerOuts,
                new Persister.InstanceCreator<Model, GetGoalProgressAndDetailsResponse.GoalTrackerOut>() {
                    @Override
                    public Model create(GetGoalProgressAndDetailsResponse.GoalTrackerOut response) {
                        return new GoalTrackerOut(response);
                    }
                });
        return true;
    }

    @Override
    public GoalTrackerActiveDto getGoalTrackerActive() {
        if (dataStore.hasModelInstance(GoalTrackerActive.class)) {
            return dataStore.getFirstModelInstance(GoalTrackerActive.class, new DataStore.ModelMapper<GoalTrackerActive, GoalTrackerActiveDto>() {
                @Override
                public GoalTrackerActiveDto mapModel(GoalTrackerActive model) {
                    return new GoalTrackerActiveDto(model);
                }
            });
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    public List<GoalTrackerOutDto> getAllGoalTrackers() {
        return dataStore.getModels(GoalTrackerOut.class, new DataStore.ModelListMapper<GoalTrackerOut, GoalTrackerOutDto>() {
            @Override
            public List<GoalTrackerOutDto> mapModels(List<GoalTrackerOut> models) {
                List<GoalTrackerOutDto> goalTrackerDTOs = new ArrayList<>();
                for (GoalTrackerOut goalTrackerModel : models) {
                    goalTrackerDTOs.add(new GoalTrackerOutDto(goalTrackerModel));
                }
                return goalTrackerDTOs;
            }
        });
    }

}
