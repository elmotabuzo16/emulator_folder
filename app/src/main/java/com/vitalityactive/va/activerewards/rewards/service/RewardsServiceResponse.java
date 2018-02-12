package com.vitalityactive.va.activerewards.rewards.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.annotations.Required;

public class RewardsServiceResponse {
    @SerializedName("awardedRewards")
    public List<AwardedReward> awardedRewards;

    public static class AwardedReward {
        @Required
        @SerializedName("agreementPartyId")
        public Integer agreementPartyId;
        @Required
        @SerializedName("awardedOn")
        public String awardedOn;
        @SerializedName("awardedRewardReferences")
        public List<Reference> awardedRewardReferences;
        @SerializedName("awardedRewardStatuses")
        public List<Status> awardedRewardStatuses;
        @Required
        @SerializedName("effectiveFrom")
        public String effectiveFrom;
        @Required
        @SerializedName("effectiveTo")
        public String effectiveTo;
        @Required
        @SerializedName("eventId")
        public Integer eventId;
        @SerializedName("exchange")
        public Exchange exchange;
        @Required
        @SerializedName("id")
        public Integer id;
        @SerializedName("inboundFinancialInstructionId")
        public Integer inboundFinancialInstructionId;
        @SerializedName("outcomeRewardValueLinkId")
        public Integer outcomeRewardValueLinkId;
        @Required
        @SerializedName("partyId")
        public Long partyId;
        @Required
        @SerializedName("reward")
        public Reward reward;
        @Required
        @SerializedName("rewardProviderPartyId")
        public Long rewardProviderPartyId;
        @SerializedName("rewardSelectionType")
        public RewardSelectionType rewardSelectionType;
        @SerializedName("rewardValue")
        public RewardValue rewardValue;
    }

    public class Reference {
        @SerializedName("typeCode")
        public String typeCode;
        @Required
        @SerializedName("typeKey")
        public Integer typeKey;
        @SerializedName("typeName")
        public String typeName;
        @Required
        @SerializedName("value")
        public String value;
    }

    public class Status {
        @SerializedName("code")
        public String code;
        @Required
        @SerializedName("effectiveOn")
        public String effectiveOn;
        @Required
        @SerializedName("key")
        public Integer key;
        @SerializedName("name")
        public String name;
        @Required
        @SerializedName("partyId")
        public Long partyId;
    }

    public class Exchange {
        @Required
        @SerializedName("exchangeOn")
        public String exchangeOn;
        @Required
        @SerializedName("fromRewardId")
        public Integer fromRewardId;
        @Required
        @SerializedName("toRewardId")
        public Integer toRewardId;
        @SerializedName("typeCode")
        public String typeCode;
        @Required
        @SerializedName("typeKey")
        public Integer typeKey;
        @SerializedName("typeName")
        public String typeName;
    }

    public static class Reward {
        @SerializedName("categoryCode")
        public String categoryCode;
        @Required
        @SerializedName("categoryKey")
        public Integer categoryKey;
        @SerializedName("categoryName")
        public String categoryName;
        @Required
        @SerializedName("id")
        public Integer id;
        @Required
        @SerializedName("key")
        public Integer key;
        @Required
        @SerializedName("name")
        public String name;
        @SerializedName("optionTypeCode")
        public String optionTypeCode;
        @Required
        @SerializedName("optionTypeKey")
        public Integer optionTypeKey;
        @Required
        @SerializedName("optionTypeName")
        public String optionTypeName;
        @Required
        @SerializedName("providedByPartyId")
        public Long providedByPartyId;
        @SerializedName("typeCategoryCode")
        public String typeCategoryCode;
        @Required
        @SerializedName("typeCategoryKey")
        public Integer typeCategoryKey;
        @SerializedName("typeCategoryName")
        public String typeCategoryName;
        @SerializedName("typeCode")
        public String typeCode;
        @Required
        @SerializedName("typeKey")
        public Integer typeKey;
        @SerializedName("typeName")
        public String typeName;
    }

    public class RewardSelectionType {
        @SerializedName("code")
        public String code;
        @Required
        @SerializedName("key")
        public Integer key;
        @SerializedName("name")
        public String name;
        @SerializedName("rewardSelections")
        public List<RewardSelection> rewardSelections;
    }

    public class RewardValue {
        @SerializedName("amount")
        public String amount;
        @SerializedName("itemDescription")
        public String itemDescription;
        @Required
        @SerializedName("linkId")
        public Integer linkId;
        @SerializedName("percent")
        public Float percent;
        @SerializedName("quantity")
        public Float quantity;
        @SerializedName("typeCode")
        public String typeCode;
        @Required
        @SerializedName("typeKey")
        public Integer typeKey;
        @SerializedName("typeName")
        public String typeName;
    }

    public class RewardSelection {
        @Required
        @SerializedName("reward")
        public Reward reward;
        @SerializedName("rewardValueAmount")
        public String rewardValueAmount;
        @SerializedName("rewardValueItemDescription")
        public String rewardValueItemDescription;
        @Required
        @SerializedName("rewardValueLinkId")
        public Integer rewardValueLinkId;
        @SerializedName("rewardValuePercent")
        public String rewardValuePercent;
        @SerializedName("rewardValueQuantity")
        public String rewardValueQuantity;
        @SerializedName("rewardValueTypeCode")
        public String rewardValueTypeCode;
        @Required
        @SerializedName("rewardValueTypeKey")
        public Integer rewardValueTypeKey;
        @Required
        @SerializedName("rewardValueTypeName")
        public String rewardValueTypeName;
        @Required
        @SerializedName("sortOrder")
        public Float sortOrder;
    }
}
