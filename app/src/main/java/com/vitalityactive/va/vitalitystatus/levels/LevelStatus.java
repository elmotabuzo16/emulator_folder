package com.vitalityactive.va.vitalitystatus.levels;

import com.vitalityactive.va.home.service.HomeScreenCardStatusResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

import static android.R.attr.key;

public class LevelStatus extends RealmObject implements Model {
    private int statusKey;
    private String name;
    private int pointsThreshold;
    private int pointsNeeded;
    private int sortOrder;

    public LevelStatus() {}

    public LevelStatus(HomeScreenCardStatusResponse.LevelStatus levelStatus) {
        pointsNeeded = levelStatus.pointsNeeded;
        sortOrder = levelStatus.sortOrder;
        pointsThreshold = levelStatus.statusPoints;
        name = levelStatus.statusName;
        statusKey = levelStatus.statusKey;
    }

    public String getName() {
        return name;
    }

    public int getKey() {
        return statusKey;
    }

    public int getPointsNeeded() {
        return pointsNeeded;
    }

    public int getPointsThreshold() {
        return pointsThreshold;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}
