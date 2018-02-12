package com.vitalityactive.va.activerewards.rewards.service;

import com.google.gson.annotations.SerializedName;
import com.vitalityactive.va.constants.AwardedRewardStatus;
import com.vitalityactive.va.constants.PartyReferenceType;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.NonUserFacingDateFormatter;

class MarkNoRewardWheelSpinUsedRequest {
    @SerializedName("awardedRewardId")
    private final long awardedRewardId;
    @SerializedName("awardedRewardStatusKey")
    private final int awardedRewardStatusKey = AwardedRewardStatus._USED;
    @SerializedName("changedBy")
    private final PartyReference changedBy;
    @SerializedName("effectiveOn")
    private final String effectiveOn;

    MarkNoRewardWheelSpinUsedRequest(long wheelSpinUniqueId, long partyId) {
        awardedRewardId = wheelSpinUniqueId;
        changedBy = new PartyReference(partyId);
        effectiveOn = NonUserFacingDateFormatter.getYearMonthDayFormatter().format(LocalDate.now());
    }

    private class PartyReference {
        @SerializedName("referenceTypeKey")
        private final long referenceTypeKey = PartyReferenceType._NATIONALID;
        @SerializedName("referenceValue")
        private final long referenceValue;

        PartyReference(long partyId) {
            this.referenceValue = partyId;
        }
    }
}
