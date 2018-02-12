package com.vitalityactive.va.myhealth.vitalityageprofile;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.myhealth.content.AttributeItem;
import com.vitalityactive.va.myhealth.content.FeedbackItem;
import com.vitalityactive.va.myhealth.shared.MyHealthUtils;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

public class MyHealthVitalityAgeTipsMoreResultsSummarySubViewHolder extends GenericRecyclerViewAdapter.ViewHolder<AttributeItem> {

    TextView attributeTitleView;
    TextView attributeValueView;
    TextView feedbackNameView;
    View sectionTitleLayout;
    View itemDivider;
    Context context;
    View contentLayout;
    GenericRecyclerViewAdapter.OnItemClickListener<AttributeItem> onItemClickListener;

    public MyHealthVitalityAgeTipsMoreResultsSummarySubViewHolder(View itemView, GenericRecyclerViewAdapter.OnItemClickListener<AttributeItem> onItemClickListener) {
        super(itemView);
        context = itemView.getContext();
        attributeTitleView = itemView.findViewById(R.id.myhealth_vitalityage_profile_item1);
        attributeValueView = itemView.findViewById(R.id.myhealth_vitalityage_profile_item2);
        feedbackNameView = itemView.findViewById(R.id.myhealth_vitalityage_profile_feedback_name);
        sectionTitleLayout = itemView.findViewById(R.id.section_title_layout);
        itemDivider = itemView.findViewById(R.id.item_divider);
        contentLayout = itemView.findViewById(R.id.content_layout);
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void bindWith(final AttributeItem dataItem) {
        if (dataItem != null) {
            ViewUtilities.setTextOfView(attributeTitleView, dataItem.getAttributeTitle());
            if (dataItem.getDisplayValue() != null) {
                ViewUtilities.setTextOfView(attributeValueView, MyHealthUtils.attemptRoundingUp(dataItem.getDisplayValue()));
            }
            if (dataItem.getFeedbackItems() != null && !dataItem.getFeedbackItems().isEmpty()) {
                FeedbackItem feedbackTip = MyHealthUtils.getDisplayableFeedback(dataItem.getFeedbackItems());
                if (feedbackTip.getFeedbackName() != null) {
                    ViewUtilities.setTextOfView(feedbackNameView, feedbackTip.getFeedbackName());
                } else {
                    feedbackNameView.setVisibility(View.GONE);
                }
            } else {
                feedbackNameView.setVisibility(View.GONE);
            }
            contentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClicked(1, dataItem);
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
            implements GenericRecyclerViewAdapter.IViewHolderFactory<AttributeItem,
            MyHealthVitalityAgeTipsMoreResultsSummarySubViewHolder> {
        GenericRecyclerViewAdapter.OnItemClickListener<AttributeItem> onItemClickListener;

        public Factory(GenericRecyclerViewAdapter.OnItemClickListener<AttributeItem> onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public MyHealthVitalityAgeTipsMoreResultsSummarySubViewHolder createViewHolder(View itemView) {
            return new MyHealthVitalityAgeTipsMoreResultsSummarySubViewHolder(itemView, onItemClickListener);
        }
    }

}