package com.vitalityactive.va.activerewards.rewards.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.rewards.content.RewardVoucherContent;
import com.vitalityactive.va.dto.PartnerItemDTO;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.CMSImageLoader;
import com.vitalityactive.va.utilities.ViewUtilities;

public class ActiveRewardsPartnerViewHolder extends GenericRecyclerViewAdapter.ViewHolder<PartnerItemDTO> {
    private final TextView title;
    private final TextView subtitle;
    private final ImageView icon;
    private final CMSImageLoader cmsImageLoader;
    private final OnPartnerClickedListener clickedListener;

    private ActiveRewardsPartnerViewHolder(View itemView, CMSImageLoader cmsImageLoader, OnPartnerClickedListener clickedListener) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        subtitle = itemView.findViewById(R.id.subtitle);
        icon = itemView.findViewById(R.id.icon);
        this.cmsImageLoader = cmsImageLoader;
        this.clickedListener = clickedListener;
    }

    @Override
    public void bindWith(final PartnerItemDTO dataItem) {
        ViewUtilities.setTextAndMakeVisibleIfPopulated(title, dataItem.title);
        int voucherDescriptionResId = RewardVoucherContent.fromPartnerId(dataItem.id).getPartnerVoucherDescription();
        ViewUtilities.setTextAndMakeVisibleIfPopulated(subtitle, subtitle.getContext().getString(voucherDescriptionResId));

        cmsImageLoader.loadImage(icon, dataItem.logoFileName, R.drawable.img_placeholder);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedListener.onPartnerClicked(dataItem);
            }
        });
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<PartnerItemDTO, ActiveRewardsPartnerViewHolder> {
        private final CMSImageLoader cmsImageLoader;
        private final OnPartnerClickedListener clickedListener;

        public Factory(CMSImageLoader cmsImageLoader, OnPartnerClickedListener clickedListener) {
            this.cmsImageLoader = cmsImageLoader;
            this.clickedListener = clickedListener;
        }

        @Override
        public ActiveRewardsPartnerViewHolder createViewHolder(View itemView) {
            return new ActiveRewardsPartnerViewHolder(itemView, cmsImageLoader, clickedListener);
        }
    }

    public interface OnPartnerClickedListener {
        void onPartnerClicked(PartnerItemDTO reward);
    }
}
