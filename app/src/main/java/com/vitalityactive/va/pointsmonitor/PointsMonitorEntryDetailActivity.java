package com.vitalityactive.va.pointsmonitor;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.eventdetail.MetadataFormatter;
import com.vitalityactive.va.dto.MetadataDTO;
import com.vitalityactive.va.dto.PointsEntryDTO;
import com.vitalityactive.va.menu.MenuContainerViewHolder;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.pointsmonitor.uicomponents.PointsListContainer;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.CompositeRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.MeasurementContentFromResourceString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class PointsMonitorEntryDetailActivity extends BaseActivity
        implements MenuContainerViewHolder.OnMenuItemClickedListener,
        GenericRecyclerViewAdapter.IViewHolderFactory<String, PointsMonitorEntryDetailActivity.DetailViewHolder> {

    public static final String POINTS_ENTRY_ID = "POINTS_ENTRY_ID";
    public static final String GLOBAL_TINT_COLOR = "GLOBAL_TINT_COLOR";

    @Inject
    PointsEntryRepository pointsEntryRepository;
    @Inject
    DateFormattingUtilities dateFormattingUtilities;
    private int globalTintColor;
    @Inject
    MeasurementContentFromResourceString uomProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_points_monitor_entry_detail);

        getDependencyInjector().inject(this);

        setUpActionBarWithTitle(R.string.PM_activity_detail_section_title_559)
                .setDisplayHomeAsUpEnabled(true);

        globalTintColor = getIntent().getIntExtra(GLOBAL_TINT_COLOR, 0);
        setActionBarColor(globalTintColor);
        setStatusBarColor(globalTintColor);

        setUpRecyclerView((RecyclerView) findViewById(R.id.recycler_view));
    }

    private void setUpRecyclerView(final RecyclerView recyclerView) {
        final CompositeRecyclerViewAdapter adapter = new CompositeRecyclerViewAdapter(setUpAdapters(), new int[]{
                R.layout.points_monitor_entry_detail_item,
                // TODO: hid help on 27/10/2017
//                R.layout.menu_container,
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        ViewUtilities.scrollToTop(recyclerView);
    }

    @NonNull
    private HashMap<Integer, GenericRecyclerViewAdapter> setUpAdapters() {
        @SuppressLint("UseSparseArrays") HashMap<Integer, GenericRecyclerViewAdapter> adapters = new HashMap<>();
        adapters.put(R.layout.points_monitor_entry_detail_item, createDetailsAdapter());
        // TODO: hid help on 27/10/2017
//        adapters.put(R.layout.menu_container, createMenuContainerAdapter());
        return adapters;
    }

    private GenericRecyclerViewAdapter createDetailsAdapter() {
        return new GenericRecyclerViewAdapter<>(this,
                Collections.singletonList("One"),
                R.layout.points_monitor_entry_detail_item,
                this);
    }

    // TODO: hid help on 27/10/2017
//    private GenericRecyclerViewAdapter createMenuContainerAdapter() {
//        return new MenuBuilder(this)
//                .addMenuItem(MenuItem.Builder.help(globalTintColor))
//                .setClickListener(this)
//                .build();
//    }

    @Override
    public void onClicked(MenuItemType menuItemType) {

    }

    @Override
    public DetailViewHolder createViewHolder(View itemView) {
        return new DetailViewHolder(itemView);
    }

    class DetailViewHolder extends GenericRecyclerViewAdapter.ViewHolder<String>
            implements GenericRecyclerViewAdapter.IViewHolderFactory<EntryDetail, MetadataViewHolder> {
        private final RecyclerView recyclerView;
        private final PointsItemContentViewHolder contentViewHolder;
        private final View deviceLayout;
        private final TextView device;

        DetailViewHolder(View itemView) {
            super(itemView);
            contentViewHolder = new PointsItemContentViewHolder(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            deviceLayout = itemView.findViewById(R.id.device_layout);
            device = itemView.findViewById(R.id.device_used);
        }

        @Override
        public void bindWith(String dataItem) {
            PointsEntryDTO pointsEntry = pointsEntryRepository.getPointsEntry(getIntent().getStringExtra(POINTS_ENTRY_ID));
            GenericRecyclerViewAdapter adapter = new GenericRecyclerViewAdapter<>(PointsMonitorEntryDetailActivity.this,
                    getDetails(pointsEntry),
                    R.layout.view_ar_activity_list,
                    new GenericTitledListContainerWithAdapter.Factory<>());
            recyclerView.setAdapter(adapter);
            contentViewHolder.bindWith(pointsEntry);
            if (TextUtilities.isNullOrWhitespace(pointsEntry.getDevice())) {
                deviceLayout.setVisibility(View.GONE);
            } else {
                device.setText(pointsEntry.getDevice());
            }
        }

        @Override
        public MetadataViewHolder createViewHolder(View itemView) {
            return new MetadataViewHolder(itemView);
        }
    }

    public static class MetadataViewHolder extends GenericRecyclerViewAdapter.ViewHolder<EntryDetail> {
        private final TextView title;
        private final TextView subtitle;

        public MetadataViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
        }

        @Override
        public void bindWith(EntryDetail dataItem) {
            title.setText(dataItem.title);
            subtitle.setText(dataItem.subtitle);
        }
    }

    public PointsListContainer getDetails(PointsEntryDTO pointsEntry) {
        List<EntryDetail> details = new ArrayList<>();
        details.add(new EntryDetail(pointsEntry.getDescription(), getString(R.string.PM_activity_detail_section_subheading_527)));
        for (MetadataDTO metadata : pointsEntry.getMetadata()) {
            details.add(new EntryDetail(getFormattedValueWithUnitOfMeasure(metadata), metadata.getTypeName()));
        }
        details.add(new EntryDetail(dateFormattingUtilities.formatDateMonthYearHoursMinutes(pointsEntry.getEffectiveDate()),
                getString(R.string.date_title_264)));

        PointsListContainer pointsListContainer = new PointsListContainer(this,
                getString(R.string.PM_activity_detail_section_heading_3_526));
        pointsListContainer.setMetadataList(details);
        return pointsListContainer;
    }

    private String getFormattedValueWithUnitOfMeasure(MetadataDTO metadata) {
        return MetadataFormatter.getFormattedValueWithUnitOfMeasure(uomProvider,
                metadata.getUnitOfMeasure(),
                metadata.getValue(),
                metadata.getTypeKey());
    }

    public static class EntryDetail {
        final String title;
        final String subtitle;

        EntryDetail(String title, String subtitle) {
            this.title = title;
            this.subtitle = subtitle;
        }
    }
}
