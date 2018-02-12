package com.vitalityactive.va.wellnessdevices.pointsmonitor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.wellnessdevices.WdEventType;
import com.vitalityactive.va.wellnessdevices.dto.PointsDetailsDto;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.containers.HeaderContainer;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.containers.MeasurementDescriptionContainer;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.containers.PointsContainer;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.models.PointsEntryDetailsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class WellnessDevicesPointsActivity extends BasePresentedActivity<WellnessDevicesPointsPresenter.UserInterface, WellnessDevicesPointsPresenter>
        implements WellnessDevicesPointsPresenter.UserInterface,
        GenericRecyclerViewAdapter.OnItemClickListener<PointsEntryDetailsModel> {
    private final static int VIEW_RANGE_CONTAINER = R.layout.view_wd_points_range_container;
    private final static int VIEW_RANGE_CONTAINER_FLAT = R.layout.view_wd_points_range_container_flat;

    public static final String KEY_BUNDLE = "KeyBundle";
    public static final String KEY_EVENT_TYPE = "EventType";

    private WdEventType eventType;
    private List<PointsEntryDetailsModel> rangeList;
    private ContainersRecyclerViewAdapter containersRecyclerViewAdapter;
    private PointsContainer pointsContainer;

    @Inject
    WellnessDevicesPointsPresenter presenter;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_wd_points);

        Bundle bundle = getIntent().getBundleExtra(KEY_BUNDLE);
        eventType = WdEventType.getEventTypeByKey(bundle.getInt(KEY_EVENT_TYPE));
        List<PointsDetailsDto> originalRangeList = presenter.getPotentialPoints(bundle.getInt(KEY_EVENT_TYPE)).getPointsEntryDetails();//bundle.getParcelableArrayList(KEY_RANGE_LIST);
        rangeList = new ArrayList<>();
        for (int i = 0; i < originalRangeList.size(); i++) {
            rangeList.add(new PointsEntryDetailsModel(originalRangeList.get(i),
                    i == originalRangeList.size() - 1));
        }

        setUpActionBarWithTitle(eventType.getName()).setDisplayHomeAsUpEnabled(true);

        pointsContainer = new PointsContainer(this, this, presenter.getUomStringsProvider());
        pointsContainer.setPointsRanges(rangeList);

        setUpRecyclerView((RecyclerView) findViewById(R.id.main_recyclerview));
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected WellnessDevicesPointsPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected WellnessDevicesPointsPresenter getPresenter() {
        return presenter;
    }

    private void setUpRecyclerView(final RecyclerView recyclerView) {
        setBackgroundColor(!hasDescription());

        ArrayList<GenericRecyclerViewAdapter> adapters = setupAdapters();
        containersRecyclerViewAdapter = new ContainersRecyclerViewAdapter(adapters);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(containersRecyclerViewAdapter);
        ViewUtilities.scrollToTop(recyclerView);
    }

    private ArrayList<GenericRecyclerViewAdapter> setupAdapters() {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();
        adapters.add(createHeaderContainer());
        adapters.add(createPointsLevelsContainer());
        if (hasDescription()) {
            adapters.add(createHeartRateDescriptionContainer());
        }
        return adapters;
    }

    private GenericRecyclerViewAdapter createHeaderContainer() {
        return new GenericRecyclerViewAdapter<>(this,
                Collections.singletonList(eventType),
                R.layout.view_wd_points_header,
                new HeaderContainer.Factory());
    }

    private GenericRecyclerViewAdapter createPointsLevelsContainer() {
        return new GenericRecyclerViewAdapter<>(this,
                pointsContainer,
                hasDescription() ? VIEW_RANGE_CONTAINER : VIEW_RANGE_CONTAINER_FLAT,
                new GenericTitledListContainerWithAdapter.Factory<>());
    }

    private GenericRecyclerViewAdapter createHeartRateDescriptionContainer() {
        return new GenericRecyclerViewAdapter<>(this,
                Collections.singletonList(eventType),
                R.layout.view_wd_points_measurement_description,
                new MeasurementDescriptionContainer.Factory());
    }

    @Override
    public void onClicked(int position, PointsEntryDetailsModel item) {
        // click on item on range list
    }

    private boolean hasDescription() {
        return eventType.getDescription() != 0;
    }

    private void setBackgroundColor(boolean isFlatView) {
        findViewById(R.id.main_recyclerview).setBackgroundColor(ContextCompat.getColor(this,
                isFlatView ? android.R.color.white : R.color.active_rewards_divider_background));
    }
}
