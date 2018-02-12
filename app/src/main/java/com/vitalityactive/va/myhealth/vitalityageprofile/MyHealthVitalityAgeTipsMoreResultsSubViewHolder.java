package com.vitalityactive.va.myhealth.vitalityageprofile;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.myhealth.content.AttributeItem;
import com.vitalityactive.va.myhealth.content.FeedbackItem;
import com.vitalityactive.va.myhealth.content.FeedbackTip;
import com.vitalityactive.va.myhealth.content.MyHealthContent;
import com.vitalityactive.va.myhealth.shared.MyHealthUtils;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

public class MyHealthVitalityAgeTipsMoreResultsSubViewHolder extends GenericRecyclerViewAdapter.ViewHolder<AttributeItem> {

    GenericRecyclerViewAdapter.OnItemClickListener<AttributeItem> onItemClickListener;
    TextView attributeTitleView;
    TextView attributeValueView;
    TextView feedbackNameView;
    TextView feedbackTipNameView;
    TextView feedbackTipValueView;
    View feedbackContentView;
    View feedbackContentViewPlaceholder;
    View itemDivider;
    View feedbackTipContentLayout;
    View contentLayout;
    Context context;


    public MyHealthVitalityAgeTipsMoreResultsSubViewHolder(View itemView, GenericRecyclerViewAdapter.OnItemClickListener<AttributeItem> onItemClickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.attributeTitleView = itemView.findViewById(R.id.myhealth_vitalityage_profile_item1);
        this.attributeValueView = itemView.findViewById(R.id.myhealth_vitalityage_profile_item2);
        this.feedbackNameView = itemView.findViewById(R.id.myhealth_vitalityage_profile_feedback_name);
        this.feedbackTipNameView = itemView.findViewById(R.id.myhealth_vitalityage_whattodo_title);
        this.feedbackTipValueView = itemView.findViewById(R.id.myhealth_vitalityage_whattodo);
        this.feedbackContentView = itemView.findViewById(R.id.feedback_content_view);
        this.feedbackContentViewPlaceholder = itemView.findViewById(R.id.feedback_content_view_placeholder);
        this.itemDivider = itemView.findViewById(R.id.item_divider);
        this.feedbackTipContentLayout = itemView.findViewById(R.id.feedback_tip_content);
        this.contentLayout = itemView.findViewById(R.id.more_results_attribute_layout);
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void bindWith(final AttributeItem dataItem) {
        if (dataItem != null) {
            if (dataItem.getAttributeTitle() != null) {
                ViewUtilities.setTextOfView(attributeTitleView, dataItem.getAttributeTitle());
            }
            if (dataItem.getAttributeValue() != null) {
                ViewUtilities.setTextOfView(attributeValueView, dataItem.getDisplayValue());
            }
            if (dataItem.getFeedbackItems() != null && !dataItem.getFeedbackItems().isEmpty()) {
                FeedbackItem feedbackItem = MyHealthUtils.getDisplayableFeedback(dataItem.getFeedbackItems());
                if (feedbackItem.getFeedbackName() != null) {
                    ViewUtilities.setTextOfView(feedbackNameView, feedbackItem.getFeedbackName());
                }
                if (dataItem.getAttributeTitle() == null && dataItem.getAttributeValue() == null && feedbackItem.getFeedbackName() == null) {
                    showPlaceholder(dataItem.getSectionTypeKey());
                }
                if (feedbackItem.getFeedbackTips() != null && !feedbackItem.getFeedbackTips().isEmpty()) {
                    FeedbackTip feedbackTip = feedbackItem.getFeedbackTips().get(0);
                    if (feedbackTipNameView != null && feedbackTip.getFeedbackTipName() != null) {
                        ViewUtilities.setTextOfView(feedbackTipNameView, feedbackTip.getFeedbackTipName());
                    }
                    if (feedbackTipValueView != null && feedbackTip.getFeedbackTipNote() != null) {
                        ViewUtilities.setTextOfView(feedbackTipValueView, feedbackTip.getFeedbackTipNote());
                    }
                    if (feedbackTip.getFeedbackTipName() == null && feedbackTip.getFeedbackTipNote() == null) {
                        hideFeedbackTipSection();
                    }
                } else {
                    hideFeedbackTipSection();
                }
            } else {
                hideFeedbackTipSection();
            }
            contentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClicked(1, dataItem);
                }
            });
            toggleItemDividers();
        }
    }

    private void hideFeedbackTipSection() {
        feedbackTipContentLayout.setVisibility(View.GONE);
    }

    private void showPlaceholder(int sectionKey) {
        ViewUtilities.setTextOfView(feedbackContentViewPlaceholder, context.getString(MyHealthContent.VitalityAgeTipsFeedbackPlaceholder.getPlaceholder(sectionKey)));
        feedbackContentViewPlaceholder.setVisibility(View.VISIBLE);
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
            MyHealthVitalityAgeTipsMoreResultsSubViewHolder> {
        GenericRecyclerViewAdapter.OnItemClickListener<AttributeItem> onItemClickListener;

        public Factory(GenericRecyclerViewAdapter.OnItemClickListener<AttributeItem> onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public MyHealthVitalityAgeTipsMoreResultsSubViewHolder createViewHolder(View itemView) {
            return new MyHealthVitalityAgeTipsMoreResultsSubViewHolder(itemView, onItemClickListener);
        }
    }

}
