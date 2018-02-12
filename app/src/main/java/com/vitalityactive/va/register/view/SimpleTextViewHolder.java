package com.vitalityactive.va.register.view;

import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class SimpleTextViewHolder extends GenericRecyclerViewAdapter.ViewHolder<String> {
    private final TextView text;

    @Override
    public void bindWith(String dataItem) {
        text.setText(dataItem);
    }

    private SimpleTextViewHolder(View itemView) {
        super(itemView);
        text = (TextView) itemView.findViewById(R.id.text);
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<String, SimpleTextViewHolder> {
        @Override
        public SimpleTextViewHolder createViewHolder(View itemView) {
            return new SimpleTextViewHolder(itemView);
        }
    }
}
