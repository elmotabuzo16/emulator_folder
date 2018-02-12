package com.vitalityactive.va.activerewards.rewards.dto;

import com.vitalityactive.va.activerewards.rewards.persistence.RewardSelection;

public class RewardSelectionDTO {
    public final String rewardName;
    public final String rewardDescription;
    public final int rewardId;
    public final int rewardKey;
    public final int rewardValueLinkId;
    public final long rewardUniqueId;
    public boolean selected;

    public RewardSelectionDTO(RewardSelection rewardSelection) {
        this.rewardName = rewardSelection.getRewardName();
        this.rewardDescription = rewardSelection.getRewardValue() + " " + rewardSelection.getRewardType();
        this.rewardId = rewardSelection.getRewardId();
        this.rewardKey = rewardSelection.getRewardKey();
        this.rewardValueLinkId = rewardSelection.getRewardValueLinkId();
        this.rewardUniqueId = rewardSelection.getRewardUniqueId();
    }
}
