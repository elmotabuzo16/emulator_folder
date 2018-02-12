package com.vitalityactive.va.pointsmonitor;

import com.vitalityactive.va.dto.PointsEntryDTO;

public interface PointsEntryRepository {
    PointsEntryDTO getPointsEntry(String id);
}
