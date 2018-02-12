package com.vitalityactive.va.activerewards.rewards.presenters;

import android.support.annotation.NonNull;

import com.vitalityactive.va.activerewards.rewards.dto.UnclaimedRewardDTO;
import com.vitalityactive.va.activerewards.rewards.persistence.UnclaimedReward;
import com.vitalityactive.va.activerewards.rewards.service.RewardsServiceResponse;

import static com.vitalityactive.va.constants.RewardId._STARBUCKSVOUCHER;

class BaseRewardPresenterTest {
    protected int rewardUniqueId = 1234;

    @NonNull
    UnclaimedRewardDTO getUnclaimedReward() {
        RewardsServiceResponse.AwardedReward awardedReward = getAwardedReward();
        return new UnclaimedRewardDTO(new UnclaimedReward(awardedReward, 0));
    }

    @NonNull
    public RewardsServiceResponse.AwardedReward getAwardedReward() {
        RewardsServiceResponse.AwardedReward awardedReward = new RewardsServiceResponse.AwardedReward();

        awardedReward.id = rewardUniqueId;
        awardedReward.outcomeRewardValueLinkId = 0;
        awardedReward.awardedOn = "2017-01-01";
        awardedReward.effectiveFrom = "2017-01-01";
        awardedReward.effectiveTo = "2017-01-31";
        awardedReward.reward = new RewardsServiceResponse.Reward();
        awardedReward.reward.id = _STARBUCKSVOUCHER;
        awardedReward.reward.name = "Test Voucher";

        return awardedReward;
    }
}
