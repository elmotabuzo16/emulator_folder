package com.vitalityactive.va.vitalitystatus;

import com.vitalityactive.va.home.service.HomeScreenCardStatusResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.vitalitystatus.levels.LevelStatus;

import io.realm.RealmList;
import io.realm.RealmObject;

public class VitalityStatus extends RealmObject implements Model {
    private int pointsStatusKey;
    private RealmList<LevelStatus> availableStatuses;
    private int totalPoints;
    private String currentStatusName;
    private int currentStatusKey;
    private int pointsToMaintainStatus;
    private int nextStatusKey;
    private int lowestStatusKey;
    private int daysRemaining;
    private String highestStatusName;
    private int highestStatusKey;
    private int carryOverStatusKey;

    public VitalityStatus() {
    }

    public VitalityStatus(HomeScreenCardStatusResponse model) {
        totalPoints = model.vitalityStatus.totalPoints;
        currentStatusName = model.vitalityStatus.overallVitalityStatusName;
        currentStatusKey = model.vitalityStatus.overallVitalityStatusKey;
        pointsToMaintainStatus = model.vitalityStatus.pointsToMaintainStatus;
        nextStatusKey = model.vitalityStatus.nextVitalityStatusKey;
        lowestStatusKey = model.vitalityStatus.lowestVitalityStatusKey;
        daysRemaining = model.daysLeftInMembershipPeriod.daysRemaining;
        highestStatusName = model.vitalityStatus.highestVitalityStatusName;
        highestStatusKey = model.vitalityStatus.highestVitalityStatusKey;
        carryOverStatusKey = model.vitalityStatus.carryOverStatusKey;
        pointsStatusKey = model.vitalityStatus.pointsStatusKey;

        if (model.availableStatuses != null) {
            availableStatuses = new RealmList<>();
            for (HomeScreenCardStatusResponse.LevelStatus levelStatus : model.availableStatuses) {
                availableStatuses.add(new LevelStatus(levelStatus));
            }
        }
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public String getCurrentStatusName() {
        return currentStatusName;
    }

    public int getCurrentStatusKey() {
        return currentStatusKey;
    }

    public int getPointsToMaintainStatus() {
        return pointsToMaintainStatus;
    }

    public int getNextStatusKey() {
        return nextStatusKey;
    }

    int getLowestStatusKey() {
        return lowestStatusKey;
    }

    int getDaysRemaining() {
        return daysRemaining;
    }

    String getHighestStatusName() {
        return highestStatusName;
    }

    int getHighestStatusKey() {
        return highestStatusKey;
    }

    public int getCarryOverStatusKey() {
        return carryOverStatusKey;
    }

    public int getPointsStatusKey() {
        return pointsStatusKey;
    }
}

