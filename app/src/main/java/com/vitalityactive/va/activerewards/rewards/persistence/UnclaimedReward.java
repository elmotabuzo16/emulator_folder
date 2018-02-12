package com.vitalityactive.va.activerewards.rewards.persistence;

import android.support.annotation.NonNull;

import com.vitalityactive.va.activerewards.rewards.service.RewardsServiceResponse;
import com.vitalityactive.va.constants.AwardedRewardStatus;
import com.vitalityactive.va.constants.RewardSelectionType;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UnclaimedReward extends RealmObject implements Model {
    @PrimaryKey
    private long id;
    private boolean skipSpin;
    private long outcomeRewardValueLinkId;
    private String awardedOnDate;
    private String expiryDate;
    private RealmList<RewardSelection> rewardSelections;
    private int rewardId;
    private String name;
    private int outcomeRewardId;
    private String outcomeRewardName;
    private String outcomeRewardValue;
    private String outcomeRewardType;
    private long outcomeRewardKey;

    public UnclaimedReward() {

    }

    public UnclaimedReward(@NonNull RewardsServiceResponse.AwardedReward awardedReward, int status) {
        this.id = awardedReward.id;
        skipSpin = status == AwardedRewardStatus._ACKNOWLEDGED;
        outcomeRewardValueLinkId = awardedReward.outcomeRewardValueLinkId == null ? 0 : awardedReward.outcomeRewardValueLinkId;
        awardedOnDate = awardedReward.awardedOn;
        expiryDate = awardedReward.effectiveTo;
        rewardId = awardedReward.reward.id;
        name = awardedReward.reward.name;
    }

    public static UnclaimedReward create(@NonNull RewardsServiceResponse.AwardedReward awardedReward, int status) {
        if ((awardedReward.rewardSelectionType.key == RewardSelectionType._WHEELSPIN && awardedReward.outcomeRewardValueLinkId == null)) {
            return null;
        }
        if (status == AwardedRewardStatus._ACKNOWLEDGED) {
            return new UnclaimedReward(awardedReward, status);
        }

        if (RewardsRepository.isRewardSelectionTypeInvalid(awardedReward)) {
            return null;
        }
        if (awardedReward.rewardSelectionType.key != RewardSelectionType._WHEELSPIN && awardedReward.rewardSelectionType.key != RewardSelectionType._CHOICE) {
            return null;
        }
        UnclaimedReward unclaimedReward = new UnclaimedReward(awardedReward, status);
        unclaimedReward.rewardSelections = new RealmList<>();
        boolean outcomeIsMissing = true;
        for (RewardsServiceResponse.RewardSelection rewardSelection : awardedReward.rewardSelectionType.rewardSelections) {
            RewardSelection rewardSelectionModel = RewardSelection.create(rewardSelection, awardedReward.rewardSelectionType.key, awardedReward.id);
            if (rewardSelectionModel != null) {
                if (rewardSelection.rewardValueLinkId.equals(awardedReward.outcomeRewardValueLinkId)) {
                    outcomeIsMissing = false;
                    unclaimedReward.outcomeRewardId = rewardSelectionModel.getRewardId();
                    unclaimedReward.outcomeRewardName = rewardSelectionModel.getRewardName();
                    unclaimedReward.outcomeRewardValue = rewardSelectionModel.getRewardValue();
                    unclaimedReward.outcomeRewardType = rewardSelectionModel.getRewardType();
                    unclaimedReward.outcomeRewardKey = rewardSelection.reward.key;
                }
                unclaimedReward.rewardSelections.add(rewardSelectionModel);
            }
        }
        if ((awardedReward.rewardSelectionType.key == RewardSelectionType._WHEELSPIN && outcomeIsMissing) || unclaimedReward.rewardSelections.isEmpty()) {
            return null;
        }

        return unclaimedReward;
    }

    public String getAwardedOnDate() {
        return awardedOnDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public long getId() {
        return id;
    }

    public int getRewardId() {
        return rewardId;
    }

    public String getName() {
        return name;
    }

    public int getOutcomeRewardId() {
        return outcomeRewardId;
    }

    public String getOutcomeRewardName() {
        return outcomeRewardName;
    }

    public String getOutcomeRewardValue() {
        return outcomeRewardValue;
    }

    public String getOutcomeRewardType() {
        return outcomeRewardType;
    }

    public long getOutcomeRewardKey() {
        return outcomeRewardKey;
    }

    public long getOutcomeRewardValueLinkId() {
        return outcomeRewardValueLinkId;
    }
}
