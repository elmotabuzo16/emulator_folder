package com.vitalityactive.va.activerewards.landing.service;

import com.google.gson.annotations.SerializedName;

public class EffectiveDate {
    @SerializedName("effectiveFrom")
    public String effectiveFrom;
    @SerializedName("effectiveTo")
    public String effectiveTo;

    public EffectiveDate(String from, String to) {
        this.effectiveFrom = from;
        this.effectiveTo = to;
    }

    @Override
    public String toString() {
        return effectiveFrom + effectiveTo;
    }
}
