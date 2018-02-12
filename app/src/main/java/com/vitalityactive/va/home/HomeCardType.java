package com.vitalityactive.va.home;

import android.support.annotation.NonNull;

import com.vitalityactive.va.constants.CardMetadataType;
import com.vitalityactive.va.constants.CardStatusType;
import com.vitalityactive.va.constants.CardType;

public enum HomeCardType {
    NON_SMOKERS_DECLARATION(getCardType(CardType._NONSMOKERSDECL)),
    VITALITY_HEALTH_CHECK(getCardType(CardType._VITALITYHEALTHCHECK)),
    ACTIVE_REWARDS(getCardType(CardType._ACTIVEREWARDS)),
    VITALITY_HEALTH_REVIEW(getCardType(CardType._VITALITYHEALTHREVIEW)),
    WELLNESS_DEVICES(getCardType(CardType._WELLDEVANDAPPS)),
    VITALITY_NUTRITION_ASSESSMENT(getCardType(CardType._VITNUTASSESSMENT)),
    SCREENNING_AND_VACCINATION(getCardType(CardType._SCREENANDVACC)),
    MENTAL_WELLBIENG(getCardType(CardType._MENTALWELLBEING)),
    HEALTH_PARTNERS(getCardType(CardType._HEALTHPARTNERS)),
    WELLNESS_PARTNERS(getCardType(CardType._WELLNESSPARTNERS)),
    REWARD_PARTNERS(getCardType(CardType._REWARDPARTNERS)),
    REWARDS(getCardType(CardType._REWARDS)),
    VOUCHERS(getCardType(CardType._VOUCHERS)),
    REWARD_SELECTION(getCardType(CardType._REWARDSELECTION)),
    HEALTH_SERVICES(getCardType(CardType._HEALTHSERVICES)),
    UNKNOWN("-1");

    @NonNull
    private static String getCardType(int cardType) {
        return String.valueOf(cardType);
    }

    private String typeKey;

    HomeCardType(String typeKey) {
        this.typeKey = typeKey;
    }

    public String getTypeKey() {
        return typeKey;
    }

    public static HomeCardType fromTypeKey(String typeKey) {
        switch (Integer.valueOf(typeKey)) {
            case CardType._NONSMOKERSDECL:
                return NON_SMOKERS_DECLARATION;
            case CardType._VITALITYHEALTHCHECK:
                return VITALITY_HEALTH_CHECK;
            case CardType._VITALITYHEALTHREVIEW:
                return VITALITY_HEALTH_REVIEW;
            case CardType._WELLDEVANDAPPS:
                return WELLNESS_DEVICES;
            case CardType._VITNUTASSESSMENT:
                return VITALITY_NUTRITION_ASSESSMENT;
            case CardType._SCREENANDVACC:
                return SCREENNING_AND_VACCINATION;
            case CardType._MENTALWELLBEING:
                return MENTAL_WELLBIENG;
            case CardType._ACTIVEREWARDS:
                return ACTIVE_REWARDS;
            case CardType._HEALTHPARTNERS:
                return HEALTH_PARTNERS;
            case CardType._WELLNESSPARTNERS:
                return WELLNESS_PARTNERS;
            case CardType._REWARDPARTNERS:
                return REWARD_PARTNERS;
            case CardType._REWARDS:
                return REWARDS;
            case CardType._VOUCHERS:
                return VOUCHERS;
            case CardType._REWARDSELECTION:
                return REWARD_SELECTION;
            case CardType._HEALTHSERVICES:
                return HEALTH_SERVICES;
            default:
                return UNKNOWN;
        }
    }

    public enum StatusType {
        NOT_STARTED(CardStatusType._NOTSTARTED),
        IN_PROGRESS(CardStatusType._INPROGRESS),
        DONE(CardStatusType._DONE),
        ACHIEVED(CardStatusType._ACHIEVED),
        ACTIVATED(CardStatusType._ACTIVATED),
        AVAILABLE(CardStatusType._AVAILABLE),
        CONFIRM_DETAILS(CardStatusType._CONFIRMDETAILS),
        NOT_ISSUED(CardStatusType._NOTISSUED),
        AVAILABLE_TO_REDEEM(CardStatusType._AVAILABLETOREDEEM),
        PENDING(CardStatusType._PENDING),
        UNKNOWN(-1);

        private int typeKey;

        StatusType(int typeKey) {
            this.typeKey = typeKey;
        }

        public int getTypeKey() {
            return typeKey;
        }

        public static StatusType fromTypeKey(int typeKey) {
            switch (typeKey) {
                case CardStatusType._NOTSTARTED:
                    return NOT_STARTED;
                case CardStatusType._INPROGRESS:
                    return IN_PROGRESS;
                case CardStatusType._DONE:
                    return DONE;
                case CardStatusType._ACHIEVED:
                    return ACHIEVED;
                case CardStatusType._ACTIVATED:
                    return ACTIVATED;
                case CardStatusType._AVAILABLE:
                    return AVAILABLE;
                case CardStatusType._CONFIRMDETAILS:
                    return CONFIRM_DETAILS;
                case CardStatusType._NOTISSUED:
                    return NOT_ISSUED;
                case CardStatusType._AVAILABLETOREDEEM:
                    return AVAILABLE_TO_REDEEM;
                case CardStatusType._PENDING:
                    return PENDING;
                default:
                    return UNKNOWN;
            }
        }
    }

    public enum MetadataType {
        REWARD_NAME(CardMetadataType._REWARDNAME),
        REWARD_ID(CardMetadataType._REWARDID),
        AWARDED_REWARD_ID(CardMetadataType._AWARDEDREWARDID),
        REWARD_VALUE_QUANTITY(CardMetadataType._REWARDVALUEQUANTITY),
        REWARD_VALUE_AMOUNT(CardMetadataType._REWARDVALUEAMOUNT),
        REWARD_VALUE_DESC(CardMetadataType._REWARDVALUEDESC),
        UNKNOWN(-1);

        private int typeKey;

        MetadataType(int typeKey) {
            this.typeKey = typeKey;
        }

        public int getTypeKey() {
            return typeKey;
        }

        public static MetadataType fromTypeKey(int typeKey) {
            switch (typeKey) {
                case CardMetadataType._REWARDNAME:
                    return REWARD_NAME;
                case CardMetadataType._REWARDID:
                    return REWARD_ID;
                case CardMetadataType._AWARDEDREWARDID:
                    return AWARDED_REWARD_ID;
                case CardMetadataType._REWARDVALUEQUANTITY:
                    return REWARD_VALUE_QUANTITY;
                case CardMetadataType._REWARDVALUEAMOUNT:
                    return REWARD_VALUE_AMOUNT;
                case CardMetadataType._REWARDVALUEDESC:
                    return REWARD_VALUE_DESC;
                default:
                    return UNKNOWN;
            }
        }
    }

}
