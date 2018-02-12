package com.vitalityactive.va.partnerjourney.home;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.vitalityactive.va.R;

public enum PartnerTypeHomeCardConfiguration {
    HEALTH_REWARDS(R.drawable.health_partners,
            R.string.card_heading_health_partners_843,
            R.drawable.health,
            R.string.card_title_health_rewards_839,
            R.string.card_action_title_view_partners_842,
            R.color.jungle_green),
    WELLNESS_REWARDS(R.drawable.wellness_partners,
            R.string.card_heading_wellness_partners_844,
            R.drawable.wellness,
            R.string.card_title_wellness_rewards_840,
            R.string.card_action_title_view_partners_842,
            R.color.water_blue),
    GREAT_REWARDS(R.drawable.reward_partners,
            R.string.card_heading_reward_partners_845,
            R.drawable.rewards_partner,
            R.string.card_title_great_rewards_841,
            R.string.card_action_title_view_partners_842,
            R.color.salmon_pink),
    HEALTH_SERVICES(R.drawable.family_medical_history,
            R.string.health_services_title_1331,
            R.drawable.health_services,
            R.string.card_title_your_health_services_1332,
            R.string.card_action_title_view_services_1333,
            R.color.water_blue);

    private final @DrawableRes int headerIcon;
    private final @StringRes int headerText;
    private final @DrawableRes int mainIcon;
    private final @StringRes int mainText;
    private final @StringRes int actionText;
    private final @ColorRes int color;

    PartnerTypeHomeCardConfiguration(@DrawableRes int headerIcon,
                                     @StringRes int headerText,
                                     @DrawableRes int mainIcon,
                                     @StringRes int mainText,
                                     @StringRes int actionText,
                                     @ColorRes int color){
        this.headerIcon = headerIcon;
        this.headerText = headerText;
        this.mainIcon = mainIcon;
        this.mainText = mainText;
        this.actionText = actionText;
        this.color = color;
    }

    public int getHeaderIcon() {
        return headerIcon;
    }

    public int getHeaderText() {
        return headerText;
    }

    public int getMainIcon() {
        return mainIcon;
    }

    public int getMainText() {
        return mainText;
    }

    public int getActionText() {
        return actionText;
    }

    public int getColor() {
        return color;
    }
}
