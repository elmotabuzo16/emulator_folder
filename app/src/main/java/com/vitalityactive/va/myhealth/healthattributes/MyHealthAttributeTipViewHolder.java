package com.vitalityactive.va.myhealth.healthattributes;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.myhealth.content.FeedbackTip;
import com.vitalityactive.va.myhealth.shared.MyHealthUtils;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

public class MyHealthAttributeTipViewHolder extends GenericRecyclerViewAdapter.ViewHolder<FeedbackTip> {

    TextView feedbackTipTitleView;
    TextView feedbackTipValueView;
    View itemDivider;
    View contentView;
    Context context;
    GenericRecyclerViewAdapter.OnItemClickListener tipDetailClickListener;

    public MyHealthAttributeTipViewHolder(View itemView, GenericRecyclerViewAdapter.OnItemClickListener tipDetailClickListener) {
        super(itemView);
        context = itemView.getContext();
        feedbackTipTitleView = itemView.findViewById(R.id.feedbacktip_title);
        feedbackTipValueView = itemView.findViewById(R.id.feedbacktip_value);
        itemDivider = itemView.findViewById(R.id.item_divider);
        this.tipDetailClickListener = tipDetailClickListener;
        this.contentView = itemView.findViewById(R.id.feedback_tip_detail);
    }

    @Override
    public void bindWith(final FeedbackTip dataItem) {
        if (dataItem != null && dataItem.getFeedbackTipName() != null) {
            ViewUtilities.setTextOfView(feedbackTipTitleView, dataItem.getFeedbackTipName());
            String note = MyHealthUtils.attemptRoundingUp(dataItem.getFeedbackTipNote());
            ViewUtilities.setTextOfView(feedbackTipValueView, note != null ? note : context.getString(R.string.feedback_tips_no_data));
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tipDetailClickListener.onClicked(1, dataItem);
                }
            });
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
            MyHealthAttributeTipViewHolder> {
        GenericRecyclerViewAdapter.OnItemClickListener tipDetailClickListener;

        public Factory(GenericRecyclerViewAdapter.OnItemClickListener tipDetailClickListener) {
            this.tipDetailClickListener = tipDetailClickListener;
        }

        @Override
        public MyHealthAttributeTipViewHolder createViewHolder(View itemView) {
            return new MyHealthAttributeTipViewHolder(itemView, tipDetailClickListener);
        }
    }
}
