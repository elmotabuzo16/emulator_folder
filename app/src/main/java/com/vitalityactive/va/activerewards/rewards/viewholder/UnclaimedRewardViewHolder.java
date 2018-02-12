package com.vitalityactive.va.activerewards.rewards.viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.rewards.dto.UnclaimedRewardDTO;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

public class UnclaimedRewardViewHolder extends GenericRecyclerViewAdapter.ViewHolder<UnclaimedRewardDTO> {
    private final TextView awardedOn;
    private final TextView reward_button;
    private final OnUnclaimedRewardClickedListener clickedListener;
    private final RelativeLayout reward_layout;

    private UnclaimedRewardViewHolder(View itemView, OnUnclaimedRewardClickedListener clickedListener, boolean rewardChoice) {
        super(itemView);
        reward_layout = itemView.findViewById(R.id.reward_layout);
        awardedOn = itemView.findViewById(R.id.reward_achieved);
        reward_button = itemView.findViewById(R.id.get_reward_button);
        if (rewardChoice) {
            reward_button.setText(R.string.AR_rewards_choose_reward_title_724);
        } else {
            reward_button.setText(R.string.AR_rewards_spin_now_title_704);
        }
        this.clickedListener = clickedListener;
    }

    @Override
    public void bindWith(final UnclaimedRewardDTO dataItem) {
        awardedOn.setText(itemView.getResources().getString(R.string.AR_rewards_credit_info_691, formatDate(dataItem.awardedOnDate), formatDate(dataItem.expiryDate)));

        reward_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedListener.onUnclaimedRewardClicked(dataItem);
            }
        });
    }

    @NonNull
    private String formatDate(LocalDate date) {
        return DateFormattingUtilities.formatDateMonthYear(itemView.getContext(), date);
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<UnclaimedRewardDTO, UnclaimedRewardViewHolder> {
        private final OnUnclaimedRewardClickedListener clickedListener;
        private final boolean rewardChoice;

        public Factory(OnUnclaimedRewardClickedListener clickedListener, boolean rewardChoice) {
            this.clickedListener = clickedListener;
            this.rewardChoice = rewardChoice;
        }

        @Override
        public UnclaimedRewardViewHolder createViewHolder(View itemView) {
            return new UnclaimedRewardViewHolder(itemView, clickedListener, rewardChoice);
        }
    }

    public interface OnUnclaimedRewardClickedListener {
        void onUnclaimedRewardClicked(UnclaimedRewardDTO rewardDetail);
    }
}
