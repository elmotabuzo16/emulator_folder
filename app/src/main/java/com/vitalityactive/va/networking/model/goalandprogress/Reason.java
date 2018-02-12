package com.vitalityactive.va.networking.model.goalandprogress;

import com.google.gson.annotations.SerializedName;

public class Reason {
    @SerializedName("categoryCode")
    public String categoryCode;
    @SerializedName("categoryKey")
    public int categoryKey;
    @SerializedName("reasonKey")
    public int reasonKey;
    @SerializedName("reasonName")
    public String reasonName;
    @SerializedName("reasonCode")
    public String reasonCode;
    @SerializedName("categoryName")
    public String categoryName;
}
