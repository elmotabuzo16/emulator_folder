package com.vitalityactive.va.activerewards.landing.service;

import com.google.gson.annotations.SerializedName;

public class ActiveRewardsActivationPayload {
    @SerializedName("effectivePeriod")
    public EffectiveDate effectivePeriod;

    public ActiveRewardsActivationPayload(String startDate, String endDate) {
        this.effectivePeriod = new EffectiveDate(startDate, endDate);
    }
}
