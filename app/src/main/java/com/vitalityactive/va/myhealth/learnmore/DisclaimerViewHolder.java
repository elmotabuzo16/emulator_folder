package com.vitalityactive.va.myhealth.learnmore;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class DisclaimerViewHolder extends GenericRecyclerViewAdapter.ViewHolder<DisclaimerItem> {
    private final ImageView icon;
    private final TextView itemTitle;
    Context context;
    int globalIconTint;

    public DisclaimerViewHolder(View itemView, View.OnClickListener onClickListener, int globalIconTint) {
        super(itemView);
        context = itemView.getContext();
        icon = itemView.findViewById(R.id.disclaimer_icon);
        itemTitle = itemView.findViewById(R.id.itemTitle);
        itemView.setOnClickListener(onClickListener);
        this.globalIconTint = globalIconTint;
    }

    private void setIconTint() {
        try {
            icon.setColorFilter(globalIconTint, android.graphics.PorterDuff.Mode.MULTIPLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bindWith(DisclaimerItem dataItem) {
        final int iconResourceId = dataItem.getIconResourceId();
        icon.setImageResource(iconResourceId);
        itemTitle.setText(dataItem.getTitle());
        setIconTint();
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<DisclaimerItem, DisclaimerViewHolder> {
        View.OnClickListener onClickListener;
        int globalIconTint;

        public Factory(View.OnClickListener onClickListener, int globalIconTint) {
            this.onClickListener = onClickListener;
            this.globalIconTint = globalIconTint;
        }

        @Override
        public DisclaimerViewHolder createViewHolder(View itemView) {
            return new DisclaimerViewHolder(itemView, onClickListener, globalIconTint);
        }
    }
}