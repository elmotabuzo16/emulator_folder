package com.vitalityactive.va.dto;

import com.vitalityactive.va.persistence.models.Metadata;
import com.vitalityactive.va.persistence.models.PointsEntry;
import com.vitalityactive.va.utilities.date.Date;

import java.util.ArrayList;
import java.util.List;

public class PointsEntryDTO {
    private final int category;
    private Date effectiveDate;
    private int points;
    private String reason;
    private String progress;
    private String description;
    private String device;
    private String id;
    private List<MetadataDTO> metadata;

    public PointsEntryDTO(PointsEntry pointsEntry) {
        effectiveDate = new Date(pointsEntry.getEffectiveDate());
        points = pointsEntry.getEarnedValue();
        reason = pointsEntry.getReason();
        progress = pointsEntry.getProgress();
        description = constructDescription(pointsEntry);
        device = pointsEntry.getDevice();
        category = pointsEntry.getCategory();
        id = pointsEntry.getId();
        metadata = new ArrayList<>();
        if (pointsEntry.getMetadata() != null) {
            for (Metadata metadata : pointsEntry.getMetadata()) {
                this.metadata.add(new MetadataDTO(metadata));
            }
        }
    }

    private String constructDescription(PointsEntry pointsEntry) {
        return pointsEntry.getTypeName();
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public int getPoints() {
        return points;
    }

    public String getReason() {
        return reason;
    }

    public String getProgress() {
        return progress;
    }

    public String getDescription() {
        return description;
    }

    public String getDevice() {
        return device;
    }

    public int getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public List<MetadataDTO> getMetadata() {
        return metadata;
    }
}
