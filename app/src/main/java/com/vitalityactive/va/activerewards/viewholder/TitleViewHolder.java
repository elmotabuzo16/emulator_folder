package com.vitalityactive.va.activerewards.viewholder;

import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class TitleViewHolder extends GenericRecyclerViewAdapter.ViewHolder<String> {
    private final TextView title;

    public TitleViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
    }

    @Override
    public void bindWith(String device) {
        title.setText(device);
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<String, TitleViewHolder> {
        @Override
        public TitleViewHolder createViewHolder(View itemView) {
            return new TitleViewHolder(itemView);
        }
    }
}
