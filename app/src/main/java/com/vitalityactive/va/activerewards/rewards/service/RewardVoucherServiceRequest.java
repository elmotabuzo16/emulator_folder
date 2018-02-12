package com.vitalityactive.va.activerewards.rewards.service;

import com.google.gson.annotations.SerializedName;

class RewardVoucherServiceRequest {
    @SerializedName("reward")
    public Reward reward;

    RewardVoucherServiceRequest(long rewardVoucherUniqueId) {
        reward = new Reward(rewardVoucherUniqueId);
    }

    public class Reward {
        @SerializedName("id")
        long id;

        public Reward(long rewardVoucherUniqueId) {
            id = rewardVoucherUniqueId;
        }
    }
}
