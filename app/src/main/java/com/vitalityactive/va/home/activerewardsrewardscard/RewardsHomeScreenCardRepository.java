package com.vitalityactive.va.home.activerewardsrewardscard;

import com.vitalityactive.va.dto.RewardHomeCardDTO;

public interface RewardsHomeScreenCardRepository {
    RewardHomeCardDTO getHomeCard(String rewardId, String awardedRewardId);
}
