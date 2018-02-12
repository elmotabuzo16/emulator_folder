package com.vitalityactive.va.persistence.models.goalandprogress;

import com.vitalityactive.va.persistence.Model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ObjectivePointsEntries extends RealmObject implements Model {

    private RealmList<EntryMetadata> pointsEntryMetadatas;
    private String categoryCode;
    private int categoryKey;
    private String categoryName;
    private int earnedValue;
    private String effectiveDate;
    private int eventId;
    @PrimaryKey
    private int id;
    private long partyId;
    private int pointsContributed;
    private int potentialValue;
    private int prelimitValue;
    private String statusChangeDate;
    private String statusTypeCode;
    private int statusTypeKey;
    private String statusTypeName;
    private String systemAwareOn;
    private String typeCode;
    private int typeKey;
    private String typeName;
    private RealmList<GoalProgressReasonRealm> reasons;

    public ObjectivePointsEntries() {}

    public ObjectivePointsEntries(com.vitalityactive.va.networking.model.goalandprogress.ObjectivePointsEntries objectivePointsEntries) {
        this.pointsEntryMetadatas = new RealmList<>();
        if(objectivePointsEntries.pointsEntryMetadatas != null &&
                !objectivePointsEntries.pointsEntryMetadatas.isEmpty()) {
            for (com.vitalityactive.va.networking.model.goalandprogress.EntryMetadata item : objectivePointsEntries.pointsEntryMetadatas) {
                this.pointsEntryMetadatas.add(new EntryMetadata(item));
            }
        }
        this.categoryCode = objectivePointsEntries.categoryCode;
        this.categoryKey = objectivePointsEntries.categoryKey;
        this.categoryName = objectivePointsEntries.categoryName;
        this.earnedValue = objectivePointsEntries.earnedValue;
        this.effectiveDate = objectivePointsEntries.effectiveDate;
        this.eventId = objectivePointsEntries.eventId;
        this.id = objectivePointsEntries.id;
        this.partyId = objectivePointsEntries.partyId;
        this.pointsContributed = objectivePointsEntries.pointsContributed;
        this.potentialValue = objectivePointsEntries.potentialValue;
        this.prelimitValue = objectivePointsEntries.prelimitValue;
        this.statusChangeDate = objectivePointsEntries.statusChangeDate;
        this.statusTypeCode = objectivePointsEntries.statusTypeCode;
        this.statusTypeKey = objectivePointsEntries.statusTypeKey;
        this.statusTypeName = objectivePointsEntries.statusTypeName;
        this.systemAwareOn = objectivePointsEntries.systemAwareOn;
        this.typeCode = objectivePointsEntries.typeCode;
        this.typeKey = objectivePointsEntries.typeKey;
        this.typeName = objectivePointsEntries.typeName;

        this.reasons = new RealmList<>();
        if(objectivePointsEntries.reason != null &&
                !objectivePointsEntries.reason.isEmpty()) {
            for (com.vitalityactive.va.networking.model.goalandprogress.Reason reason : objectivePointsEntries.reason) {
                this.reasons.add(new GoalProgressReasonRealm(reason));
            }
        }
    }

    public RealmList<EntryMetadata> getPointsEntryMetadatas() {
        return pointsEntryMetadatas;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public int getCategoryKey() {
        return categoryKey;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getEarnedValue() {
        return earnedValue;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public int getEventId() {
        return eventId;
    }

    public int getId() {
        return id;
    }

    public long getPartyId() {
        return partyId;
    }

    public int getPointsContributed() {
        return pointsContributed;
    }

    public int getPotentialValue() {
        return potentialValue;
    }

    public int getPrelimitValue() {
        return prelimitValue;
    }

    public String getStatusChangeDate() {
        return statusChangeDate;
    }

    public String getStatusTypeCode() {
        return statusTypeCode;
    }

    public int getStatusTypeKey() {
        return statusTypeKey;
    }

    public String getStatusTypeName() {
        return statusTypeName;
    }

    public String getSystemAwareOn() {
        return systemAwareOn;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public String getTypeName() {
        return typeName;
    }

    public RealmList<GoalProgressReasonRealm> getReasons() {
        return reasons;
    }
}
