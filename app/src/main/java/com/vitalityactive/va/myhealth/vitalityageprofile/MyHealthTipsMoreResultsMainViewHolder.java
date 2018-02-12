package com.vitalityactive.va.myhealth.vitalityageprofile;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.myhealth.content.AttributeItem;
import com.vitalityactive.va.myhealth.content.MyHealthContent;
import com.vitalityactive.va.myhealth.content.SectionItem;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

public class MyHealthTipsMoreResultsMainViewHolder extends GenericRecyclerViewAdapter.ViewHolder<SectionItem> implements GenericRecyclerViewAdapter.OnItemClickListener<AttributeItem> {
    RecyclerView tipsRecyclerView;
    ContainersRecyclerViewAdapter containersRecyclerViewAdapter;
    Context context;
    View sectionTitle;
    GenericRecyclerViewAdapter.OnItemClickListener<AttributeItem> onItemClickListener;

    public MyHealthTipsMoreResultsMainViewHolder(View itemView, GenericRecyclerViewAdapter.OnItemClickListener<AttributeItem> onItemClickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.tipsRecyclerView = itemView.findViewById(R.id.feedback_tips_recyclerview);
        this.sectionTitle = itemView.findViewById(R.id.section_title);
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void bindWith(SectionItem sectionItem) {
        if (sectionItem != null) {
            ViewUtilities.setTextOfView(sectionTitle, sectionItem.getSectionTitle());
            containersRecyclerViewAdapter = new ContainersRecyclerViewAdapter(createFeedbackTipsAdapters(sectionItem.getAttributeItems()));
            tipsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            tipsRecyclerView.setAdapter(containersRecyclerViewAdapter);
        }
    }

    private List<GenericRecyclerViewAdapter> createFeedbackTipsAdapters(List<AttributeItem> attributeItems) {
        List<GenericRecyclerViewAdapter> viewAdapters = new ArrayList<>();
        if (attributeItems != null) {
            for (AttributeItem attrributeItem : getAttributeSublist(attributeItems)) {
                viewAdapters.add(new GenericRecyclerViewAdapter<>(context, attrributeItem, R.layout.myhealth_vitalityage_tip_more_results_item, new MyHealthVitalityAgeTipsMoreResultsSubViewHolder.Factory(this)));
            }
        }
        return viewAdapters;
    }

    private List<AttributeItem> getAttributeSublist(List<AttributeItem> attributeItems) {
        if (attributeItems != null && attributeItems.size() > MyHealthContent.MAX_TIPS_TO_SHOW) {
            return attributeItems.subList(0, MyHealthContent.MAX_TIPS_TO_SHOW);
        }
        return attributeItems;
    }

    @Override
    public void onClicked(int position, AttributeItem attributeItem) {
        onItemClickListener.onClicked(position, attributeItem);
    }


    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<SectionItem,
            MyHealthTipsMoreResultsMainViewHolder> {
        GenericRecyclerViewAdapter.OnItemClickListener<AttributeItem> onItemClickListener;

        public Factory(GenericRecyclerViewAdapter.OnItemClickListener<AttributeItem> onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public MyHealthTipsMoreResultsMainViewHolder createViewHolder(View itemView) {
            return new MyHealthTipsMoreResultsMainViewHolder(itemView, onItemClickListener);
        }
    }

}
