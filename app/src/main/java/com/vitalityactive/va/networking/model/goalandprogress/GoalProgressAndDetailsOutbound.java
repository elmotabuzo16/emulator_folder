package com.vitalityactive.va.networking.model.goalandprogress;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoalProgressAndDetailsOutbound {

    @SerializedName("eventMetaDatas")
    public List<EntryMetadata> eventMetaDatas;
    @SerializedName("dateLogged")
    public String dateLogged;
    @SerializedName("eventDateTime")
    public String eventDateTime;
    @SerializedName("eventSourceCode")
    public String eventSourceCode;
    @SerializedName("eventSourceKey")
    public int eventSourceKey;
    @SerializedName("eventSourceName")
    public String eventSourceName;
    @SerializedName("id")
    public int id;
    @SerializedName("partyId")
    public long partyId;
    @SerializedName("reportedBy")
    public long reportedBy;
    @SerializedName("typeCode")
    public String typeCode;
    @SerializedName("typeKey")
    public int typeKey;
    @SerializedName("typeName")
    public String typeName;
}
