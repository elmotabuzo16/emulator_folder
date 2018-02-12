package com.vitalityactive.va.eventsfeed.data.net.request;

/**
 * Created by jayellos on 11/17/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EffectivePeriod {

    @SerializedName("effectiveFrom")
    @Expose
    private String effectiveFrom;
    @SerializedName("effectiveTo")
    @Expose
    private String effectiveTo;

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(String effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public String getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(String effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

}