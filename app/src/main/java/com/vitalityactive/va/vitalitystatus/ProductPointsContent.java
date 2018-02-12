package com.vitalityactive.va.vitalitystatus;

public interface ProductPointsContent {
    String getSubtitleString(String pointsEarningFlag, int potentialPoints, boolean pointsLimitReached);

    String getCategoryTitle(int key);

    int getIconResourceId(int key);

    String getFeatureTitle(int key);

    String getPointsStatusMessage(int pointsToMaintainStatus, String currentStatusName, int pointsToNextLevel, String nextStatusLevelName);
}
