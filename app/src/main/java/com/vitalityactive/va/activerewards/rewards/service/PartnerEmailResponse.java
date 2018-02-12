package com.vitalityactive.va.activerewards.rewards.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PartnerEmailResponse {
    @SerializedName("getPartyResponse")
    public PartyResponse response;

    public class PartyResponse {
        @SerializedName("party")
        public Party party;
    }

    public class Party {
        @SerializedName("generalPreferences")
        public List<GeneralPreference> generalPreferences;
    }

    public class GeneralPreference {
        @SerializedName("typeKey")
        public int typeKey;
        @SerializedName("value")
        public String value;
    }
}
