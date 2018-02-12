package com.vitalityactive.va.vitalitystatus.earningpoints;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.wellnessdevices.WdEventType;
import com.vitalityactive.va.wellnessdevices.dto.PointsConditionsDto;
import com.vitalityactive.va.wellnessdevices.dto.PointsDetailsDto;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.MeasurementContentFromResourceString;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.containers.HeaderContainer;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.containers.MeasurementDescriptionContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.vitalityactive.va.vitalitystatus.detail.VitalityStatusLevelDetailActivity.BUNDLE_KEY;
import static com.vitalityactive.va.vitalitystatus.detail.VitalityStatusLevelDetailActivity.NAME;
import static com.vitalityactive.va.vitalitystatus.detail.VitalityStatusLevelDetailActivity.PRODUCT_FEATURE_KEY;

public class StatusPointsActivity
        extends BasePresentedActivity<StatusPointsPresenter.UserInterface, StatusPointsPresenter>
        implements StatusPointsPresenter.UserInterface {

    @Inject
    StatusPointsPresenter presenter;
    @Inject
    MeasurementContentFromResourceString uomProvider;
    private RecyclerView recyclerView;

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_status_points);

        Bundle bundle = getIntent().getBundleExtra(BUNDLE_KEY);
        String name = bundle.getString(NAME, "");

        presenter.setProductFeatureKey(bundle.getInt(PRODUCT_FEATURE_KEY, 0));

        setActionBarTitleAndDisplayHomeAsUp(name);

        recyclerView = findViewById(R.id.main_recyclerview);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return (super.onOptionsItemSelected(item));
    }

    private ArrayList<GenericRecyclerViewAdapter> setupAdapters(List<StatusPointsItem> statusPointsItems) {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();

        WdEventType wdEventType = presenter.getWdEventType();
        adapters.add(createHeaderContainer(wdEventType));
        adapters.addAll(createPointsLevelsContainer(statusPointsItems));

        if (hasDescription(wdEventType)) {
            adapters.add(createHeartRateDescriptionContainer(wdEventType));
        }

        return adapters;
    }

    private boolean hasDescription(WdEventType wdEventType) {
        return wdEventType.getDescription() != 0;
    }

    private GenericRecyclerViewAdapter createHeartRateDescriptionContainer(WdEventType wdEventType) {
        return new GenericRecyclerViewAdapter<>(this,
                Collections.singletonList(wdEventType),
                R.layout.view_wd_points_measurement_description,
                new MeasurementDescriptionContainer.Factory());
    }

    private List<GenericRecyclerViewAdapter> createPointsLevelsContainer(List<StatusPointsItem> pointsInformationDTOs) {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();

        for (int i = 0; i < pointsInformationDTOs.size(); i++) {
            List<PointsDetailsDto> pointsDetailsDtos = pointsInformationDTOs.get(i).getPointsDetailsDtos();
            if (pointsDetailsDtos != null && pointsDetailsDtos.size() > 0) {
                for (PointsDetailsDto pointsDetailsDto : pointsDetailsDtos) {

                    List<PointsConditionsDto> conditions = pointsDetailsDto.getConditions();

                    if (conditions.size() > 0) {
                        adapters.add(new GenericRecyclerViewAdapter<>(this,
                                new StatusPointsConditionItem(pointsDetailsDto.getPotentialPoints(), conditions),
                                R.layout.status_points_list_item,
                                new StatusPointsConditionItemViewHolder.Factory(uomProvider)));
                    }
                }
            } else {
                adapters.add(new GenericRecyclerViewAdapter<>(this,
                        pointsInformationDTOs.get(i),
                        R.layout.status_points_list_item,
                        new StatusPointsItemViewHolder.Factory()));
            }
        }

        return adapters;
    }

    private GenericRecyclerViewAdapter createHeaderContainer(WdEventType eventType) {

        return new GenericRecyclerViewAdapter<>(this,
                eventType,
                R.layout.status_points_header,
                new HeaderContainer.Factory());
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected StatusPointsPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected StatusPointsPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void displayPointsContent(List<StatusPointsItem> statusPointsItems) {
        ArrayList<GenericRecyclerViewAdapter> adapters = setupAdapters(statusPointsItems);
        ContainersRecyclerViewAdapter containersRecyclerViewAdapter = new ContainersRecyclerViewAdapter(adapters);

        recyclerView.setAdapter(containersRecyclerViewAdapter);
        ViewUtilities.scrollToTop(recyclerView);
    }
}
