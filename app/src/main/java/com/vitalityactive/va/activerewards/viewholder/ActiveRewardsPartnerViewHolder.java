package com.vitalityactive.va.activerewards.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.rewards.content.RewardPartnerContent;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

public class ActiveRewardsPartnerViewHolder extends GenericRecyclerViewAdapter.ViewHolder<RewardPartnerContent>{
    private final TextView title;
    private final TextView subtitle;
    private final ImageView icon;

    private ActiveRewardsPartnerViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        subtitle = itemView.findViewById(R.id.subtitle);
        icon = itemView.findViewById(R.id.icon);
    }

    @Override
    public void bindWith(RewardPartnerContent dataItem) {
        ViewUtilities.setTextAndMakeVisibleIfPopulated(title, itemView.getContext().getString(dataItem.getPartnerName()));
        ViewUtilities.setTextAndMakeVisibleIfPopulated(subtitle, itemView.getContext().getString(dataItem.getPartnerVoucherDescription()));

        final int iconResourceId = dataItem.getLogoResourceId();

        if (iconResourceId != 0) {
            icon.setImageResource(iconResourceId);
            icon.setVisibility(View.VISIBLE);
        } else {
            icon.setVisibility(View.GONE);
        }
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<RewardPartnerContent, ActiveRewardsPartnerViewHolder> {
        @Override
        public ActiveRewardsPartnerViewHolder createViewHolder(View itemView) {
            return new ActiveRewardsPartnerViewHolder(itemView);
        }
    }
}
