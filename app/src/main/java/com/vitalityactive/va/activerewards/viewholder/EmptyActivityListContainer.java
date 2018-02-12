package com.vitalityactive.va.activerewards.viewholder;

import android.view.View;

import com.vitalityactive.va.activerewards.dto.ActivityItem;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class EmptyActivityListContainer extends GenericRecyclerViewAdapter.ViewHolder<ActivityItem> {

    public EmptyActivityListContainer(View itemView) {
        super(itemView);
    }

    @Override
    public void bindWith(ActivityItem dataItem) {
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<ActivityItem, EmptyActivityListContainer> {

        @Override
        public EmptyActivityListContainer createViewHolder(View itemView) {
            return new EmptyActivityListContainer(itemView);
        }
    }

}
