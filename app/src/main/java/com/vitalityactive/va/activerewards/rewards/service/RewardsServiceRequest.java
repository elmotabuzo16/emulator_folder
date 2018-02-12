package com.vitalityactive.va.activerewards.rewards.service;

import com.google.gson.annotations.SerializedName;
import com.vitalityactive.va.activerewards.landing.service.EffectiveDate;

class RewardsServiceRequest {
    @SerializedName("effectivePeriod")
    private EffectiveDate effectivePeriod;
    @SerializedName("party")
    private Party party;

    RewardsServiceRequest(EffectiveDate effectivePeriod, long partyId) {
        this.effectivePeriod = effectivePeriod;
        party = new Party(partyId);
    }

    private class Party {
        @SerializedName("id")
        long partyId;

        Party(long partyId) {
            this.partyId = partyId;
        }
    }
}
