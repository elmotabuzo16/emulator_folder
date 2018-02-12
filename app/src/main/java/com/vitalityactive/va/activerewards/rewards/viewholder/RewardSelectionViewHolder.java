package com.vitalityactive.va.activerewards.rewards.viewholder;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.rewards.content.RewardPartnerContent;
import com.vitalityactive.va.activerewards.rewards.dto.RewardSelectionDTO;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class RewardSelectionViewHolder extends GenericRecyclerViewAdapter.ViewHolder<RewardSelectionDTO> {
    private final OnRewardSelectionToggledListener selectionListener;

    private final ImageView logo;
    private final TextView name;
    private final TextView desc;
    private final CompoundButton check;

    public RewardSelectionViewHolder(View itemView, OnRewardSelectionToggledListener selectionListener) {
        super(itemView);
        this.selectionListener = selectionListener;
        logo = itemView.findViewById(R.id.reward_partner_logo);
        name = itemView.findViewById(R.id.reward_selection_name);
        desc = itemView.findViewById(R.id.reward_selection_description);
        check = itemView.findViewById(R.id.reward_selection_check);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check.setChecked(true);
            }
        });
    }

    @Override
    public void bindWith(final RewardSelectionDTO reward) {
        int logoResId = RewardPartnerContent.fromRewardId(reward.rewardId).getLogoResourceId();
        logo.setImageResource(logoResId);
        name.setText(reward.rewardName);
        desc.setText(reward.rewardDescription);
        check.setChecked(reward.selected);

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                selectionListener.onRewardSelectionToggled(reward, isChecked);
            }
        });
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<RewardSelectionDTO, RewardSelectionViewHolder> {
        private final OnRewardSelectionToggledListener toggledListener;

        public Factory(OnRewardSelectionToggledListener toggledListener) {
            this.toggledListener = toggledListener;
        }

        @Override
        public RewardSelectionViewHolder createViewHolder(View itemView) {
            return new RewardSelectionViewHolder(itemView, toggledListener);
        }
    }

    public interface OnRewardSelectionToggledListener {
        void onRewardSelectionToggled(RewardSelectionDTO reward, boolean isChecked);
    }
}
