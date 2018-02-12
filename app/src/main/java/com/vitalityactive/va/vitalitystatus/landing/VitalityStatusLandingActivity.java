package com.vitalityactive.va.vitalitystatus.landing;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.CardMarginSettings;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.home.events.GetProductFeaturePointsResponseEvent;
import com.vitalityactive.va.menu.MenuBuilder;
import com.vitalityactive.va.menu.MenuContainerViewHolder;
import com.vitalityactive.va.menu.MenuItem;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.vitalitystatus.PointsCategoryTitledList;
import com.vitalityactive.va.vitalitystatus.ProductPointsContent;
import com.vitalityactive.va.vitalitystatus.VitalityStatusDTO;
import com.vitalityactive.va.vitalitystatus.earningpoints.PointsInformationDTO;
import com.vitalityactive.va.vitalitystatus.viewholder.StatusContentViewHolder;
import com.vitalityactive.va.vitalitystatus.viewholder.StatusHeaderWithProgressAndButtonViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class VitalityStatusLandingActivity
        extends BasePresentedActivity
        implements VitalityStatusLandingPresenter.UserInterface,
        MenuContainerViewHolder.OnMenuItemClickedListener, RecyclerView.OnChildAttachStateChangeListener {

    @Inject
    VitalityStatusLandingPresenter presenter;
    @Inject
    MainThreadScheduler scheduler;
    @Inject
    ProductPointsContent content;

    private RecyclerView recyclerView;
    private ContainersRecyclerViewAdapter container;

    @Override
    public void displayContent(VitalityStatusDTO currentLevel, boolean shouldShowMyRewards) {
        container = new ContainersRecyclerViewAdapter(createAdapters(currentLevel, shouldShowMyRewards));

        recyclerView.setAdapter(container);

        ViewUtilities.scrollToTop(recyclerView);
    }

    @Override
    public void displayProductFeaturePoints(final List<PointsInformationDTO> pointsInformationDTOs) {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                container.addOrReplaceAdapter(getPointsCategoriesAdapter(pointsInformationDTOs), 2);
                recyclerView.findViewById(R.id.recycler_view).setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void showError(GetProductFeaturePointsResponseEvent event) {
        hideEmptyState();

        String snackBarMessage;
        if (event.getRequestResult() == RequestResult.CONNECTION_ERROR) {
            snackBarMessage = getString(R.string.connectivity_error_alert_title_44);
        } else {
            snackBarMessage = getString(R.string.error_unable_to_load_title_503);
        }

        Snackbar.make(findViewById(R.id.vitality_status_landing),
                snackBarMessage,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.AR_connection_error_button_retry_789, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showEmptyState();
                        presenter.retryLoading();
                    }
                }).show();
    }

    @Override
    public void onClicked(MenuItemType menuItemType) {
        navigationCoordinator.navigateOnMenuItemFromVitalityStatus(this, menuItemType);
    }

    public void handleMyRewardsButtonTapped(View view) {
        navigationCoordinator.navigateToStatusMyRewardsDetail(this);
    }

    public void handleVitalityStatusInfoIconTapped(View view) {
        View parent = (View) view.getParent();
        TextView titleTextView = parent.findViewById(R.id.title);
        navigationCoordinator.navigateToVitalityStatusDetail(this, titleTextView.getText().toString());
    }

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_vitality_status_landing);

        setActionBarTitleAndDisplayHomeAsUp(getString(R.string.Status_landing_main_title_status_796));

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.addOnChildAttachStateChangeListener(this);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected VitalityStatusLandingPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @NonNull
    private List<GenericRecyclerViewAdapter> createAdapters(VitalityStatusDTO currentLevel,
                                                            boolean shouldShowMyRewards) {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();

        adapters.add(getStatusHeaderAdapter(currentLevel, shouldShowMyRewards));
        adapters.add(getStatusContentAdapter(currentLevel, shouldShowMyRewards));
        ArrayList<PointsInformationDTO> pointsInformationDTOs = new ArrayList<>();
        adapters.add(getPointsCategoriesAdapter(pointsInformationDTOs));
        adapters.add(getMenuAdapter());

        return adapters;
    }

    private GenericRecyclerViewAdapter getPointsCategoriesAdapter(List<PointsInformationDTO> pointsInformationDTOs) {
        PointsCategoryTitledList pointsCategoryTitledList =
                new PointsCategoryTitledList(getString(R.string.Status_landing_earning_points_group_header_803), pointsInformationDTOs, navigationCoordinator, content);

        return new GenericRecyclerViewAdapter<>(this,
                pointsCategoryTitledList,
                R.layout.titled_list_container,
                new GenericTitledListContainerWithAdapter.Factory<>());
    }

    private GenericRecyclerViewAdapter getStatusContentAdapter(final VitalityStatusDTO vitalityStatusDTO, boolean shouldShowMyRewards) {
        return new GenericRecyclerViewAdapter<>(this,
                vitalityStatusDTO,
                R.layout.vitality_status_landing_points_content,
                new StatusContentViewHolder.Factory(shouldShowMyRewards));
    }

    private GenericRecyclerViewAdapter getStatusHeaderAdapter(VitalityStatusDTO currentLevel, boolean shouldShowMyRewards) {
        String pointsStatus = content.getPointsStatusMessage(currentLevel.getPointsToMaintainStatus(),
                currentLevel.getCurrentStatusLevelName(),
                currentLevel.getPointsToNextLevel(),
                currentLevel.getNextStatusName());

        StatusItem statusItem = new StatusItem(currentLevel.getCurrentStatusLevelName(),
                pointsStatus,
                currentLevel.getCurrentStatusLevel().getLargeIconResourceId(),
                getProgressWithMinimumOfOne(currentLevel));

        return new GenericRecyclerViewAdapter<>(this,
                statusItem,
                R.layout.vitality_status_landing_header,
                new StatusHeaderWithProgressAndButtonViewHolder.Factory(shouldShowMyRewards));
    }

    private int getProgressWithMinimumOfOne(VitalityStatusDTO vitalityStatusDTO) {
        return Math.max(1, Math.min(100, vitalityStatusDTO.getProgress()));
    }

    private GenericRecyclerViewAdapter<CardMarginSettings, MenuContainerViewHolder> getMenuAdapter() {
        return new MenuBuilder(this)
                .addMenuItem(MenuItem.Builder.learnMore())
                .setClickListener(this)
                .build();
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
        if (view.getId() != R.id.list_container) {
            return;
        }

        if (!presenter.hasFeatureCategories() && presenter.requestInProgress()) {
            showEmptyState();
        } else {
            hideEmptyState();
        }

        if (presenter.hasFeatureCategories()) {
            showEmptyState();
        }
    }

    private void showEmptyState() {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                View listContainer = findViewById(R.id.list_container);
                if (listContainer == null) {
                    return;
                }

                View pointsRecyclerView = listContainer.findViewById(R.id.recycler_view);
                if (pointsRecyclerView == null) {
                    return;
                }

                pointsRecyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void hideEmptyState() {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                View listContainer = findViewById(R.id.list_container);
                if (listContainer == null) {
                    return;
                }

                View pointsRecyclerView = listContainer.findViewById(R.id.recycler_view);
                if (pointsRecyclerView == null) {
                    return;
                }

                pointsRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {

    }
}

