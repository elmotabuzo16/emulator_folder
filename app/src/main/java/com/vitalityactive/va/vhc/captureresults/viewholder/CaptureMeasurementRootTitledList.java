package com.vitalityactive.va.vhc.captureresults.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.vhc.captureresults.CaptureResultsPresenter;
import com.vitalityactive.va.vhc.dto.MeasurementItem;

import java.util.ArrayList;
import java.util.List;

public class CaptureMeasurementRootTitledList extends GenericTitledListContainerWithAdapter.TitledListWithAdapter {
    private final CaptureResultsPresenter presenter;
    private final CaptureMeasurementParameters parameters;
    private final List<MeasurementItem> measurementItems = new ArrayList<>();
    private GenericRecyclerViewAdapter<MeasurementItem, CaptureMeasurementViewHolder> adapter;

    public CaptureMeasurementRootTitledList(CaptureResultsPresenter presenter, CaptureMeasurementParameters parameters) {
        super("");
        this.presenter = presenter;
        this.parameters = parameters;
    }

    @Override
    public RecyclerView.Adapter buildAdapter(Context context) {
        if (adapter == null) {
            adapter = new GenericRecyclerViewAdapter<>(context,
                    measurementItems,
                    R.layout.vhc_capture_measurement_container,
                    new CaptureMeasurementViewHolder.Factory(presenter, parameters));
        }

        return adapter;
    }

    public void updateItems(List<MeasurementItem> items) {
        measurementItems.clear();
        measurementItems.addAll(items);

        if (adapter != null)
            adapter.notifyDataSetChanged();
    }
}
