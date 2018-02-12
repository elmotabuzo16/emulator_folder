package com.vitalityactive.va.pointsmonitor;

import com.vitalityactive.va.dto.PointsEntryCategoryDTO;

import java.util.List;

public interface PointsMonitorAvailablePointsCategoriesProvider {

    List<PointsEntryCategoryDTO> getPointsEntryCategories();
}
