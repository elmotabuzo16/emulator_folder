package com.vitalityactive.va.myhealth.healthattributes;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.myhealth.content.FeedbackTip;
import com.vitalityactive.va.myhealth.content.FeedbackTipItem;
import com.vitalityactive.va.myhealth.content.MyHealthContent;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

public class HealthAttributeFeedbackTipMainViewholder extends GenericRecyclerViewAdapter.ViewHolder<FeedbackTipItem> implements GenericRecyclerViewAdapter.OnItemClickListener<FeedbackTip> {

    RecyclerView tipsRecyclerView;
    ContainersRecyclerViewAdapter containersRecyclerViewAdapter;
    Context context;
    View sectionTitle;
    ImageView sectionIcon;
    TextView moreTipsView;
    View.OnClickListener onMoreTipsClickListener;
    GenericRecyclerViewAdapter.OnItemClickListener tipDetailClickListener;
    int globalTintColor;

    public HealthAttributeFeedbackTipMainViewholder(View itemView, View.OnClickListener onMoreTipsClickListener, GenericRecyclerViewAdapter.OnItemClickListener onItemClickListener, int globalTintColor) {
        super(itemView);
        context = itemView.getContext();
        tipsRecyclerView = itemView.findViewById(R.id.feedback_attribute_tips_recyclerview);
        sectionTitle = itemView.findViewById(R.id.section_title);
        sectionIcon = itemView.findViewById(R.id.section_icon);
        moreTipsView = itemView.findViewById(R.id.more_tips);
        this.onMoreTipsClickListener = onMoreTipsClickListener;
        this.tipDetailClickListener = onItemClickListener;
        this.globalTintColor = globalTintColor;
    }

    @Override
    public void bindWith(final FeedbackTipItem feedbackTipItem) {
        if (feedbackTipItem != null) {
            ViewUtilities.setTextOfView(sectionTitle, feedbackTipItem.getSectionTitle());
            containersRecyclerViewAdapter = new ContainersRecyclerViewAdapter(createAttributeFeedbackTipsAdapters(getFeedbackTipSublist(feedbackTipItem.getFeedbackTips())));
            tipsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            tipsRecyclerView.setAdapter(containersRecyclerViewAdapter);
            hideMoreTipsIfAllTipsShown(feedbackTipItem.getFeedbackTips());
            moreTipsView.setOnClickListener(onMoreTipsClickListener);
            setButtonTint(moreTipsView);
        }
    }

    private void setButtonTint(TextView moreTipsButton) {
        try {
            moreTipsButton.setTextColor(globalTintColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<FeedbackTip> getFeedbackTipSublist(List<FeedbackTip> feedbackTips) {
        if (feedbackTips != null && feedbackTips.size() > MyHealthContent.MAX_TIPS_TO_SHOW) {
            return feedbackTips.subList(0, MyHealthContent.MAX_TIPS_TO_SHOW);
        }
        return feedbackTips;
    }

    private void hideMoreTipsIfAllTipsShown(List<FeedbackTip> feedbackTips) {
        if (feedbackTips == null || !(feedbackTips.size() > MyHealthContent.MAX_TIPS_TO_SHOW)) {
            moreTipsView.setVisibility(View.GONE);
        }
    }

    private List<GenericRecyclerViewAdapter> createAttributeFeedbackTipsAdapters(List<FeedbackTip> feedbackTips) {
        List<GenericRecyclerViewAdapter> viewAdapters = new ArrayList<>();
        if (feedbackTips != null) {
            for (FeedbackTip feedbackTip : feedbackTips) {
                if (feedbackTip.getFeedbackTipName() != null) {
                    viewAdapters.add(new GenericRecyclerViewAdapter<>(context, feedbackTip, R.layout.myhealth_feedback_attribute_tips_item, new MyHealthAttributeTipViewHolder.Factory(this)));
                }
            }
        }
        return viewAdapters;
    }

    @Override
    public void onClicked(int position, FeedbackTip feedbackTip) {
        tipDetailClickListener.onClicked(position, feedbackTip);
    }


    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<FeedbackTipItem,
            HealthAttributeFeedbackTipMainViewholder> {

        View.OnClickListener onMoreTipsClickListener;
        GenericRecyclerViewAdapter.OnItemClickListener tipDetailClickListener;
        int globalTintColor;

        public Factory(View.OnClickListener onMoreTipsClickListener, GenericRecyclerViewAdapter.OnItemClickListener tipDetailClickListener, int globalTintColor) {
            this.onMoreTipsClickListener = onMoreTipsClickListener;
            this.tipDetailClickListener = tipDetailClickListener;
            this.globalTintColor = globalTintColor;
        }

        @Override
        public HealthAttributeFeedbackTipMainViewholder createViewHolder(View itemView) {
            return new HealthAttributeFeedbackTipMainViewholder(itemView, onMoreTipsClickListener, tipDetailClickListener, globalTintColor);
        }
    }

}
