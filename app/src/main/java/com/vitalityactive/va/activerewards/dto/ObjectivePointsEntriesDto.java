package com.vitalityactive.va.activerewards.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.vitalityactive.va.networking.model.goalandprogress.Reason;
import com.vitalityactive.va.persistence.models.goalandprogress.EntryMetadata;
import com.vitalityactive.va.persistence.models.goalandprogress.GoalProgressReasonRealm;
import com.vitalityactive.va.persistence.models.goalandprogress.ObjectivePointsEntries;

import java.util.ArrayList;
import java.util.List;

public class ObjectivePointsEntriesDto implements Parcelable {

    private List<EntryMetadataDto> pointsEntryMetadatas;
    private String categoryCode;
    private int categoryKey;
    private String categoryName;
    private int earnedValue;
    private String effectiveDate;
    private int eventId;
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
    private List<GoalProgressReasonDto> reasons;

    public ObjectivePointsEntriesDto(ObjectivePointsEntries src) {
        this.pointsEntryMetadatas = new ArrayList<>();
        if(src.getPointsEntryMetadatas() != null && !src.getPointsEntryMetadatas().isEmpty()) {
            for (EntryMetadata entryMetadata : src.getPointsEntryMetadatas()) {
                this.pointsEntryMetadatas.add(new EntryMetadataDto(entryMetadata));
            }
        }
        this.categoryCode = src.getCategoryCode();
        this.categoryKey = src.getCategoryKey();
        this.categoryName = src.getCategoryName();
        this.earnedValue = src.getEarnedValue();
        this.effectiveDate = src.getEffectiveDate();
        this.eventId = src.getEventId();
        this.id = src.getId();
        this.partyId = src.getPartyId();
        this.pointsContributed = src.getPointsContributed();
        this.potentialValue = src.getPotentialValue();
        this.prelimitValue = src.getPrelimitValue();
        this.statusChangeDate = src.getStatusChangeDate();
        this.statusTypeCode = src.getStatusTypeCode();
        this.statusTypeKey = src.getStatusTypeKey();
        this.statusTypeName = src.getStatusTypeName();
        this.systemAwareOn = src.getSystemAwareOn();
        this.typeCode = src.getTypeCode();
        this.typeKey = src.getTypeKey();
        this.typeName = src.getTypeName();

        this.reasons = new ArrayList<>();
        if(src.getReasons() != null && !src.getReasons().isEmpty()) {
            for (GoalProgressReasonRealm reason : src.getReasons()) {
                this.reasons.add(new GoalProgressReasonDto(reason));
            }
        }
    }

    public ObjectivePointsEntriesDto(com.vitalityactive.va.networking.model.goalandprogress.ObjectivePointsEntries src) {
        this.pointsEntryMetadatas = new ArrayList<>();
        for(com.vitalityactive.va.networking.model.goalandprogress.EntryMetadata entryMetadata : src.pointsEntryMetadatas){
            this.pointsEntryMetadatas.add(new EntryMetadataDto(entryMetadata));
        }
        this.categoryCode = src.categoryCode;
        this.categoryKey = src.categoryKey;
        this.categoryName = src.categoryName;
        this.earnedValue = src.earnedValue;
        this.effectiveDate = src.effectiveDate;
        this.eventId = src.eventId;
        this.id = src.id;
        this.partyId = src.partyId;
        this.pointsContributed = src.pointsContributed;
        this.potentialValue = src.potentialValue;
        this.prelimitValue = src.prelimitValue;
        this.statusChangeDate = src.statusChangeDate;
        this.statusTypeCode = src.statusTypeCode;
        this.statusTypeKey = src.statusTypeKey;
        this.statusTypeName = src.statusTypeName;
        this.systemAwareOn = src.systemAwareOn;
        this.typeCode = src.typeCode;
        this.typeKey = src.typeKey;
        this.typeName = src.typeName;

        this.reasons = new ArrayList<>();
        if(src.reason != null && !src.reason.isEmpty()) {
            for (Reason reason : src.reason) {
                this.reasons.add(new GoalProgressReasonDto(reason));
            }
        }
    }

    public List<EntryMetadataDto> getPointsEntryMetadatas() {
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

    public List<GoalProgressReasonDto> getReasons() {
        return reasons;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.pointsEntryMetadatas);
        dest.writeString(this.categoryCode);
        dest.writeInt(this.categoryKey);
        dest.writeString(this.categoryName);
        dest.writeInt(this.earnedValue);
        dest.writeString(this.effectiveDate);
        dest.writeInt(this.eventId);
        dest.writeInt(this.id);
        dest.writeLong(this.partyId);
        dest.writeInt(this.pointsContributed);
        dest.writeInt(this.potentialValue);
        dest.writeInt(this.prelimitValue);
        dest.writeString(this.statusChangeDate);
        dest.writeString(this.statusTypeCode);
        dest.writeInt(this.statusTypeKey);
        dest.writeString(this.statusTypeName);
        dest.writeString(this.systemAwareOn);
        dest.writeString(this.typeCode);
        dest.writeInt(this.typeKey);
        dest.writeString(this.typeName);
        dest.writeTypedList(this.reasons);
    }

    protected ObjectivePointsEntriesDto(Parcel in) {
        this.pointsEntryMetadatas = in.createTypedArrayList(EntryMetadataDto.CREATOR);
        this.categoryCode = in.readString();
        this.categoryKey = in.readInt();
        this.categoryName = in.readString();
        this.earnedValue = in.readInt();
        this.effectiveDate = in.readString();
        this.eventId = in.readInt();
        this.id = in.readInt();
        this.partyId = in.readLong();
        this.pointsContributed = in.readInt();
        this.potentialValue = in.readInt();
        this.prelimitValue = in.readInt();
        this.statusChangeDate = in.readString();
        this.statusTypeCode = in.readString();
        this.statusTypeKey = in.readInt();
        this.statusTypeName = in.readString();
        this.systemAwareOn = in.readString();
        this.typeCode = in.readString();
        this.typeKey = in.readInt();
        this.typeName = in.readString();
        this.reasons = in.createTypedArrayList(GoalProgressReasonDto.CREATOR);
    }

    public static final Parcelable.Creator<ObjectivePointsEntriesDto> CREATOR = new Parcelable.Creator<ObjectivePointsEntriesDto>() {
        @Override
        public ObjectivePointsEntriesDto createFromParcel(Parcel source) {
            return new ObjectivePointsEntriesDto(source);
        }

        @Override
        public ObjectivePointsEntriesDto[] newArray(int size) {
            return new ObjectivePointsEntriesDto[size];
        }
    };
}
