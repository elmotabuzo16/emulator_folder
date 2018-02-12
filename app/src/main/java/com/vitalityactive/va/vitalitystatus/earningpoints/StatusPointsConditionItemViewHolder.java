package com.vitalityactive.va.vitalitystatus.earningpoints;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.register.view.SimpleTextViewHolder;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.wellnessdevices.dto.PointsConditionsDto;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.MeasurementContentFromResourceString;

import java.util.ArrayList;
import java.util.List;

class StatusPointsConditionItemViewHolder extends GenericRecyclerViewAdapter.ViewHolder<StatusPointsConditionItem> {
    private final MeasurementContentFromResourceString uomProvider;
    private final TextView title;
    private RecyclerView recyclerView;

    private StatusPointsConditionItemViewHolder(View itemView, MeasurementContentFromResourceString uomProvider) {
        super(itemView);
        this.uomProvider = uomProvider;

        title = itemView.findViewById(R.id.title);
        recyclerView = itemView.findViewById(R.id.recycler_view);
    }

    @Override
    public void bindWith(StatusPointsConditionItem dataItem) {
        title.setText(getPointsTitle(dataItem));

        setCondition(dataItem);
    }

    private void setCondition(StatusPointsConditionItem dataItem) {
        List<GenericRecyclerViewAdapter> adapters = new ArrayList<>();

        for (PointsConditionsDto pointsConditionsDto : dataItem.getConditions()) {
            String conditionString = String.valueOf(uomProvider.getConditionString(pointsConditionsDto));

            adapters.add(new GenericRecyclerViewAdapter<>(itemView.getContext(),
                    conditionString,
                    R.layout.status_points_point_item,
                    new SimpleTextViewHolder.Factory()));
        }

        recyclerView.setAdapter(new ContainersRecyclerViewAdapter(adapters));
    }

    private String getPointsTitle(StatusPointsConditionItem dataItem) {
        String placeHolder = itemView.getContext().getString(R.string.Status_points_progress_838);
        return String.format(placeHolder, dataItem.getPotentialPoints());
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<StatusPointsConditionItem,
            GenericRecyclerViewAdapter.ViewHolder<StatusPointsConditionItem>> {
        private MeasurementContentFromResourceString uomProvider;

        public Factory(MeasurementContentFromResourceString uomProvider) {
            this.uomProvider = uomProvider;
        }

        @Override
        public GenericRecyclerViewAdapter.ViewHolder<StatusPointsConditionItem> createViewHolder(View itemView) {
            return new StatusPointsConditionItemViewHolder(itemView, uomProvider);
        }
    }

}
