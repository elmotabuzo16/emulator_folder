package com.vitalityactive.va.wellnessdevices.landing.uicontainers;

import android.view.View;

import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class GetLinkingContainer extends GenericRecyclerViewAdapter.ViewHolder<String> {

    public GetLinkingContainer(View itemView) {
        super(itemView);
    }

    @Override
    public void bindWith(String dataItem) {
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<String, GetLinkingContainer> {

        @Override
        public GetLinkingContainer createViewHolder(View itemView) {
            return new GetLinkingContainer(itemView);
        }
    }

}
