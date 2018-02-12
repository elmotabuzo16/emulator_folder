package com.vitalityactive.va.home;

import com.vitalityactive.va.constants.CardItemMetadataType;
import com.vitalityactive.va.constants.CardItemStatusType;
import com.vitalityactive.va.constants.CardItemType;
import com.vitalityactive.va.constants.CardMetadataType;

public enum HomeCardItemType {
    VOUCHER(CardItemType._VOUCHER),
    DEVICE(CardItemType._DEVICE),
    REWARD_CHOICE(CardItemType._REWARDCHOICE),
    WHEEL_SPIN(CardItemType._WHEELSPIN),
    UNKNOWN(-1);

    private int typeKey;

    HomeCardItemType(int typeKey) {
        this.typeKey = typeKey;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public static HomeCardItemType fromTypeKey(int typeKey) {
        switch (typeKey) {
            case CardItemType._VOUCHER:
                return VOUCHER;
            case CardItemType._DEVICE:
                return DEVICE;
            case CardItemType._REWARDCHOICE:
                return REWARD_CHOICE;
            case CardItemType._WHEELSPIN:
                return WHEEL_SPIN;
            default:
                return UNKNOWN;
        }
    }

    public enum StatusType {
        LINKED(CardItemStatusType._LINKED),
        AVAILABLE(CardItemStatusType._AVAILABLE),
        EXPIRED(CardItemStatusType._EXPIRED),
        REDEEMED(CardItemStatusType._REDEEMED),
        DONE(CardItemStatusType._DONE),
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
                case CardItemStatusType._LINKED:
                    return LINKED;
                case CardItemStatusType._AVAILABLE:
                    return AVAILABLE;
                case CardItemStatusType._EXPIRED:
                    return EXPIRED;
                case CardItemStatusType._REDEEMED:
                    return REDEEMED;
                case CardItemStatusType._DONE:
                    return DONE;
                default:
                    return UNKNOWN;
            }
        }
    }

    public enum MetadataType {
        AWARDED_REWARD_ID(CardMetadataType._AWARDEDREWARDID),
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
                case CardItemMetadataType._AWARDEDREWARDID:
                    return AWARDED_REWARD_ID;
                default:
                    return UNKNOWN;
            }
        }
    }

}
