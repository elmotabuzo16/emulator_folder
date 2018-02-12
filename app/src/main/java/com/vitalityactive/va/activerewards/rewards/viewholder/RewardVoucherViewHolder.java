package com.vitalityactive.va.activerewards.rewards.viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.rewards.content.RewardPartnerContent;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.activerewards.rewards.persistence.RewardsRepository;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

public class RewardVoucherViewHolder extends GenericRecyclerViewAdapter.ViewHolder<RewardVoucherDTO> {
    private final TextView rewardName;
    private final OnRewardVoucherClickedListener clickedListener;
    private final TextView expiryDate;
    private final ImageView icon;
    private final TextView rewardDescription;

    private RewardVoucherViewHolder(View itemView, OnRewardVoucherClickedListener clickedListener) {
        super(itemView);
        rewardName = itemView.findViewById(R.id.reward_name);
        expiryDate = itemView.findViewById(R.id.expiry_date);
        rewardDescription = itemView.findViewById(R.id.reward_description);
        icon = itemView.findViewById(R.id.icon);
        this.clickedListener = clickedListener;
    }

    @Override
    public void bindWith(final RewardVoucherDTO dataItem) {
        rewardName.setText(dataItem.name);
        if (dataItem.showCodePending) {
            expiryDate.setText(itemView.getResources().getString(R.string.AR_rewards_voucher_code_pending_1044));
        } else {
            expiryDate.setText(itemView.getResources().getString(R.string.AR_voucher_expires_658, getText(dataItem.expiryDate)));
        }
        String description = RewardsRepository.getRewardDescription(dataItem.rewardValue, dataItem.rewardType);
        rewardDescription.setText(description);
        icon.setImageResource(RewardPartnerContent.fromRewardId(dataItem.rewardId).getLogoResourceId());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedListener.onRewardVoucherClicked(dataItem);
            }
        });
    }

    @NonNull
    private String getText(LocalDate date) {
        return DateFormattingUtilities.formatDateMonthYear(itemView.getContext(), date);
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<RewardVoucherDTO, RewardVoucherViewHolder> {
        private final OnRewardVoucherClickedListener clickedListener;

        public Factory(OnRewardVoucherClickedListener clickedListener) {
            this.clickedListener = clickedListener;
        }

        @Override
        public RewardVoucherViewHolder createViewHolder(View itemView) {
            return new RewardVoucherViewHolder(itemView, clickedListener);
        }
    }

    public interface OnRewardVoucherClickedListener {
        void onRewardVoucherClicked(RewardVoucherDTO reward);
    }
}
