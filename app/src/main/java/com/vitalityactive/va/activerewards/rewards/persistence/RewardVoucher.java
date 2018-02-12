package com.vitalityactive.va.activerewards.rewards.persistence;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vitalityactive.va.activerewards.rewards.service.RewardsServiceResponse;
import com.vitalityactive.va.constants.AwardedRewardReferenceType;
import com.vitalityactive.va.constants.AwardedRewardStatus;
import com.vitalityactive.va.constants.RewardSelectionType;
import com.vitalityactive.va.constants.RewardValueType;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RewardVoucher extends RealmObject implements Model {
    @PrimaryKey
    long id;
    @Nullable
    private RealmList<RealmString> voucherNumbers;
    private String expiryDate;
    private int rewardId;
    private String description;
    @NonNull
    private String rewardValue = "";
    @NonNull
    private String rewardType = "";
    @NonNull
    private String name = "";
    private boolean showCodePending;
    private boolean issueFailed;
    private boolean availableToRedeem;
    private String effectiveFromDate;
    private RealmList<RewardSelection> rewardSelections;

    public RewardVoucher() {

    }

    public RewardVoucher(RewardsServiceResponse.AwardedReward awardedReward, int awardedRewardStatus) {
        id = awardedReward.id;
        voucherNumbers = getVoucherNumbers(awardedReward);
        expiryDate = awardedReward.effectiveTo;
        rewardId = awardedReward.reward.id;
        description = getRewardValue(awardedReward) + getRewardType(awardedReward);
        rewardValue = getRewardValue(awardedReward);
        rewardType = getRewardType(awardedReward);
        name = awardedReward.reward.name;
        showCodePending = awardedRewardStatus == AwardedRewardStatus._ALLOCATED;
        issueFailed = awardedRewardStatus == AwardedRewardStatus._ISSUEFAILED;
        availableToRedeem = awardedRewardStatus == AwardedRewardStatus._AVAILABLETOREDEEM;
        effectiveFromDate = awardedReward.effectiveFrom;
        rewardSelections = new RealmList<>();
    }

    @Nullable
    public static RewardVoucher create(RewardsServiceResponse.AwardedReward awardedReward, int awardedRewardStatus) {
        if (awardedReward.rewardValue != null && (awardedReward.rewardValue.typeKey == null || awardedReward.rewardValue.linkId == null)) {
            return null;
        }
        RewardVoucher rewardVoucher = new RewardVoucher(awardedReward, awardedRewardStatus);

        if (!RewardsRepository.isRewardSelectionTypeInvalid(awardedReward) &&
                awardedReward.rewardSelectionType.key == RewardSelectionType._REWARDSELECTION) {

            for (RewardsServiceResponse.RewardSelection rewardSelection : awardedReward.rewardSelectionType.rewardSelections) {
                RewardSelection rewardSelectionModel = RewardSelection.create(rewardSelection, awardedReward.rewardSelectionType.key, awardedReward.id);
                if (rewardSelectionModel != null) {
                    rewardVoucher.rewardSelections.add(rewardSelectionModel);
                }
            }
            if (rewardVoucher.rewardSelections.isEmpty()) {
                return null;
            }
        }
        return rewardVoucher;
    }

    @NonNull
    private String getRewardType(RewardsServiceResponse.AwardedReward awardedReward) {
        if (awardedReward.rewardValue == null) {
            return "";
        }
        if (awardedReward.rewardValue.typeKey == RewardValueType._SPECIFICITEM) {
            return awardedReward.rewardValue.itemDescription == null ? "" : awardedReward.rewardValue.itemDescription;
        }
        return awardedReward.reward.typeName == null ? "" : awardedReward.reward.typeName;
    }

    @NonNull
    private String getRewardValue(RewardsServiceResponse.AwardedReward awardedReward) {
        if (awardedReward.rewardValue == null) {
            return "";
        }
        switch (awardedReward.rewardValue.typeKey) {
            case RewardValueType._MONETARY:
                return TextUtilities.toStringOrEmpty(awardedReward.rewardValue.amount);
            case RewardValueType._PERCENTAGE:
                return TextUtilities.toStringOrEmpty(awardedReward.rewardValue.percent);
            case RewardValueType._SPECIFICITEM:
                if (awardedReward.rewardValue.quantity == null || awardedReward.rewardValue.quantity <= 0.00001f) {
                    return "";
                }
                return ViewUtilities.roundToTwoDecimalsRemoveTrailingZeros(String.valueOf(awardedReward.rewardValue.quantity));
            default:
                return "";
        }
    }

    @Nullable
    private RealmList<RealmString> getVoucherNumbers(RewardsServiceResponse.AwardedReward awardedReward) {
        RealmList<RealmString> voucherNumbers = new RealmList<>();

        if (awardedReward.awardedRewardReferences != null) {
            for (RewardsServiceResponse.Reference reference : awardedReward.awardedRewardReferences) {
                if (reference.typeKey == AwardedRewardReferenceType._VOUCHERNUMBER) {
                    voucherNumbers.add(new RealmString(reference.value));
                }
            }
        }
        return voucherNumbers;
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

    public String getDescription() {
        return description;
    }

    @Nullable
    public List<String> getVoucherNumbers() {
        List<String> list = new ArrayList<>();
        if (voucherNumbers != null) {
            for (RealmString string : voucherNumbers) {
                list.add(string.value);
            }
        }

        return list;
    }

    @NonNull
    public String getRewardValue() {
        return rewardValue;
    }

    @NonNull
    public String getRewardType() {
        return rewardType;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public boolean getShowCodePending() {
        return showCodePending;
    }

    public boolean isIssueFailed() {
        return issueFailed;
    }

    public boolean isAvailableToRedeem() {
        return availableToRedeem;
    }

    public String getEffectiveFromDate() {
        return effectiveFromDate;
    }
}
