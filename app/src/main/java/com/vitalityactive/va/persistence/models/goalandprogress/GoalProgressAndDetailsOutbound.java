package com.vitalityactive.va.persistence.models.goalandprogress;

import com.vitalityactive.va.persistence.Model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GoalProgressAndDetailsOutbound extends RealmObject implements Model {

    private RealmList<EntryMetadata> eventMetaDatas;
    private String dateLogged;
    private String eventDateTime;
    private String eventSourceCode;
    private int eventSourceKey;
    private String eventSourceName;
    @PrimaryKey
    private int id;
    private long partyId;
    private long reportedBy;
    private String typeCode;
    private int typeKey;
    private String typeName;

    public GoalProgressAndDetailsOutbound() {}

    public GoalProgressAndDetailsOutbound(com.vitalityactive.va.networking.model.goalandprogress.GoalProgressAndDetailsOutbound goalProgressAndDetailsOutbound) {
        this.eventMetaDatas = new RealmList<>();
        if(goalProgressAndDetailsOutbound.eventMetaDatas != null &&
                !goalProgressAndDetailsOutbound.eventMetaDatas.isEmpty()) {
            for (com.vitalityactive.va.networking.model.goalandprogress.EntryMetadata item : goalProgressAndDetailsOutbound.eventMetaDatas) {
                this.eventMetaDatas.add(new EntryMetadata(item));
            }
        }
        this.dateLogged = goalProgressAndDetailsOutbound.dateLogged;
        this.eventDateTime = goalProgressAndDetailsOutbound.eventDateTime;
        this.eventSourceCode = goalProgressAndDetailsOutbound.eventSourceCode;
        this.eventSourceKey = goalProgressAndDetailsOutbound.eventSourceKey;
        this.eventSourceName = goalProgressAndDetailsOutbound.eventSourceName;
        this.id = goalProgressAndDetailsOutbound.id;
        this.partyId = goalProgressAndDetailsOutbound.partyId;
        this.reportedBy = goalProgressAndDetailsOutbound.reportedBy;
        this.typeCode = goalProgressAndDetailsOutbound.typeCode;
        this.typeKey = goalProgressAndDetailsOutbound.typeKey;
        this.typeName = goalProgressAndDetailsOutbound.typeName;
    }

    public RealmList<EntryMetadata> getEventMetaDatas() {
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
}
