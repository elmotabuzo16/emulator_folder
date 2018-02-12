package com.vitalityactive.va.activerewards.viewholder;

import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.TitleAndSubtitle;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

public class TitleAndSubtitleViewHolder<T extends TitleAndSubtitle> extends GenericRecyclerViewAdapter.ViewHolder<T> {
    private final TextView title;
    private final TextView subtitle;

    public TitleAndSubtitleViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        subtitle = (TextView) itemView.findViewById(R.id.subtitle);
    }

    @Override
    public void bindWith(T dataItem) {
        ViewUtilities.setTextAndMakeVisibleIfPopulated(title, dataItem.getTitle());
        ViewUtilities.setTextAndMakeVisibleIfPopulated(subtitle, dataItem.getSubtitle());
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<TitleAndSubtitle, TitleAndSubtitleViewHolder<TitleAndSubtitle>> {
        @Override
        public TitleAndSubtitleViewHolder<TitleAndSubtitle> createViewHolder(View itemView) {
            return new TitleAndSubtitleViewHolder<>(itemView);
        }
    }
}
