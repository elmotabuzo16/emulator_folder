package com.vitalityactive.va.vhc.captureresults.viewholder;

import android.view.View;

import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class EmptyViewHolder
        implements com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter.IViewHolderFactory<CaptureMeasurementRootTitledList,
            com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter.ViewHolder<CaptureMeasurementRootTitledList>> {
    @Override
    public GenericRecyclerViewAdapter.ViewHolder<CaptureMeasurementRootTitledList> createViewHolder(View itemView) {
        return new GenericRecyclerViewAdapter.ViewHolder<CaptureMeasurementRootTitledList>(itemView) {
            @Override
            public void bindWith(CaptureMeasurementRootTitledList dataItem) {

            }
        };
    }
}
