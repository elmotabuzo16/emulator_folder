package com.vitalityactive.va.activerewards.rewards.persistence;

import android.support.annotation.NonNull;

import com.vitalityactive.va.activerewards.rewards.service.RewardsServiceResponse;
import com.vitalityactive.va.constants.RewardValueType;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;

import io.realm.RealmObject;

public class RewardSelection extends RealmObject implements Model {
    @NonNull
    private String rewardName = "";
    @NonNull
    private String rewardValue = "";
    private int rewardSelectionTypeKey;
    @NonNull
    private String rewardType = "";
    private int rewardId;
    private int rewardKey;
    private int rewardValueLinkId;
    private long rewardUniqueId;
    private float sortOrder;

    public RewardSelection() {

    }

    public RewardSelection(@NonNull RewardsServiceResponse.RewardSelection rewardSelection, int rewardSelectionTypeKey, long rewardUniqueId) {
        rewardName = rewardSelection.reward.name;
        rewardValue = getRewardValue(rewardSelection);
        rewardType = getRewardType(rewardSelection);
        rewardId = rewardSelection.reward.id;
        rewardKey = rewardSelection.reward.key;
        this.rewardUniqueId = rewardUniqueId;
        rewardValueLinkId = rewardSelection.rewardValueLinkId;
        this.rewardSelectionTypeKey = rewardSelectionTypeKey;
        sortOrder = rewardSelection.sortOrder;
    }

    @NonNull
    private String getRewardType(RewardsServiceResponse.RewardSelection rewardSelection) {
        if (rewardSelection.rewardValueTypeKey == RewardValueType._SPECIFICITEM) {
            return rewardSelection.rewardValueItemDescription;
        }
        return TextUtilities.toStringOrEmpty(rewardSelection.reward.typeName);
    }

    @NonNull
    private String getRewardValue(@NonNull RewardsServiceResponse.RewardSelection rewardSelection) {
        switch (rewardSelection.rewardValueTypeKey) {
            case RewardValueType._MONETARY:
                return TextUtilities.toStringOrEmpty(rewardSelection.rewardValueAmount);
            case RewardValueType._SPECIFICITEM:
                String quantity = ViewUtilities.roundToTwoDecimalsRemoveTrailingZeros(TextUtilities.toStringOrEmpty(rewardSelection.rewardValueQuantity));
                return "0".equals(quantity) ? "" : quantity;
            case RewardValueType._PERCENTAGE:
                return TextUtilities.toStringOrEmpty(rewardSelection.rewardValuePercent);
        }
        return "";
    }

    public static RewardSelection create(@NonNull RewardsServiceResponse.RewardSelection rewardSelection, int rewardSelectionTypeKey, long rewardUniqueId) {
        if (RewardsRepository.isRewardInvalid(rewardSelection.reward) ||
                rewardSelection.rewardValueLinkId == null ||
                rewardSelection.rewardValueTypeKey == null ||
                rewardSelection.rewardValueTypeName == null ||
                rewardSelection.sortOrder == null) {
            return null;
        }
        return new RewardSelection(rewardSelection, rewardSelectionTypeKey, rewardUniqueId);
    }

    @NonNull
    public String getRewardName() {
        return rewardName;
    }

    @NonNull
    public String getRewardValue() {
        return rewardValue;
    }

    @NonNull
    public String getRewardType() {
        return rewardType;
    }

    public long getRewardUniqueId() {
        return rewardUniqueId;
    }

    public int getRewardId() {
        return rewardId;
    }

    public int getRewardKey() {
        return rewardKey;
    }

    public int getRewardValueLinkId() {
        return rewardValueLinkId;
    }
}
