package com.vitalityactive.va.activerewards.history;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.CardMarginSettings;
import com.vitalityactive.va.activerewards.dto.GoalTrackerOutDto;
import com.vitalityactive.va.activerewards.history.uicontainers.PaginationFooterContainer;
import com.vitalityactive.va.activerewards.history.uicontainers.PagingFooterViewAdapter;
import com.vitalityactive.va.activerewards.history.uicontainers.RootActivityContainer;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.menu.MenuBuilder;
import com.vitalityactive.va.menu.MenuContainerViewHolder;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

public class GoalHistoryActivity
        extends BasePresentedActivity<GoalHistoryPresenter.UserInterface, GoalHistoryPresenter>
        implements GoalHistoryPresenter.UserInterface,
        MenuContainerViewHolder.OnMenuItemClickedListener,
        GenericRecyclerViewAdapter.OnItemClickListener<GoalTrackerOutDto>,
        View.OnClickListener {
    private final static String TAG = "AR_History";
    public static final int SCROLL_DIRECTION_DOWN = 1;

    private LinearLayout emptyView;
    private RecyclerView content;
    private RootActivityContainer rootActivityContainer;
    private PagingFooterViewAdapter footerAdapter;

    @Inject
    GoalHistoryPresenter presenter;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_active_rewards_monthly_activity);

        setUpActionBarWithTitle(R.string.AR_landing_activity_cell_title_711)
                .setDisplayHomeAsUpEnabled(true);

        emptyView = findViewById(R.id.ar_empty_container);
        content = findViewById(R.id.main_recyclerview);

        rootActivityContainer = new RootActivityContainer(this, this);
        footerAdapter = createPaginationFooterContainer();

        setUpRecyclerView(content);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected GoalHistoryPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected GoalHistoryPresenter getPresenter() {
        return presenter;
    }


    @Override
    public void showPagingLoadingIndicator() {
        PaginationFooterContainer footerContainer = (PaginationFooterContainer) footerAdapter.getContainer();
        if (footerContainer != null) {
            footerContainer.showLoading();
        }
    }

    @Override
    public void hidePagingLoadingIndicator() {
        if (footerAdapter == null) {
            return;
        }
        PaginationFooterContainer footerContainer = (PaginationFooterContainer) footerAdapter.getContainer();
        if (footerContainer != null) {
            footerContainer.hideFooter();
        }
    }

    @Override
    public void showConnectionErrorMessage(boolean fullscreen) {
        showFooterError();
    }

    @Override
    public void showGenericErrorMessage(boolean fullscreen) {
        showFooterError();
    }

    private void showFooterError() {
        PaginationFooterContainer footerContainer = (PaginationFooterContainer) footerAdapter.getContainer();
        if (footerContainer != null) {
            footerContainer.showError();
        }
    }

    @Override
    public void showNoMoreActivityView() {
        PaginationFooterContainer footerContainer = (PaginationFooterContainer) footerAdapter.getContainer();
        if (footerContainer != null) {
            footerContainer.showEndOfList();
        }
    }

    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
    }

    @Override
    public void updateRecyclerView() {
        emptyView.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);

        rootActivityContainer.setHistoryData(presenter.getActivityHistory());

        if (presenter.isLastPage()) {
            showNoMoreActivityView();
            return;
        }
        if (!content.canScrollVertically(SCROLL_DIRECTION_DOWN)) {
            showPagingLoadingIndicator();
            presenter.loadNextPage();
        }
    }

    private void setUpRecyclerView(final RecyclerView recyclerView) {
        ArrayList<GenericRecyclerViewAdapter> adapters = setupAdapters();
        ContainersRecyclerViewAdapter containersRecyclerViewAdapter = new ContainersRecyclerViewAdapter(adapters);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(containersRecyclerViewAdapter);
        recyclerView.addOnScrollListener(new PaginationScrollListener());
        ViewUtilities.scrollToTop(recyclerView);
    }

    private ArrayList<GenericRecyclerViewAdapter> setupAdapters() {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();
//        adapters.add(createMenuContainerAdapter()); // removed according to VA-19474
        adapters.add(createActivityHistoryContainer());
        adapters.add(footerAdapter);
        return adapters;
    }

    private GenericRecyclerViewAdapter createMenuContainerAdapter() {
        CardMarginSettings cardMarginSettings = new CardMarginSettings();
        cardMarginSettings.showBottomMarginOnly();
        return new MenuBuilder(this)
                .setClickListener(this)
                .setMenuItems(MenuBuilder.MenuItemSet.Help)
                .setCardMarginSettings(cardMarginSettings)
                .build();
    }

    private GenericRecyclerViewAdapter createActivityHistoryContainer() {
        return new GenericRecyclerViewAdapter<>(this,
                rootActivityContainer,
                R.layout.view_ar_root_activity_history_container,
                new GenericTitledListContainerWithAdapter.Factory<>());
    }

    private PagingFooterViewAdapter createPaginationFooterContainer() {
        return new PagingFooterViewAdapter<>(this,
                Collections.singletonList(""),
                R.layout.view_ar_pagination_footer_container,
                new PaginationFooterContainer.Factory(this));
    }

    public void onEmptyStateButtonClicked(View view) {
        navigationCoordinator.navigateOnHelpFromMonthlyActivity(this);
    }

    /**
     * MenuContainerViewHolder.OnMenuItemClickedListener
     */
    @Override
    public void onClicked(MenuItemType menuItemType) {
        if (MenuItemType.Help.equals(menuItemType)) {
            // TODO implement
        }
    }

    /**
     * GenericRecyclerViewAdapter.OnItemClickListener<GoalTrackerOutDto>
     *
     * @param position
     * @param item
     */
    @Override
    public void onClicked(int position, GoalTrackerOutDto item) {
        Log.d(TAG, "Clicked: position = " + position);
        navigationCoordinator.navigateToHistoryWeekDetailedActivity(this, item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.try_again_btn:
                presenter.loadNextPage();
                Log.d(TAG, "try again clicked");
                break;
            default:
                //NOP
                break;
        }
    }

    private class PaginationScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dx <= 0 && dy <= 0) {
                return;
            }

            LinearLayoutManager rootLayoutManager = (LinearLayoutManager) content.getLayoutManager();
            if (rootLayoutManager.findLastVisibleItemPosition() == rootLayoutManager.getItemCount() - 1) {
                loadMoreItems();
            }
        }

        private void loadMoreItems() {
            showPagingLoadingIndicator();
            presenter.loadNextPage();
        }
    }
}
