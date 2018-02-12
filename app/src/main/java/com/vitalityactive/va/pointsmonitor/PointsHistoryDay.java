package com.vitalityactive.va.pointsmonitor;

import com.vitalityactive.va.dto.PointsEntryDTO;
import com.vitalityactive.va.utilities.date.Date;

import java.util.List;

public class PointsHistoryDay {
    private List<PointsEntryDTO> pointsEntries;
    private Date date;

    public PointsHistoryDay(Date date, List<PointsEntryDTO> pointsEntries) {
        this.date = date;
        this.pointsEntries = pointsEntries;
    }

    public List<PointsEntryDTO> getPointsEntries() {
        return pointsEntries;
    }

    public Date getDate() {
        return date;
    }
}
