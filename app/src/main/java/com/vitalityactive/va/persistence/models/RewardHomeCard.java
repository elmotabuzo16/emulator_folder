package com.vitalityactive.va.persistence.models;

import android.support.annotation.NonNull;

import com.vitalityactive.va.constants.CardMetadataType;
import com.vitalityactive.va.constants.CardStatusType;
import com.vitalityactive.va.home.service.HomeScreenCardStatusResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.utilities.date.LocalDate;

import io.realm.RealmObject;

public class RewardHomeCard extends RealmObject implements Model {
    @NonNull
    private String validTo = LocalDate.now().toString();
    private String type;
    private int status;
    private String awardedRewardId;
    private String rewardId;
    private String rewardName;
    private String rewardValueAmount;
    private String rewardValueDesc;
    private String rewardValueQuantity;
    private String rewardValuePercent;

    public RewardHomeCard() {

    }

    public RewardHomeCard(HomeScreenCardStatusResponse.Card card) {
        type = card.typeKey;
        status = card.statusTypeKey;
        validTo = card.validTo;
        for (HomeScreenCardStatusResponse.Metadata metadata : card.cardMetadatas) {
            switch (metadata.typeKey) {
                case CardMetadataType._AWARDEDREWARDID:
                    if (metadata.value != null) {
                        awardedRewardId = metadata.value;
                    }
                    break;
                case CardMetadataType._REWARDID:
                    rewardId = metadata.value;
                    break;
                case CardMetadataType._REWARDNAME:
                    rewardName = metadata.value;
                    break;
                case CardMetadataType._REWARDVALUEAMOUNT:
                    if (metadata.value != null) {
                        rewardValueAmount = metadata.value;
                    }
                    break;
                case CardMetadataType._REWARDVALUEDESC:
                    if (metadata.value != null) {
                        rewardValueDesc = metadata.value;
                    }
                    break;
                case CardMetadataType._REWARDVALUEQUANTITY:
                    if (metadata.value != null) {
                        rewardValueQuantity = metadata.value;
                    }
                    break;
                case CardMetadataType._REWARDVALUEPERCENT:
                    if (metadata.value != null) {
                        rewardValuePercent = metadata.value;
                    }
                    break;
            }
        }
    }

    public static RewardHomeCard create(HomeScreenCardStatusResponse.Card model) {
        if (model.statusTypeKey != CardStatusType._DONE) {
            return new RewardHomeCard(model);
        } else {
            return null;
        }
    }

    public String getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    @NonNull
    public String getValidTo() {
        return validTo;
    }

    public String getAwardedRewardId() {
        return awardedRewardId;
    }

    public String getRewardId() {
        return rewardId;
    }

    public String getRewardName() {
        return rewardName;
    }

    public String getRewardValueAmount() {
        return rewardValueAmount;
    }

    public String getRewardValueDesc() {
        return rewardValueDesc;
    }

    public String getRewardValueQuantity() {
        return rewardValueQuantity;
    }

    public String getRewardValuePercent() {
        return rewardValuePercent;
    }
}
