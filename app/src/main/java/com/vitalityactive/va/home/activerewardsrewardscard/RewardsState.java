package com.vitalityactive.va.home.activerewardsrewardscard;

import android.support.annotation.StringRes;

import com.vitalityactive.va.R;

enum RewardsState {
    LEARN_MORE(R.string.AR_homescreen_card_earn_rewards_title_786,
            0,
            R.string.learn_more_button_title_104),
    CHOOSE_REWARD(R.string.AR_rewards_earned_one_reward_title_782,
            R.string.AR_rewards_expires_soon_title_784,
            R.string.AR_rewards_choose_reward_title_724),
    CHOOSE_REWARDS(R.string.AR_rewards_earned_title_782,
            R.string.AR_rewards_expiring_soon_title_783,
            R.string.card_action_title_choose_rewards),
    SPIN_NOW(R.string.AR_rewards_earned_one_reward_title_782,
            R.string.AR_rewards_expires_soon_title_784,
            R.string.AR_rewards_spin_now_title_704),
    VIEW_AVAILABLE_SPINS(R.string.AR_rewards_earned_title_782,
            R.string.AR_rewards_expiring_soon_title_783,
            R.string.AR_homescreen_card_available_spins_button_title_787);

    private final @StringRes
    int mainText;
    private final @StringRes
    int subText;
    private final @StringRes
    int actionText;

    RewardsState(@StringRes int mainText,
                 @StringRes int subText,
                 @StringRes int actionText) {
        this.mainText = mainText;
        this.subText = subText;
        this.actionText = actionText;
    }

    public int getMainText() {
        return mainText;
    }

    public int getSubText() {
        return subText;
    }

    public int getActionText() {
        return actionText;
    }

}
