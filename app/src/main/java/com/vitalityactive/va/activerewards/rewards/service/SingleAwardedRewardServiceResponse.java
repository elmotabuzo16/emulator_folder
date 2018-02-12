package com.vitalityactive.va.activerewards.rewards.service;

import com.google.gson.annotations.SerializedName;

public class SingleAwardedRewardServiceResponse {
    @SerializedName("awardedReward")
    public RewardsServiceResponse.AwardedReward awardedReward;
}
