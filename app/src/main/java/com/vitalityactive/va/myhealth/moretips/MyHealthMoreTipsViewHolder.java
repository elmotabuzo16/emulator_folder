package com.vitalityactive.va.myhealth.moretips;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.myhealth.content.FeedbackTip;
import com.vitalityactive.va.myhealth.shared.MyHealthUtils;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

public class MyHealthMoreTipsViewHolder extends GenericRecyclerViewAdapter.ViewHolder<FeedbackTip> {

    private TextView feedbackTipTitleView;
    private TextView feedbackTipValueView;
    private View itemDivider;
    private Context context;
    private GenericRecyclerViewAdapter.OnItemClickListener onItemClickListener;
    private View mainView;

    public MyHealthMoreTipsViewHolder(View itemView, GenericRecyclerViewAdapter.OnItemClickListener onItemClickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.feedbackTipTitleView = itemView.findViewById(R.id.feedbacktip_title);
        this.feedbackTipValueView = itemView.findViewById(R.id.feedbacktip_value);
        this.itemDivider = itemView.findViewById(R.id.item_divider);
        this.onItemClickListener = onItemClickListener;
        this.mainView = itemView;
    }

    @Override
    public void bindWith(final FeedbackTip dataItem) {
        if (dataItem != null) {
            ViewUtilities.setTextOfView(feedbackTipTitleView, dataItem.getFeedbackTipName());
            ViewUtilities.setTextOfView(feedbackTipValueView, MyHealthUtils.attemptRoundingUp(dataItem.getFeedbackTipNote()));
            mainView.setOnClickListener(view -> onItemClickListener.onClicked(1, dataItem));
        }
        toggleItemDividers();
    }

    private void toggleItemDividers() {
        if (getAdapterPosition() == 0) {
            itemDivider.setVisibility(View.GONE);
        } else {
            itemDivider.setVisibility(View.VISIBLE);
        }
    }


    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<FeedbackTip,
            MyHealthMoreTipsViewHolder> {
        final GenericRecyclerViewAdapter.OnItemClickListener onItemClickListener;

        public Factory(GenericRecyclerViewAdapter.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public MyHealthMoreTipsViewHolder createViewHolder(View itemView) {
            return new MyHealthMoreTipsViewHolder(itemView, onItemClickListener);
        }
    }
}
