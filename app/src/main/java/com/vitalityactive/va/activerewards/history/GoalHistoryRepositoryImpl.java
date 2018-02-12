package com.vitalityactive.va.activerewards.history;

import android.support.annotation.NonNull;
import android.util.Log;

import com.vitalityactive.va.activerewards.dto.GoalTrackerOutDto;
import com.vitalityactive.va.networking.model.goalandprogress.GetGoalProgressAndDetailsResponse;
import com.vitalityactive.va.networking.parsing.Persister;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.persistence.models.goalandprogress.GoalTrackerOut;
import com.vitalityactive.va.utilities.date.NonUserFacingDateFormatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmQuery;

public class GoalHistoryRepositoryImpl implements GoalHistoryRepository {
    private final DataStore dataStore;
    private final Persister persister;

    public GoalHistoryRepositoryImpl(DataStore dataStore) {
        this.dataStore = dataStore;
        this.persister = new Persister(dataStore);
    }

    @Override
    public boolean persistGoalsAndProgress(@NonNull GetGoalProgressAndDetailsResponse response) {
        if(response != null && response.getGoalProgressAndDetailsResponse != null &&
                response.getGoalProgressAndDetailsResponse.goalTrackerOuts != null) {
            persister.addOrUpdateModels(response.getGoalProgressAndDetailsResponse.goalTrackerOuts, new Persister.InstanceCreator<Model, GetGoalProgressAndDetailsResponse.GoalTrackerOut>() {
                @Override
                public Model create(GetGoalProgressAndDetailsResponse.GoalTrackerOut goalTrackerOut) {
                    return new GoalTrackerOut(goalTrackerOut);
                }
            });
        }
        return true;
    }

    @Override
    public List<GoalTrackerOutDto> loadGoalProgressAndDetails(String startDateStr, String endDateStr) {
        // This is not going to be replaced with DateUtils as this is data for backend and shouldn't depend on user's locale
        try {
            final Date startDate = NonUserFacingDateFormatter.javaUtilDateFromYearMonthDayString(startDateStr);
            final Date endDate = NonUserFacingDateFormatter.javaUtilDateFromYearMonthDayString(endDateStr);

        return dataStore.getModels(GoalTrackerOut.class,
                new DataStore.QueryExecutor<GoalTrackerOut, RealmQuery<GoalTrackerOut>>() {
                    @Override
                    public List<GoalTrackerOut> executeQueries(RealmQuery<GoalTrackerOut> initialQuery) {
                        return initialQuery
                                .between("effectiveFromDate", startDate, endDate)
                                .between("effectiveToDate", startDate, endDate)
                                .findAll();
                    }
                },
                new DataStore.ModelListMapper<GoalTrackerOut, GoalTrackerOutDto>() {
                    @Override
                    public List<GoalTrackerOutDto> mapModels(List<GoalTrackerOut> models) {
                        List<GoalTrackerOutDto> mappedModels = new ArrayList<>();
                        for (GoalTrackerOut goalTrackerOut : models) {
                            mappedModels.add(new GoalTrackerOutDto(goalTrackerOut));
                        }
                        return mappedModels;
                    }
                });
        } catch (ParseException ex) {
            Log.e("AR_HISTORY", "wrong date format: " + ex.toString());
            return null;
        }
    }

    @Override
    public boolean doesRangeExistsInDb(String startDate, String endDate) {
        List<GoalTrackerOutDto> list = loadGoalProgressAndDetails(startDate, endDate);
        return list != null && list.size() > 0;
    }
}