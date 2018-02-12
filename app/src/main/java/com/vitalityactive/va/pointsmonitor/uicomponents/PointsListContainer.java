package com.vitalityactive.va.pointsmonitor.uicomponents;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.pointsmonitor.PointsMonitorEntryDetailActivity;
import com.vitalityactive.va.pointsmonitor.PointsMonitorEntryDetailActivity.EntryDetail;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

public class PointsListContainer extends GenericTitledListContainerWithAdapter.TitledListWithAdapter {
    private GenericRecyclerViewAdapter<EntryDetail, PointsMonitorEntryDetailActivity.MetadataViewHolder> adapter;

    public PointsListContainer(Context context,
                               String title) {
        super(title);
        this.adapter = getContentAdapter(context);
        addDividers = true;
        setDividersLeftPaddingPx(ViewUtilities.pxFromDp(16));
    }

    @Override
    public RecyclerView.Adapter buildAdapter(Context context) {
        return adapter;
    }

    public void setMetadataList(List<EntryDetail> metadataItems) {
        adapter.replaceData(metadataItems);
    }

    public List<EntryDetail> getMetadataItems() {
        return adapter.getData();
    }

    public boolean isEmpty() {
        return getMetadataItems().isEmpty();
    }

    public static class MetadataHolderFactory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<EntryDetail, PointsMonitorEntryDetailActivity.MetadataViewHolder> {
        @Override
        public PointsMonitorEntryDetailActivity.MetadataViewHolder createViewHolder(View itemView) {
            return new PointsMonitorEntryDetailActivity.MetadataViewHolder(itemView);
        }
    }

    private static GenericRecyclerViewAdapter<EntryDetail, PointsMonitorEntryDetailActivity.MetadataViewHolder> getContentAdapter(Context context) {
        return new GenericRecyclerViewAdapter<>(context,
                new ArrayList<EntryDetail>(),
                R.layout.points_monitor_entry_metadata_item,
                new MetadataHolderFactory());
    }

}
