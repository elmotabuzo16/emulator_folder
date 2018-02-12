package com.vitalityactive.va.activerewards.landing.service;

import com.google.gson.annotations.SerializedName;

public class GoalProgressAndDetailsRequestBody {
    @SerializedName("getGoalProgressAndDetailsRequest")
    public GetGoalProgressAndDetailsRequest getGoalProgressAndDetailsRequest;

    public GoalProgressAndDetailsRequestBody(int key,
                                             long partyId,
                                             EffectiveDate range) {
        getGoalProgressAndDetailsRequest = new GetGoalProgressAndDetailsRequest(key, partyId, range);
    }

    public static class GetGoalProgressAndDetailsRequest {
        @SerializedName("effectiveDate")
        public EffectiveDate effectiveDate;
        @SerializedName("goal")
        public Goal[] goal;
        @SerializedName("partyId")
        public long partyId;

        public GetGoalProgressAndDetailsRequest(int key,
                                                long partyId,
                                                EffectiveDate range) {
            this.effectiveDate = range;
            this.partyId = partyId;
            this.goal = new Goal[] {new Goal(key)};
        }
    }

    public static class Goal {
        @SerializedName("key")
        public int key;

        public Goal(int key) {
            this.key = key;
        }
    }
}
