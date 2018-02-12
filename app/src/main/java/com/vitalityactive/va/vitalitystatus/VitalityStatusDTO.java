package com.vitalityactive.va.vitalitystatus;

import com.vitalityactive.va.vitalitystatus.levels.LevelStatusDTO;

import java.util.List;

public class VitalityStatusDTO {
    int totalPoints;
    private int pointsToMaintainStatus;
    private int lowestStatusKey;
    private int daysRemaining;
    private int highestStatusKey;
    private String highestStatusName;
    public LevelStatusDTO nextStatusLevel;
    private LevelStatusDTO currentStatusLevel;
    private LevelStatusDTO carryOverStatusLevel;
    private List<LevelStatusDTO> availableStatuses;
    private final int pointsStatusKey;

    public VitalityStatusDTO(VitalityStatus vitalityStatus,
                             LevelStatusDTO currentLevel,
                             LevelStatusDTO nextLevel,
                             LevelStatusDTO carryOverStatusLevel,
                             List<LevelStatusDTO> availableStatuses,
                             int pointsStatusKey) {
        totalPoints = vitalityStatus.getTotalPoints();
        currentStatusLevel = currentLevel;
        nextStatusLevel = nextLevel;
        pointsToMaintainStatus = vitalityStatus.getPointsToMaintainStatus();
        lowestStatusKey = vitalityStatus.getLowestStatusKey();
        daysRemaining = vitalityStatus.getDaysRemaining();
        highestStatusName = vitalityStatus.getHighestStatusName();
        highestStatusKey = vitalityStatus.getHighestStatusKey();
        this.carryOverStatusLevel = carryOverStatusLevel;

        this.availableStatuses = availableStatuses;
        this.pointsStatusKey = pointsStatusKey;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getPointsToMaintainStatus() {
        return pointsToMaintainStatus;
    }

    public String getNextStatusName() {
        return nextStatusLevel.getName();
    }

    public int getPointsToNextLevel() {
        return nextStatusLevel.getPointsNeeded();
    }

    public int getNextStatusPointsThreshold() {
        return nextStatusLevel.getPointsThreshold();
    }

    public int getProgress() {
        if (getPointsToNextLevel() != 0) {
            float currentLevelPointsRange = totalPoints - currentStatusLevel.getPointsThreshold();
            float nextLevelPointsRange = getNextStatusPointsThreshold() - currentStatusLevel.getPointsThreshold();
            return (int) (currentLevelPointsRange / nextLevelPointsRange * 100);
        }

        return 100;
    }

    public int getLowestStatusKey() {
        return lowestStatusKey;
    }

    public int getDaysRemaining() {
        return daysRemaining;
    }

    public String getHighestStatusName() {
        return highestStatusName;
    }

    public int getHighestStatusKey() {
        return highestStatusKey;
    }

    public List<LevelStatusDTO> getAvailableStatuses() {
        return availableStatuses;
    }

    public LevelStatusDTO getCurrentStatusLevel() {
        return currentStatusLevel;
    }

    LevelStatusDTO getNextStatusLevel() {
        return nextStatusLevel;
    }

    public LevelStatusDTO getCarryOverStatusLevel() {
        return carryOverStatusLevel;
    }

    public String getCurrentStatusLevelName() {
        return currentStatusLevel.getName();
    }

    public int getCurrentStatusLevelKey() {
        return currentStatusLevel.getKey();
    }

    public int getPointsStatusKey() {
        return pointsStatusKey;
    }
}