package com.vitalityactive.va.home.activerewardsrewardscard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.RewardId;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dto.RewardHomeCardDTO;
import com.vitalityactive.va.home.cards.HomeScreenCardFragment;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

import javax.inject.Inject;

public class RewardsVoucherCardFragment extends HomeScreenCardFragment {

    private static final String EXTRA_REWARD_ID = "EXTRA_REWARD_ID";
    private static final String EXTRA_AWARDED_REWARD_ID = "EXTRA_AWARDED_REWARD_ID";

    @Inject
    protected RewardsHomeScreenCardRepository repository;
    @Inject
    protected DateFormattingUtilities dateFormatUtil;

    @Nullable
    private RewardHomeCardDTO card;

    @Override
    protected void setupDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected void loadData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String rewardId = bundle.getString(EXTRA_REWARD_ID);
            String awardedRewardId = bundle.getString(EXTRA_AWARDED_REWARD_ID);
            card = repository.getHomeCard(rewardId, awardedRewardId);
        }
    }

    @Override
    protected void onClicked() {
        if (card != null) {
            long rewardUniqueId = TextUtilities.getLongFromString(card.awardedRewardId);
            navigationCoordinator.navigateAfterARRewardVoucherCardTapped(getActivity(), rewardUniqueId);
        }
    }

    private long getRewardId() {
        return card == null ? 0 : TextUtilities.getLongFromString(card.rewardId);
    }

    @Override
    protected void configureView(View view) {
        if (card != null) {
            TextView heading = view.findViewById(R.id.heading);
            TextView title = view.findViewById(R.id.title);
            TextView subTitle = view.findViewById(R.id.sub_title);
            TextView action = view.findViewById(R.id.action_button);

            heading.setText(card.rewardName);

            int logoResId = getRewardLogoResourceId(card.rewardId);
            setImage(view, R.id.reward_logo, getDrawable(logoResId));
            subTitle.setText(getString(R.string.AR_voucher_expires_658,
                    dateFormatUtil.formatDateMonthYear(new LocalDate(card.validTo))));
            action.setText(R.string.card_action_title_use_reward1093);
            if (RewardId._EASYTICKETS == getRewardId()) {
                title.setText(getString(R.string.AR_rewards_reward_voucher_placeholder_9999, card.rewardValuePercent));
            } else if (RewardId._FOODPANDA == getRewardId()){
                title.setText(getString(R.string.AR_rewards_reward_voucher_placeholder_9999, card.rewardValueAmount));
            }
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.active_rewards_home_card_rewards_voucher;
    }

    public static class Factory implements HomeScreenCardFragment.Factory {

        private String rewardId;
        private String awardedRewardId;

        public Factory(String rewardId, String awardedRewardId) {
            this.rewardId = rewardId;
            this.awardedRewardId = awardedRewardId;
        }

        @Override
        public HomeScreenCardFragment buildFragment() {
            RewardsVoucherCardFragment fragment = new RewardsVoucherCardFragment();
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_REWARD_ID, rewardId);
            bundle.putString(EXTRA_AWARDED_REWARD_ID, awardedRewardId);
            fragment.setArguments(bundle);

            return fragment;
        }
    }
}
