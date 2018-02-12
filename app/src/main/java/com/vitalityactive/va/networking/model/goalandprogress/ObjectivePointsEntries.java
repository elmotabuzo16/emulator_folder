package com.vitalityactive.va.networking.model.goalandprogress;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ObjectivePointsEntries {

    @SerializedName("pointsEntryMetadatas")
    public List<EntryMetadata> pointsEntryMetadatas;
    @SerializedName("categoryCode")
    public String categoryCode;
    @SerializedName("categoryKey")
    public int categoryKey;
    @SerializedName("categoryName")
    public String categoryName;
    @SerializedName("earnedValue")
    public int earnedValue;
    @SerializedName("effectiveDate")
    public String effectiveDate;
    @SerializedName("eventId")
    public int eventId;
    @SerializedName("id")
    public int id;
    @SerializedName("partyId")
    public long partyId;
    @SerializedName("pointsContributed")
    public int pointsContributed;
    @SerializedName("potentialValue")
    public int potentialValue;
    @SerializedName("prelimitValue")
    public int prelimitValue;
    @SerializedName("statusChangeDate")
    public String statusChangeDate;
    @SerializedName("statusTypeCode")
    public String statusTypeCode;
    @SerializedName("statusTypeKey")
    public int statusTypeKey;
    @SerializedName("statusTypeName")
    public String statusTypeName;
    @SerializedName("systemAwareOn")
    public String systemAwareOn;
    @SerializedName("typeCode")
    public String typeCode;
    @SerializedName("typeKey")
    public int typeKey;
    @SerializedName("typeName")
    public String typeName;
    @SerializedName("reason")
    public List<com.vitalityactive.va.networking.model.goalandprogress.Reason> reason;
}
