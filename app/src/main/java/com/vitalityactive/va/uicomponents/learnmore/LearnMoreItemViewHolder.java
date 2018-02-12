package com.vitalityactive.va.uicomponents.learnmore;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.viewholder.TitleAndSubtitleViewHolder;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

public class LearnMoreItemViewHolder extends TitleAndSubtitleViewHolder<LearnMoreItem> {
    private final ImageView ivIcon;
    private final TextView tvLink;

    public LearnMoreItemViewHolder(View itemView) {
        super(itemView);
        ivIcon = (ImageView) itemView.findViewById(R.id.icon);
        tvLink = (TextView) itemView.findViewById(R.id.link);
    }

    @Override
    public void bindWith(LearnMoreItem dataItem) {
        super.bindWith(dataItem);

        final int iconResourceId = dataItem.getIconResourceId();

        if (iconResourceId != 0) {
            if (dataItem.isIconTinted() && (dataItem.getResourceTintColor() != 0 || dataItem.getHexTintColor() != 0)) {
                Drawable drawable = ViewUtilities.tintDrawable(ContextCompat.getDrawable(itemView.getContext(), iconResourceId),
                        dataItem.getResourceTintColor() != 0 ? ContextCompat.getColor(itemView.getContext(), dataItem.getResourceTintColor()) : dataItem.getHexTintColor());
                ivIcon.setImageDrawable(drawable);
            } else {
                ivIcon.setImageResource(iconResourceId);
            }
            ivIcon.setVisibility(View.VISIBLE);
        } else {
            ivIcon.setVisibility(View.GONE);
        }

        final String link = dataItem.getLink();
        if (TextUtils.isEmpty(link)) {
            tvLink.setVisibility(View.GONE);
        } else {
            tvLink.setText(link);
            tvLink.setVisibility(View.VISIBLE);
            if (dataItem.getResourceTintColor() != 0) {
                tvLink.setTextColor(ContextCompat.getColor(itemView.getContext(), dataItem.getResourceTintColor()));
            }
            if (dataItem.getOnClickListener() != null) {
                tvLink.setOnClickListener(dataItem.getOnClickListener());
            }
        }
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<LearnMoreItem, LearnMoreItemViewHolder> {
        @Override
        public LearnMoreItemViewHolder createViewHolder(View itemView) {
            return new LearnMoreItemViewHolder(itemView);
        }
    }
}