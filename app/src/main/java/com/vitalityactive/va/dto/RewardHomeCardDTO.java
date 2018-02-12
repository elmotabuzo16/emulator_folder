package com.vitalityactive.va.dto;

import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.persistence.models.RewardHomeCard;

public class RewardHomeCardDTO {
    public HomeCardType type = HomeCardType.UNKNOWN;
    public HomeCardType.StatusType status = HomeCardType.StatusType.UNKNOWN;
    public String awardedRewardId;
    public String rewardId;
    public String rewardName;
    public String rewardValueAmount;
    public String rewardValueDesc;
    public String rewardValueQuantity;
    public String rewardValuePercent;
    public String validTo;

    public RewardHomeCardDTO() {

    }

    public RewardHomeCardDTO(RewardHomeCard card) {
        type = HomeCardType.fromTypeKey(card.getType());
        status = HomeCardType.StatusType.fromTypeKey(card.getStatus());
        validTo = card.getValidTo();
        awardedRewardId = card.getAwardedRewardId();
        rewardId = card.getRewardId();
        rewardName = card.getRewardName();
        rewardValueAmount = card.getRewardValueAmount();
        rewardValueDesc = card.getRewardValueDesc();
        rewardValueQuantity = card.getRewardValueQuantity();
        rewardValuePercent = card.getRewardValuePercent();
    }

    public boolean isPending() {
        return status == HomeCardType.StatusType.PENDING;
    }

    public boolean isAvailableToRedeem() {
        return status == HomeCardType.StatusType.AVAILABLE_TO_REDEEM;
    }

    public boolean isAvailable() {
        return status == HomeCardType.StatusType.AVAILABLE;
    }

}
