package com.vitalityactive.va.pointsmonitor;

public interface PointsMonitorInteractor {

    boolean hasPointsEntries();

    boolean isFetchingPointsHistory();

    void fetchPointsHistory();

    void refreshPointsHistory();

}
