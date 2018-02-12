package com.vitalityactive.va.vitalitystatus.earningpoints;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.TitleAndSubtitle;
import com.vitalityactive.va.activerewards.viewholder.TitleAndSubtitleViewHolder;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.vitalitystatus.ProductPointsContent;
import com.vitalityactive.va.vitalitystatus.detail.VitalityStatusLevelDetailActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class VitalityStatusDetailsActivity
        extends BasePresentedActivity<VitalityStatusDetailsPresenter.UserInterface, VitalityStatusDetailsPresenter>
        implements VitalityStatusDetailsPresenter.UserInterface {

    @Inject
    VitalityStatusDetailsPresenter presenter;
    @Inject
    ProductPointsContent content;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_vitality_status_earning_points);

        int key = getIntent().getIntExtra(VitalityStatusLevelDetailActivity.PRODUCT_FEATURE_KEY, 0);

        if (key == 0) {
            key = getIntent().getIntExtra(VitalityStatusLevelDetailActivity.SUBFEATURE_KEY, 0);
            presenter.setIsSubfeature(true);
        } else {
            presenter.setIsSubfeature(false);
        }
        
        presenter.setKey(key);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected VitalityStatusDetailsPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected VitalityStatusDetailsPresenter getPresenter() {
        return presenter;
    }

    private GenericRecyclerViewAdapter<TitleAndSubtitle, TitleAndSubtitleViewHolder<TitleAndSubtitle>> createHeaderAdapter(TitleAndSubtitle titleAndSubtitle) {
        return new GenericRecyclerViewAdapter<>(this,
                titleAndSubtitle,
                R.layout.vitality_status_earning_method_header,
                new TitleAndSubtitleViewHolder.Factory());
    }

    @Override
    public void displayPointsInformation(List<PointsInformationDTO> pointsInformationDTOs, TitleAndSubtitle titleAndSubtitle) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        List<GenericRecyclerViewAdapter> adapters = new ArrayList<>();

        if (!TextUtilities.isNullOrWhitespace(titleAndSubtitle.getTitle())) {
            adapters.add(createHeaderAdapter(titleAndSubtitle));
        }
        adapters.add(createContentAdapters(pointsInformationDTOs));

        ContainersRecyclerViewAdapter container = new ContainersRecyclerViewAdapter(adapters);

        recyclerView.setAdapter(container);
    }

    @Override
    public void setActionBarTitle(String title) {
        setActionBarTitleAndDisplayHomeAsUp(title);
    }

    private GenericRecyclerViewAdapter createContentAdapters(final List<PointsInformationDTO> pointsInformationDTOs) {
        return new GenericRecyclerViewAdapter<>(this,
                pointsInformationDTOs,
                R.layout.vitality_status_earning_method_container,
                new ProductFeatureViewHolder.Factory(navigationCoordinator, content));
    }
}

