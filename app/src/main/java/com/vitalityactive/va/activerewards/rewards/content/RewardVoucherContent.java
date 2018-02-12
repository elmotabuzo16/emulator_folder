package com.vitalityactive.va.activerewards.rewards.content;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.vitalityactive.va.R;

public enum RewardVoucherContent {
    STARBUCKS_VOUCHER(116, R.drawable.starbucks_80_x_60, R.string.AR_partners_starbucks_name_734, R.string.AR_partners_starbucks_voucher_value_705, R.drawable.starbucks_200_x_150),
    VUE_MOVIE_TICKET(120, R.drawable.vue_80_x_60, R.string.AR_partners_vue_name_1055, R.string.AR_partners_vue_voucher_value_1057, R.drawable.vue_200_x_150),
    CINEWORLD_MOVIE_TICKET(118, R.drawable.cineworld_80_x_60, R.string.AR_partners_cineworld_name_1054, R.string.AR_partners_cineworld_voucher_value_1056, R.drawable.cineworld_200_x_150);

    private final long partnerId;
    private final int logoResourceId;
    private final int partnerName;
    private final int partnerVoucherDescription;
    private int largeLogoResourceId;

    RewardVoucherContent(long partnerId,
                         @DrawableRes int logoResourceId,
                         @StringRes int partnerName,
                         @StringRes int partnerVoucherDescription,
                         @DrawableRes int largeLogoResourceId) {
        this.partnerId = partnerId;
        this.logoResourceId = logoResourceId;
        this.partnerName = partnerName;
        this.partnerVoucherDescription = partnerVoucherDescription;
        this.largeLogoResourceId = largeLogoResourceId;
    }

    public static RewardVoucherContent fromPartnerId(long partnerId) {
        for (RewardVoucherContent partnerContent : RewardVoucherContent.values()) {
            if (partnerContent.getPartnerId() == partnerId) {
                return partnerContent;
            }
        }
        return STARBUCKS_VOUCHER;
    }

    public long getPartnerId() {
        return partnerId;
    }

    @DrawableRes
    public int getLogoResourceId() {
        return logoResourceId;
    }

    @StringRes
    public int getPartnerName() {
        return partnerName;
    }

    @StringRes
    public int getPartnerVoucherDescription() {
        return partnerVoucherDescription;
    }

    @DrawableRes
    public int getLargeLogoResourceId() {
        return largeLogoResourceId;
    }
}
