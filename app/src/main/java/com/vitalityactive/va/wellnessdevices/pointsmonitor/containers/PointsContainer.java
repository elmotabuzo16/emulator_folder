package com.vitalityactive.va.wellnessdevices.pointsmonitor.containers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.MeasurementContentFromResourceString;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.models.PointsEntryDetailsModel;

import java.util.ArrayList;
import java.util.List;

public class PointsContainer extends GenericTitledListContainerWithAdapter.TitledListWithAdapter {
    private GenericRecyclerViewAdapter<PointsEntryDetailsModel, PointRangeItemViewHolder> adapter;

    public PointsContainer(Context context,
                           GenericRecyclerViewAdapter.OnItemClickListener<PointsEntryDetailsModel> clickListener,
                           MeasurementContentFromResourceString uomProvider) {
        super("");
        this.adapter = getContentAdapter(context, clickListener, uomProvider);
    }

    @Override
    public RecyclerView.Adapter buildAdapter(Context context) {
        return adapter;
    }

    public void setPointsRanges(List<PointsEntryDetailsModel> pointsItems) {
        adapter.replaceData(pointsItems);
    }

    public List<PointsEntryDetailsModel> getPointsRanges() {
        return adapter.getData();
    }

    public boolean isEmpty() {
        return getPointsRanges().isEmpty();
    }

    private static GenericRecyclerViewAdapter<PointsEntryDetailsModel, PointRangeItemViewHolder> getContentAdapter(Context context,
                                                                                                                   GenericRecyclerViewAdapter.OnItemClickListener<PointsEntryDetailsModel> clickedListener,
                                                                                                                   MeasurementContentFromResourceString uomProvider) {
        return new GenericRecyclerViewAdapter<>(context,
                new ArrayList<PointsEntryDetailsModel>(),
                R.layout.view_wd_points_range_list_item,
                new PointRangeItemViewHolder.Factory(context, uomProvider),
                clickedListener);
    }
}
