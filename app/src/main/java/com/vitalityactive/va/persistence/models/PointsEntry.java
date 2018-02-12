package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.constants.EventMetadataType;
import com.vitalityactive.va.networking.model.PointsHistoryServiceResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.utilities.date.Date;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PointsEntry extends RealmObject implements Model {
    private RealmList<Metadata> metadata;
    private String id;
    private int earnedValue;
    private long effectiveDate;
    private String reason;
    private String progress;
    private String device;
    private int category;
    private String typeName;

    public PointsEntry() {

    }

    public PointsEntry(PointsHistoryServiceResponse.PointsEntry pointsEntry) {
        id = pointsEntry.id;
        earnedValue = pointsEntry.earnedValue;
        effectiveDate = new Date(pointsEntry.effectiveDate).getMillisecondsSinceEpoch();
        if (firstValueExists(pointsEntry.reasons)) {
            reason = pointsEntry.reasons.get(0).reason;
        }
        if (firstValueExists(pointsEntry.goalPointsTrackers)) {
            progress = String.valueOf(pointsEntry.goalPointsTrackers.get(0).pointsContributing);
        }
        typeName = pointsEntry.typeName;
        category = pointsEntry.category;
        metadata = new RealmList<>();
        if (pointsEntry.metadatas != null) {
            for (PointsHistoryServiceResponse.Metadata metadata : pointsEntry.metadatas) {
                if (metadata.typeKey.equals(String.valueOf(EventMetadataType._MANUFACTURER))) {
                    this.device = metadata.value;
                } else {
                    this.metadata.add(new Metadata(metadata));
                }
            }
        }
    }

    private <T> boolean firstValueExists(List<T> list) {
        return list != null && !list.isEmpty() && list.get(0) != null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEarnedValue() {
        return earnedValue;
    }

    public long getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(long effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public int getCategory() {
        return category;
    }

    public RealmList<Metadata> getMetadata() {
        return metadata;
    }

    public void setMetadata(RealmList<Metadata> metadata) {
        this.metadata = metadata;
    }

    public String getTypeName() {
        return typeName;
    }
}
