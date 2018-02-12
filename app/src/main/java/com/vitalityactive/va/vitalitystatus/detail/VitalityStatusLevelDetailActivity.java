package com.vitalityactive.va.vitalitystatus.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.TitleSubtitleAndIcon;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.register.view.SimpleTextViewHolder;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.vitalitystatus.VitalityStatusDTO;
import com.vitalityactive.va.utilities.CMSImageLoader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class VitalityStatusLevelDetailActivity
        extends BasePresentedActivity<VitalityStatusLevelDetailPresenter.UserInterface, VitalityStatusLevelDetailPresenter>
        implements VitalityStatusLevelDetailPresenter.UserInterface {

    public static final String PRODUCT_FEATURE_KEY = "PRODUCT_FEATURE_KEY";
    public static final String SUBFEATURE_KEY = "SUBFEATURE_KEY";
    public static final String NAME = "NAME";
    public static final String BUNDLE_KEY = "BUNDLE_KEY";

    @Inject
    VitalityStatusLevelDetailPresenter presenter;
    @Inject
    CMSImageLoader cmsImageLoader;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_vitality_status_level_detail);

        setupActionBar();
    }

    protected void setupActionBar() {
//        String title = getIntent().getExtras().getString(TITLE);

//        setActionBarTitleAndDisplayHomeAsUp(title);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected VitalityStatusLevelDetailPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected VitalityStatusLevelDetailPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void displayRewardsDetail(VitalityStatusDTO vitalityStatusDTO, List<TitleSubtitleAndIcon> rewardsDetails) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        ContainersRecyclerViewAdapter containersRecyclerViewAdapter = new ContainersRecyclerViewAdapter(getAdapters(vitalityStatusDTO, rewardsDetails));

        recyclerView.setAdapter(containersRecyclerViewAdapter);
    }

    @NonNull
    private List<GenericRecyclerViewAdapter> getAdapters(VitalityStatusDTO vitalityStatusDTO, List<TitleSubtitleAndIcon> rewardsDetails) {
        List<GenericRecyclerViewAdapter> adapters = new ArrayList<>();
        adapters.add(getHeaderAdapter(vitalityStatusDTO));
        adapters.add(getRewardItemsAdapter(rewardsDetails));
        return adapters;
    }

    private GenericRecyclerViewAdapter getRewardItemsAdapter(List<TitleSubtitleAndIcon> rewardsDetails) {
        return new GenericRecyclerViewAdapter<>(this,
                rewardsDetails,
                R.layout.vitality_status_rewards_list_container,
                new VitalityStatusDetailContainerViewHolder.Factory("", cmsImageLoader));
    }

    private GenericRecyclerViewAdapter getHeaderAdapter(VitalityStatusDTO vitalityStatusDTO) {
        String headerText = String.format(getString(R.string.Status_my_rewards_status_1_message_805),
                vitalityStatusDTO.getCurrentStatusLevelName());

        return new GenericRecyclerViewAdapter<>(this,
                headerText,
                R.layout.vitality_status_rewards_detail_header,
                new SimpleTextViewHolder.Factory());
    }
}
