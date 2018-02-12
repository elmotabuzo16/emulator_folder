package com.vitalityactive.va.myhealth.vitalityageprofile;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.myhealth.content.AttributeItem;
import com.vitalityactive.va.myhealth.content.SectionItem;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;


public class MyHealthTipsMoreResultsSummaryMainViewHolder extends GenericRecyclerViewAdapter.ViewHolder<SectionItem> implements GenericRecyclerViewAdapter.OnItemClickListener<AttributeItem> {
    RecyclerView tipsRecyclerView;
    ContainersRecyclerViewAdapter containersRecyclerViewAdapter;
    Context context;
    View sectionTitle;
    GenericRecyclerViewAdapter.OnItemClickListener<AttributeItem> onItemClickListener;

    public MyHealthTipsMoreResultsSummaryMainViewHolder(View itemView, GenericRecyclerViewAdapter.OnItemClickListener<AttributeItem> onItemClickListener) {
        super(itemView);
        context = itemView.getContext();
        tipsRecyclerView = itemView.findViewById(R.id.feedback_tips_recyclerview);
        sectionTitle = itemView.findViewById(R.id.section_title);
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void bindWith(SectionItem vitalityAgeTips) {
        if (vitalityAgeTips != null) {
            ViewUtilities.setTextOfView(sectionTitle, vitalityAgeTips.getSectionTitle());
            containersRecyclerViewAdapter = new ContainersRecyclerViewAdapter(createVitalityAgeTipsSummaryAdapters(vitalityAgeTips.getAttributeItems()));
            tipsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            tipsRecyclerView.setAdapter(containersRecyclerViewAdapter);
        }
    }

    private List<GenericRecyclerViewAdapter> createVitalityAgeTipsSummaryAdapters(List<AttributeItem> attributeItems) {
        List<GenericRecyclerViewAdapter> viewAdapters = new ArrayList<>();
        if (attributeItems != null) {
            for (AttributeItem attributeItem : attributeItems) {
                viewAdapters.add(new GenericRecyclerViewAdapter<>(context, attributeItem, R.layout.myhealth_vitalityage_tip_summary_item, new MyHealthVitalityAgeTipsMoreResultsSummarySubViewHolder.Factory(this)));
            }
        }
        return viewAdapters;
    }

    @Override
    public void onClicked(int position, AttributeItem item) {
        onItemClickListener.onClicked(position, item);
    }


    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<SectionItem,
            MyHealthTipsMoreResultsSummaryMainViewHolder> {

        GenericRecyclerViewAdapter.OnItemClickListener<AttributeItem> onItemClickListener;

        public Factory(GenericRecyclerViewAdapter.OnItemClickListener<AttributeItem> onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public MyHealthTipsMoreResultsSummaryMainViewHolder createViewHolder(View itemView) {
            return new MyHealthTipsMoreResultsSummaryMainViewHolder(itemView, onItemClickListener);
        }
    }
}
