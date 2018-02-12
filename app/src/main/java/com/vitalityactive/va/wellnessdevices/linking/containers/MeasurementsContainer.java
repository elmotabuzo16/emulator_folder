package com.vitalityactive.va.wellnessdevices.linking.containers;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.wellnessdevices.WdEventType;

import java.util.ArrayList;
import java.util.List;

public class MeasurementsContainer extends GenericTitledListContainerWithAdapter.TitledListWithAdapter {
    private GenericRecyclerViewAdapter<MeasurementItem, MeasurementItemViewHolder> adapter;

    public MeasurementsContainer(Context context,
                                 String title,
                                 GenericRecyclerViewAdapter.OnItemClickListener<MeasurementItem> clickListener) {
        super(title);
        this.adapter = getContentAdapter(context, clickListener);
    }

    @Override
    public RecyclerView.Adapter buildAdapter(Context context) {
        return adapter;
    }

    public void setMeasurements(List<MeasurementItem> measurementItems) {
        adapter.replaceData(measurementItems);
    }

    private List<MeasurementItem> getMeasurementItems() {
        return adapter.getData();
    }

    public boolean isEmpty(){
        return getMeasurementItems().isEmpty();
    }

    private static GenericRecyclerViewAdapter<MeasurementItem, MeasurementItemViewHolder> getContentAdapter(Context context,
                                            GenericRecyclerViewAdapter.OnItemClickListener<MeasurementItem> clickedListener) {
        return new GenericRecyclerViewAdapter<>(context,
                new ArrayList<MeasurementItem>(),
                R.layout.view_wd_linking_measurement_list_item,
                new MeasurementItemViewHolder.Factory(),
                clickedListener);
    }

    public static class MeasurementItem {
        @DrawableRes int iconId;
        @StringRes  int title;
        WdEventType eventType;

        public MeasurementItem(@NonNull WdEventType eventType){
            this.iconId = eventType.getDrawableIcon();
            this.title = eventType.getName();
            this.eventType = eventType;
        }

        public WdEventType getEventType() {
            return eventType;
        }

        public int getTitleResourceId() {
            return title;
        }
    }
}
