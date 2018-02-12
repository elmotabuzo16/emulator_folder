package com.vitalityactive.va.partnerjourney.containers;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.partnerjourney.PartnerListPresenterImpl;
import com.vitalityactive.va.partnerjourney.models.PartnerItem;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class PartnerViewHolder extends GenericRecyclerViewAdapter.ViewHolder<PartnerItem> {
    private final PartnerListPresenterImpl presenter;
    private final TextView title;
    private final TextView subtitle;
    private final ImageView logo;
    private final TextView longDescription;
    private int placeholderImage;

    public PartnerViewHolder(View itemView, PartnerListPresenterImpl presenter) {
        super(itemView);
        this.presenter = presenter;
        this.placeholderImage = presenter.getPartnerType().listItemPlaceholderImage;
        title = itemView.findViewById(R.id.title);
        subtitle = itemView.findViewById(R.id.subtitle);
        longDescription = itemView.findViewById(R.id.long_description);
        logo = itemView.findViewById(R.id.partner_logo);
    }

    @Override
    public void bindWith(PartnerItem dataItem) {
        title.setText(dataItem.title);
        presenter.getCMSImageLoader().loadImage(logo, dataItem.logoFileName, placeholderImage);

        subtitle.setText(dataItem.details);
        longDescription.setText(dataItem.longDescription);
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<PartnerItem,PartnerViewHolder> {
        private final PartnerListPresenterImpl presenter;

        public Factory(PartnerListPresenterImpl presenter) {
            this.presenter = presenter;
        }

        @Override
        public PartnerViewHolder createViewHolder(View itemView) {
            return new PartnerViewHolder(itemView, presenter);
        }
    }
}
