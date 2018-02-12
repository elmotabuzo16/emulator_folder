package com.vitalityactive.va.activerewards.rewards;


import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.utilities.ViewUtilities;

import static com.vitalityactive.va.constants.RewardId._CINEWORLD;
import static com.vitalityactive.va.constants.RewardId._CINEWORLDORVUE;
import static com.vitalityactive.va.constants.RewardId._STARBUCKSVOUCHER;
import static com.vitalityactive.va.constants.RewardId._VUE;

public class RewardVoucherActivity extends BaseRewardVoucherActivity {

    protected void setRewardSpecificInstructionsForAvailableVoucher(RewardVoucherDTO voucher) {
        CharSequence stringToDisplay = "";

        switch (voucher.rewardId) {
            case _STARBUCKSVOUCHER:
                stringToDisplay = getText(R.string.Ar_Rewards_starbucks_issued_reward_ready_for_collection_html_1085);
                break;
            case _VUE:
            case _CINEWORLD:
                stringToDisplay = getText(R.string.AR_rewards_cinema_voucher_footer_1112);
                break;
        }

        ViewUtilities.setHTMLTextAndMakeVisibleIfPopulated(voucherInstructions, stringToDisplay);
    }


    protected void setRewardSpecificInstructionsForPendingVoucher(RewardVoucherDTO voucher) {
        CharSequence stringToDisplay = "";

        switch (voucher.rewardId) {
            case _STARBUCKSVOUCHER:
                stringToDisplay = getText(R.string.ar_rewards_pending_starbucks_reward_footer_html_1079);
                break;
            case _CINEWORLDORVUE:
                stringToDisplay = getText(R.string.AR_rewards_cinema_pending_footer_1115);
                break;
            case _VUE:
            case _CINEWORLD:
                stringToDisplay = "This scenario is not part of any reward protocol, but is an unintended side effect of prolonged exposure to this project. Please report to your nearest brain matter emancipation officer.";
                break;
        }

        ViewUtilities.setHTMLTextAndMakeVisibleIfPopulated(voucherInstructions, stringToDisplay);
    }
}
