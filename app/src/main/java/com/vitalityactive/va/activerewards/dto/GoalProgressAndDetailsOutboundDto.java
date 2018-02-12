package com.vitalityactive.va.activerewards.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.vitalityactive.va.persistence.models.goalandprogress.EntryMetadata;
import com.vitalityactive.va.persistence.models.goalandprogress.GoalProgressAndDetailsOutbound;

import java.util.ArrayList;
import java.util.List;

public class GoalProgressAndDetailsOutboundDto implements Parcelable {

    private List<EntryMetadataDto> eventMetaDatas;
    private String dateLogged;
    private String eventDateTime;
    private String eventSourceCode;
    private int eventSourceKey;
    private String eventSourceName;
    private int id;
    private long partyId;
    private long reportedBy;
    private String typeCode;
    private int typeKey;
    private String typeName;

    public GoalProgressAndDetailsOutboundDto(GoalProgressAndDetailsOutbound src) {
        this.eventMetaDatas = new ArrayList<>();
        for(EntryMetadata entryMetadata : src.getEventMetaDatas()){
            this.eventMetaDatas.add(new EntryMetadataDto(entryMetadata));
        }
        this.dateLogged = src.getDateLogged();
        this.eventDateTime = src.getEventDateTime();
        this.eventSourceCode = src.getEventSourceCode();
        this.eventSourceKey = src.getEventSourceKey();
        this.eventSourceName = src.getEventSourceName();
        this.id = src.getId();
        this.partyId = src.getPartyId();
        this.reportedBy = src.getReportedBy();
        this.typeCode = src.getTypeCode();
        this.typeKey = src.getTypeKey();
        this.typeName = src.getTypeName();
    }

    public GoalProgressAndDetailsOutboundDto(com.vitalityactive.va.networking.model.goalandprogress.GoalProgressAndDetailsOutbound src) {
        this.eventMetaDatas = new ArrayList<>();
        if(src.eventMetaDatas != null && !src.eventMetaDatas.isEmpty()) {
            for (com.vitalityactive.va.networking.model.goalandprogress.EntryMetadata entryMetadata : src.eventMetaDatas) {
                this.eventMetaDatas.add(new EntryMetadataDto(entryMetadata));
            }
        }
        this.dateLogged = src.dateLogged;
        this.eventDateTime = src.eventDateTime;
        this.eventSourceCode = src.eventSourceCode;
        this.eventSourceKey = src.eventSourceKey;
        this.eventSourceName = src.eventSourceName;
        this.id = src.id;
        this.partyId = src.partyId;
        this.reportedBy = src.reportedBy;
        this.typeCode = src.typeCode;
        this.typeKey = src.typeKey;
        this.typeName = src.typeName;
    }

    public List<EntryMetadataDto> getEventMetaDatas() {
        return eventMetaDatas;
    }

    public String getDateLogged() {
        return dateLogged;
    }

    public String getEventDateTime() {
        return eventDateTime;
    }

    public String getEventSourceCode() {
        return eventSourceCode;
    }

    public int getEventSourceKey() {
        return eventSourceKey;
    }

    public String getEventSourceName() {
        return eventSourceName;
    }

    public int getId() {
        return id;
    }

    public long getPartyId() {
        return partyId;
    }

    public long getReportedBy() {
        return reportedBy;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.eventMetaDatas);
        dest.writeString(this.dateLogged);
        dest.writeString(this.eventDateTime);
        dest.writeString(this.eventSourceCode);
        dest.writeInt(this.eventSourceKey);
        dest.writeString(this.eventSourceName);
        dest.writeInt(this.id);
        dest.writeLong(this.partyId);
        dest.writeLong(this.reportedBy);
        dest.writeString(this.typeCode);
        dest.writeInt(this.typeKey);
        dest.writeString(this.typeName);
    }

    protected GoalProgressAndDetailsOutboundDto(Parcel in) {
        this.eventMetaDatas = in.createTypedArrayList(EntryMetadataDto.CREATOR);
        this.dateLogged = in.readString();
        this.eventDateTime = in.readString();
        this.eventSourceCode = in.readString();
        this.eventSourceKey = in.readInt();
        this.eventSourceName = in.readString();
        this.id = in.readInt();
        this.partyId = in.readLong();
        this.reportedBy = in.readInt();
        this.typeCode = in.readString();
        this.typeKey = in.readInt();
        this.typeName = in.readString();
    }

    public static final Parcelable.Creator<GoalProgressAndDetailsOutboundDto> CREATOR = new Parcelable.Creator<GoalProgressAndDetailsOutboundDto>() {
        @Override
        public GoalProgressAndDetailsOutboundDto createFromParcel(Parcel source) {
            return new GoalProgressAndDetailsOutboundDto(source);
        }

        @Override
        public GoalProgressAndDetailsOutboundDto[] newArray(int size) {
            return new GoalProgressAndDetailsOutboundDto[size];
        }
    };
}
