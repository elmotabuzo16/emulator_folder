package com.vitalityactive.va.activerewards.viewholder;

import android.view.View;
import android.widget.ImageView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.TitleSubtitleAndIcon;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class TitleSubtitleAndIconViewHolder extends TitleAndSubtitleViewHolder<TitleSubtitleAndIcon> {
    private final ImageView icon;

    public TitleSubtitleAndIconViewHolder(View itemView) {
        super(itemView);
        icon = (ImageView) itemView.findViewById(R.id.icon);
    }

    @Override
    public void bindWith(TitleSubtitleAndIcon dataItem) {
        super.bindWith(dataItem);

        final int iconResourceId = dataItem.getIconResourceId();

        if (iconResourceId != 0) {
            icon.setImageResource(iconResourceId);
            icon.setVisibility(View.VISIBLE);
        } else {
            icon.setVisibility(View.GONE);
        }
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<TitleSubtitleAndIcon, TitleSubtitleAndIconViewHolder> {
        @Override
        public TitleSubtitleAndIconViewHolder createViewHolder(View itemView) {
            return new TitleSubtitleAndIconViewHolder(itemView);
        }
    }
}
