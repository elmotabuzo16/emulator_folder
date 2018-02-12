package com.vitalityactive.va.vhc.captureresults.viewholder;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.vhc.captureresults.CaptureResultsPresenter;
import com.vitalityactive.va.vhc.dto.MeasurementItem;

public class CaptureMeasurementViewHolder extends GenericRecyclerViewAdapter.ViewHolder<MeasurementItem> {
    private final TextView title;
    private final ImageView icon;
    private final CaptureResultsPresenter presenter;
    private final CaptureMeasurementParameters parameters;
    private TextView description;

    public CaptureMeasurementViewHolder(View itemView, CaptureResultsPresenter presenter, CaptureMeasurementParameters parameters) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        description = itemView.findViewById(R.id.vhc_measurement_item_description);
        icon = itemView.findViewById(R.id.group_icon);
        this.presenter = presenter;
        this.parameters = parameters;
    }

    @Override
    public void bindWith(MeasurementItem dataItem) {
        Drawable tintedDrawable = ViewUtilities.tintDrawable(ContextCompat.getDrawable(itemView.getContext(), dataItem.iconResourceId), parameters.tintColor);
        icon.setImageDrawable(tintedDrawable);
        title.setText(dataItem.title);
        setDescription(dataItem);
        setMeasurementItemFields(dataItem, (RecyclerView) itemView.findViewById(R.id.recycler_view));
    }

    private void setMeasurementItemFields(MeasurementItem dataItem, RecyclerView measurementSubItemsRecyclerView) {
        measurementSubItemsRecyclerView.setLayoutManager(
                new LinearLayoutManager(itemView.getContext()));

        final GenericRecyclerViewAdapter adapter =
                new GenericRecyclerViewAdapter<>(title.getContext(),
                        dataItem.measurementItemFields,
                        R.layout.vhc_measurement_item_fields,
                        new CaptureMeasurementFieldViewHolder.Factory(dataItem.type, presenter, parameters));
        measurementSubItemsRecyclerView.setAdapter(adapter);

        if (!ViewUtilities.alreadyHaveAnItemDecorator(measurementSubItemsRecyclerView)) {
            ViewUtilities.addDividers(measurementSubItemsRecyclerView.getContext(), measurementSubItemsRecyclerView);
        }
    }

    private void setDescription(MeasurementItem dataItem) {
        boolean visible = dataItem.description != null && !dataItem.description.isEmpty();
        description.setVisibility(visible ? View.VISIBLE : View.GONE);
        description.setText(dataItem.description);
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<MeasurementItem, CaptureMeasurementViewHolder> {
        private CaptureResultsPresenter presenter;
        private final CaptureMeasurementParameters parameters;

        public Factory(CaptureResultsPresenter presenter, CaptureMeasurementParameters parameters) {
            this.presenter = presenter;
            this.parameters = parameters;
        }

        @Override
        public CaptureMeasurementViewHolder createViewHolder(View itemView) {
            return new CaptureMeasurementViewHolder(itemView, presenter, parameters);
        }
    }
}
