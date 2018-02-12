package com.vitalityactive.va.activerewards.rewards.dto;

import com.vitalityactive.va.activerewards.rewards.persistence.UnclaimedReward;
import com.vitalityactive.va.utilities.date.LocalDate;

public class UnclaimedRewardDTO extends RewardDTO {
    public final LocalDate awardedOnDate;
    public final int outcomeRewardId;
    public final String outcomeRewardName;
    public final String outcomeRewardValue;
    public final String outcomeRewardType;
    public final long outcomeRewardKey;
    public final long outcomeRewardValueLinkId;

    public UnclaimedRewardDTO(UnclaimedReward unclaimedReward) {
        super(unclaimedReward.getExpiryDate(), unclaimedReward.getId(), unclaimedReward.getRewardId(), unclaimedReward.getName());
        awardedOnDate = new LocalDate(unclaimedReward.getAwardedOnDate());
        outcomeRewardId = unclaimedReward.getOutcomeRewardId();
        outcomeRewardName = unclaimedReward.getOutcomeRewardName();
        outcomeRewardValue = unclaimedReward.getOutcomeRewardValue();
        outcomeRewardType = unclaimedReward.getOutcomeRewardType();
        outcomeRewardKey = unclaimedReward.getOutcomeRewardKey();
        outcomeRewardValueLinkId = unclaimedReward.getOutcomeRewardValueLinkId();
    }
}
