package com.vitalityactive.va.vitalitystatus;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.TitleSubtitleAndIcon;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.CMSImageLoader;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.vitalitystatus.detail.VitalityStatusDetailContainerViewHolder;
import com.vitalityactive.va.vitalitystatus.viewholder.StatusHeaderViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class VitalityStatusLevelIncreasedActivity
        extends BasePresentedActivity<VitalityStatusLevelIncreasedPresenter.UserInterface, VitalityStatusLevelIncreasedPresenter>
        implements VitalityStatusLevelIncreasedPresenter.UserInterface {

    @Inject
    VitalityStatusLevelIncreasedPresenter presenter;
    @Inject
    CMSImageLoader cmsImageLoader;
    private RecyclerView recyclerView;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_vitality_status_level_increased);

        setCloseAsActionBarIcon();

        recyclerView = findViewById(R.id.main_recyclerview);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected VitalityStatusLevelIncreasedPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected VitalityStatusLevelIncreasedPresenter getPresenter() {
        return presenter;
    }


    @Override
    public void displayStatusIncreased(List<TitleSubtitleAndIcon> rewardsDetails, VitalityStatusDTO vitalityStatusDTO) {
        setActionBarTitleAndDisplayHomeAsUp(getString(R.string.Status_increased_status_main_title_809));
        setRecyclerViewAdapter(createStatusIncreasedAdapters(vitalityStatusDTO, rewardsDetails));
    }

    @Override
    public void displayStatusInformation(List<TitleSubtitleAndIcon> rewardsDetails, VitalityStatusDTO vitalityStatusDTO) {
        setActionBarTitleAndDisplayHomeAsUp(getString(R.string.Status_information_main_title_814));
        setRecyclerViewAdapter(createStatusInformationAdapters(vitalityStatusDTO, rewardsDetails));
    }

    private void setRecyclerViewAdapter(List<GenericRecyclerViewAdapter> adapters) {
        ContainersRecyclerViewAdapter containersRecyclerViewAdapter = new ContainersRecyclerViewAdapter(adapters);

        recyclerView.setAdapter(containersRecyclerViewAdapter);
        ViewUtilities.scrollToTop(recyclerView);
    }

    private List<GenericRecyclerViewAdapter> createStatusInformationAdapters(VitalityStatusDTO vitalityStatusDTO, List<TitleSubtitleAndIcon> rewardsDetails) {
        List<GenericRecyclerViewAdapter> adapters = new ArrayList<>();

        adapters.add(getHeaderAdapter(getStatusInformationHeaderDTO(vitalityStatusDTO), vitalityStatusDTO.getCurrentStatusLevelKey()));
        adapters.add(getRewardsAdapter(vitalityStatusDTO, rewardsDetails));

        if (vitalityStatusDTO.getCurrentStatusLevelKey() == vitalityStatusDTO.getHighestStatusKey()) {
            return adapters;
        }

        adapters.add(getFooterStatusAdapter(getStatusInformationFooterDTO(vitalityStatusDTO), getString(R.string.Status_information_group_header_817)));

        return adapters;
    }

    @NonNull
    private TitleSubtitleAndIcon getStatusInformationFooterDTO(VitalityStatusDTO vitalityStatusDTO) {
        String footerSubTitle = String.format(getString(R.string.Status_information_starting_message_818), vitalityStatusDTO.getNextStatusName());

        return new TitleSubtitleAndIcon(vitalityStatusDTO.getCurrentStatusLevelName(),
                footerSubTitle,
                vitalityStatusDTO.getCurrentStatusLevel().getSmallIconResourceId());
    }

    @NonNull
    private TitleSubtitleAndIcon getStatusInformationHeaderDTO(VitalityStatusDTO vitalityStatusDTO) {
        String carryOverStatusName = vitalityStatusDTO.getCarryOverStatusLevel().getName();
        String headerTitle = String.format(getString(R.string.Status_information_title_815),
                carryOverStatusName);
        String headerSubTitle = String.format(getString(R.string.Status_information_message_816),
                carryOverStatusName,
                carryOverStatusName);

        return new TitleSubtitleAndIcon(headerTitle,
                headerSubTitle,
                vitalityStatusDTO.getCarryOverStatusLevel().getLargeIconResourceId());
    }

    @NonNull
    private List<GenericRecyclerViewAdapter> createStatusIncreasedAdapters(VitalityStatusDTO vitalityStatusDTO, List<TitleSubtitleAndIcon> rewardsDetails) {
        List<GenericRecyclerViewAdapter> adapters = new ArrayList<>();

        adapters.add(getHeaderAdapter(getStatusIncreasedHeaderDTO(vitalityStatusDTO), vitalityStatusDTO.getCurrentStatusLevelKey()));
        adapters.add(getRewardsAdapter(vitalityStatusDTO, rewardsDetails));

        if (vitalityStatusDTO.getCurrentStatusLevel().getKey() == vitalityStatusDTO.getHighestStatusKey()) {
            return adapters;
        }

        adapters.add(getFooterStatusAdapter(getStatusIncreasedFooterDTO(vitalityStatusDTO), getString(R.string.Status_increased_status_group_heading_812)));

        return adapters;
    }

    @NonNull
    private TitleSubtitleAndIcon getStatusIncreasedFooterDTO(VitalityStatusDTO vitalityStatusDTO) {
        String footerSubTitle;
        footerSubTitle = String.format(getString(R.string.Status_increased_status_points_needed_813),
                vitalityStatusDTO.getPointsToNextLevel());

        return new TitleSubtitleAndIcon(vitalityStatusDTO.getNextStatusName(),
                footerSubTitle,
                vitalityStatusDTO.getNextStatusLevel().getSmallIconResourceId());
    }

    @NonNull
    private TitleSubtitleAndIcon getStatusIncreasedHeaderDTO(VitalityStatusDTO vitalityStatusDTO) {
        String headerTitle;
        headerTitle = String.format(getString(R.string.Status_increased_status_title_810), vitalityStatusDTO.getCurrentStatusLevelName());

        return new TitleSubtitleAndIcon(headerTitle,
                getPointsStatus(vitalityStatusDTO),
                vitalityStatusDTO.getCurrentStatusLevel().getLargeIconResourceId());
    }

    @NonNull
    private String getPointsStatus(VitalityStatusDTO vitalityStatusDTO) {
        String pointsStatus;
        int key = vitalityStatusDTO.getCurrentStatusLevelKey();
        String currentLevelName = vitalityStatusDTO.getCurrentStatusLevelName();

        if (key == vitalityStatusDTO.getCarryOverStatusLevel().getKey()
                && key != vitalityStatusDTO.getLowestStatusKey()) {
            pointsStatus = String.format(getString(R.string.Status_increase_status_again_message_832), vitalityStatusDTO.getCarryOverStatusLevel().getName());
        } else if (key == vitalityStatusDTO.getHighestStatusKey()) {
            pointsStatus = String.format(getString(R.string.Status_increased_status_final_message_823), currentLevelName);
        } else {
            pointsStatus = String.format(getString(R.string.Status_increased_status_message_811), currentLevelName);
        }
        return pointsStatus;
    }

    @NonNull
    private GenericRecyclerViewAdapter getFooterStatusAdapter(TitleSubtitleAndIcon footerDTO, String containerTitle) {
        return new GenericRecyclerViewAdapter<>(this,
                footerDTO,
                R.layout.vitality_status_level_footer,
                new StatusFooterViewHolder.Factory(containerTitle));
    }

    @NonNull
    private GenericRecyclerViewAdapter<Object, StatusHeaderViewHolder> getHeaderAdapter(TitleSubtitleAndIcon statusHeaderDTO, int currentStatusLevelKey) {
        return new GenericRecyclerViewAdapter<>(this,
                statusHeaderDTO,
                R.layout.vitality_status_level_increased_header,
                new StatusHeaderViewHolder.Factory(currentStatusLevelKey));
    }

    @NonNull
    private GenericRecyclerViewAdapter getRewardsAdapter(VitalityStatusDTO vitalityStatusDTO, List<TitleSubtitleAndIcon> rewardsDetails) {
        String rewardsTitle = String.format(getString(R.string.Status_my_rewards_status_title_804), vitalityStatusDTO.getCurrentStatusLevel().getName());

        return new GenericRecyclerViewAdapter<>(this,
                rewardsDetails,
                R.layout.vitality_status_rewards_list_container,
                new VitalityStatusDetailContainerViewHolder.Factory(rewardsTitle, cmsImageLoader));
    }
}
