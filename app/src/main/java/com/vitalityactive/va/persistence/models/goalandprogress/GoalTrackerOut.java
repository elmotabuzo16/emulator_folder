package com.vitalityactive.va.persistence.models.goalandprogress;

import android.util.Log;

import com.vitalityactive.va.networking.model.goalandprogress.GetGoalProgressAndDetailsResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.utilities.date.NonUserFacingDateFormatter;

import java.text.ParseException;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GoalTrackerOut extends RealmObject implements Model {
    private RealmList<ObjectiveTrackersGoal> objectiveTrackers;
    private int completedObjectives;
    private String effectiveFrom;
    private Date effectiveFromDate;
    private String effectiveTo;
    private Date effectiveToDate;
    private String goalCode;
    private int goalKey;
    private String goalName;
    private String goalTrackerStatusCode;
    private int goalTrackerStatusKey;
    private String goalTrackerStatusName;
    private String monitorUntil;
    private long partyId;
    private int percentageCompleted;
    private String statusChangedOn;
    private int totalObjectives;
    private boolean isHistory;
    @PrimaryKey
    private int id;

    public GoalTrackerOut() {}

    public GoalTrackerOut(GetGoalProgressAndDetailsResponse.GoalTrackerOut goalTrackerOut) {
        this.objectiveTrackers = new RealmList<>();
        for(GetGoalProgressAndDetailsResponse.ObjectiveTrackersGoal objectiveTrackers : goalTrackerOut.objectiveTrackers){
            this.objectiveTrackers.add(new ObjectiveTrackersGoal(objectiveTrackers));
        }
        this.completedObjectives = goalTrackerOut.completedObjectives;
        this.effectiveFrom = goalTrackerOut.effectiveFrom;
        this.effectiveTo = goalTrackerOut.effectiveTo;
        this.goalCode = goalTrackerOut.goalCode;
        this.goalKey = goalTrackerOut.goalKey;
        this.goalName = goalTrackerOut.goalName;
        this.goalTrackerStatusCode = goalTrackerOut.goalTrackerStatusCode;
        this.goalTrackerStatusKey = goalTrackerOut.goalTrackerStatusKey;
        this.goalTrackerStatusName = goalTrackerOut.goalTrackerStatusName;
        this.monitorUntil = goalTrackerOut.monitorUntil;
        this.partyId = goalTrackerOut.partyId;
        this.percentageCompleted = goalTrackerOut.percentageCompleted;
        this.statusChangedOn = goalTrackerOut.statusChangedOn;
        this.totalObjectives = goalTrackerOut.totalObjectives;
        this.id = goalTrackerOut.id;
        try {
            this.effectiveFromDate = NonUserFacingDateFormatter.javaUtilDateFromYearMonthDayString(this.effectiveFrom);
            this.effectiveToDate = NonUserFacingDateFormatter.javaUtilDateFromYearMonthDayString(this.effectiveTo);
        } catch (ParseException ex) {
            Log.e(NonUserFacingDateFormatter.TAG, "wrong date format: " + ex.toString());
        }
    }

    public RealmList<ObjectiveTrackersGoal> getObjectiveTrackers() {
        return objectiveTrackers;
    }

    public int getCompletedObjectives() {
        return completedObjectives;
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public Date getEffectiveFromDate() {
        return effectiveFromDate;
    }

    public String getEffectiveTo() {
        return effectiveTo;
    }

    public Date getEffectiveToDate() {
        return effectiveToDate;
    }

    public String getGoalCode() {
        return goalCode;
    }

    public int getGoalKey() {
        return goalKey;
    }

    public String getGoalName() {
        return goalName;
    }

    public String getGoalTrackerStatusCode() {
        return goalTrackerStatusCode;
    }

    public int getGoalTrackerStatusKey() {
        return goalTrackerStatusKey;
    }

    public String getGoalTrackerStatusName() {
        return goalTrackerStatusName;
    }

    public String getMonitorUntil() {
        return monitorUntil;
    }

    public long getPartyId() {
        return partyId;
    }

    public int getPercentageCompleted() {
        return percentageCompleted;
    }

    public String getStatusChangedOn() {
        return statusChangedOn;
    }

    public int getTotalObjectives() {
        return totalObjectives;
    }

    public boolean isHistory() {
        return isHistory;
    }

    public void setHistory(boolean history) {
        isHistory = history;
    }
}
