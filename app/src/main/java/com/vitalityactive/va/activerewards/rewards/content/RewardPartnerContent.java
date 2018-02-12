package com.vitalityactive.va.activerewards.rewards.content;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.RewardId;

public enum RewardPartnerContent {
    VITALITY_POINTS(RewardId._VITALITYPOINTS, R.drawable.cineworld_80_x_60, R.string.AR_partners_starbucks_name_734, R.string.AR_partners_starbucks_voucher_value_705, R.drawable.starbucks_200_x_150, "partner.html"),
    AMAZON(RewardId._AMAZONVOUCHER, R.drawable.amazon_reward_sml, R.string.AR_partners_starbucks_name_734, R.string.AR_partners_starbucks_voucher_value_705, R.drawable.starbucks_200_x_150, "partner.html"),
    ZAPPOS(RewardId._ZAPPOSVOUCHER, R.drawable.wholefoods_reward_sml, R.string.AR_partners_starbucks_name_734, R.string.AR_partners_starbucks_voucher_value_705, R.drawable.starbucks_200_x_150, "partner.html"),
    TOKEN(RewardId._TOKEN, R.drawable.cineworld_80_x_60, R.string.AR_partners_starbucks_name_734, R.string.AR_partners_starbucks_voucher_value_705, R.drawable.starbucks_200_x_150, "partner.html"),
    STARBUCKS_VOUCHER(RewardId._STARBUCKSVOUCHER, R.drawable.starbucks_80_x_60, R.string.AR_partners_starbucks_name_734, R.string.AR_partners_starbucks_voucher_value_705, R.drawable.starbucks_200_x_150, "partner.html"),
    VUE_MOVIE_TICKET(RewardId._VUE, R.drawable.vue_80_x_60, R.string.AR_partners_vue_name_1055, R.string.AR_partners_vue_voucher_value_1057, R.drawable.vue_200_x_150, "partner.html"),
    CINEWORLD_MOVIE_TICKET(RewardId._CINEWORLD, R.drawable.cineworld_80_x_60, R.string.AR_partners_cineworld_name_1054, R.string.AR_partners_cineworld_voucher_value_1056, R.drawable.cineworld_200_x_150, "partner.html"),
    CINEWORLD_OR_VUE(RewardId._CINEWORLDORVUE, R.drawable.cineworld_vue_80_x_60, R.string.AR_partners_starbucks_name_734, R.string.AR_partners_starbucks_voucher_value_705, R.drawable.cineworld_vue_200_x_150, "partner.html"),
    NO_WIN(RewardId._NOWIN, R.drawable.no_reward_80_x_60, R.string.AR_partners_starbucks_name_734, R.string.AR_partners_starbucks_voucher_value_705, R.drawable.no_reward_200_x_150, "partner.html"),
    EASY_TICKETS(RewardId._EASYTICKETS, R.drawable.img_placeholder, R.string.AR_partners_easy_tickets, R.string.AR_partners_easy_tickets_voucher_value, R.drawable.img_placeholder, "easy_tickets.html"),
    FOOD_PANDA(RewardId._FOODPANDA, R.drawable.img_placeholder, R.string.AR_partners_food_panda, R.string.AR_partners_food_panda_voucher_value, R.drawable.img_placeholder, "food_panda.html");

    private final int rewardId;
    private final int logoResourceId;
    private final int partnerName;
    private final int partnerVoucherDescription;
    private int largeLogoResourceId;
    private String detailsFileName;

    RewardPartnerContent(int rewardId,
                         @DrawableRes int logoResourceId,
                         @StringRes int partnerName,
                         @StringRes int partnerVoucherDescription,
                         @DrawableRes int largeLogoResourceId,
                         String detailsFileName) {
        this.rewardId = rewardId;
        this.logoResourceId = logoResourceId;
        this.partnerName = partnerName;
        this.partnerVoucherDescription = partnerVoucherDescription;
        this.largeLogoResourceId = largeLogoResourceId;
        this.detailsFileName = detailsFileName;
    }

    public static RewardPartnerContent fromRewardId(int rewardId) {
        for (RewardPartnerContent rewardPartnerContent : RewardPartnerContent.values()) {
            if (rewardPartnerContent.getRewardId() == rewardId) {
                return rewardPartnerContent;
            }
        }
        return STARBUCKS_VOUCHER;
    }

    public int getRewardId() {
        return rewardId;
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

    public String getDetailsFileName() {
        return detailsFileName;
    }
}
