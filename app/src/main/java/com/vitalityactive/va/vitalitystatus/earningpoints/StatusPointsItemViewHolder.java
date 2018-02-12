package com.vitalityactive.va.vitalitystatus.earningpoints;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.register.view.SimpleTextViewHolder;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

class StatusPointsItemViewHolder extends GenericRecyclerViewAdapter.ViewHolder<StatusPointsItem> {

    private final RecyclerView recyclerView;
    private final TextView title;

    private StatusPointsItemViewHolder(View itemView) {
        super(itemView);

        recyclerView = itemView.findViewById(R.id.recycler_view);
        title = itemView.findViewById(R.id.title);

    }

    @Override
    public void bindWith(StatusPointsItem dataItem) {
        setTitle(dataItem);
        setupRecyclerView(dataItem);
    }

    private void setTitle(StatusPointsItem dataItem) {
        String placeHolder = itemView.getContext().getString(R.string.Status_points_progress_838);
        String pointsTitle = String.format(placeHolder, dataItem.getPotentialPoints());

        title.setText(pointsTitle);
    }

    private void setupRecyclerView(StatusPointsItem dataItem) {
        String pointsItemDetail = PointsItemDetailProvider.getItemText(dataItem.getPointsEntryType(), itemView.getContext());

        recyclerView.setAdapter(new GenericRecyclerViewAdapter<>(itemView.getContext(),
                pointsItemDetail,
                R.layout.status_points_point_item,
                new SimpleTextViewHolder.Factory()));
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<StatusPointsItem, GenericRecyclerViewAdapter.ViewHolder<StatusPointsItem>> {

        @Override
        public GenericRecyclerViewAdapter.ViewHolder<StatusPointsItem> createViewHolder(View itemView) {
            return new StatusPointsItemViewHolder(itemView);
        }
    }


}
