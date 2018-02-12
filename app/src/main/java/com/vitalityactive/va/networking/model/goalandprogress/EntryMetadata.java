package com.vitalityactive.va.networking.model.goalandprogress;

import com.google.gson.annotations.SerializedName;

public class EntryMetadata {
    @SerializedName("typeCode")
    public String typeCode;
    @SerializedName("typeKey")
    public int typeKey;
    @SerializedName("typeName")
    public String typeName;
    @SerializedName("unitOfMeasure")
    public String unitOfMeasure;
    @SerializedName("value")
    public String value;
}
