package com.vitalityactive.va.activerewards.viewholder;

import android.view.View;
import android.widget.ImageView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.TitleSubtitleAndIcon;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

public class TitleSubtitleAndColoredIconViewHolder extends TitleAndSubtitleViewHolder<TitleSubtitleAndIcon> {
    private final ImageView icon;

    public TitleSubtitleAndColoredIconViewHolder(View itemView) {
        super(itemView);
        icon = (ImageView) itemView.findViewById(R.id.icon);
    }

    @Override
    public void bindWith(TitleSubtitleAndIcon dataItem) {
        super.bindWith(dataItem);

        final int iconResourceId = dataItem.getIconResourceId();

        if (iconResourceId != 0) {
            icon.setImageResource(iconResourceId);

            int color = ViewUtilities.getColorPrimaryFromTheme(itemView);
            icon.setColorFilter(color);
        }
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<TitleSubtitleAndIcon, TitleSubtitleAndColoredIconViewHolder> {
        @Override
        public TitleSubtitleAndColoredIconViewHolder createViewHolder(View itemView) {
            return new TitleSubtitleAndColoredIconViewHolder(itemView);
        }
    }
}
