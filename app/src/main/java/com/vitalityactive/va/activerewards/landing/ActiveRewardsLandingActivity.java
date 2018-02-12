package com.vitalityactive.va.activerewards.landing;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.ActivityItem;
import com.vitalityactive.va.activerewards.dto.WeeklyTargetItem;
import com.vitalityactive.va.activerewards.landing.uicontainers.NotStartedContainer;
import com.vitalityactive.va.activerewards.viewholder.ActiveRewardsWeeklyTargetViewHolder;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.menu.MenuBuilder;
import com.vitalityactive.va.menu.MenuContainerViewHolder;
import com.vitalityactive.va.menu.MenuItem;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.uicomponents.adapters.CompositeRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class ActiveRewardsLandingActivity extends BasePresentedActivity<ActiveRewardsLandingPresenter.UserInterface, ActiveRewardsLandingPresenter>
        implements ActiveRewardsLandingPresenter.UserInterface,
        MenuContainerViewHolder.OnMenuItemClickedListener,
        GenericRecyclerViewAdapter.OnItemClickListener<ActivityItem>,
        EventListener<AlertDialogFragment.DismissedEvent> {
    private static final String TAG = "ActiveRewardsLanding";
    public static final String EXTRA_SHOULD_CHECK_LINKED_DEVICES_ON_START = "EXTRA_CHECK_LINK_DEVICE_ON_START";
    private static final String AR_LINK_DEVICE_EVENT = "AR_LINK_DEVICE_EVENT";

    View emptyView;
    RecyclerView contentView;

    private SwipeRefreshLayout swipeRefreshLayout;

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            presenter.onSwipeToRefresh();
        }
    };

    @Inject
    ActiveRewardsLandingPresenter presenter;
    @Inject
    EventDispatcher eventDispatcher;
    @Inject
    InsurerConfigurationRepository insurerConfigurationRepository;
    @Inject
    DateFormattingUtilities dateFormattingUtilities;

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        navigationCoordinator.getActiveRewardsDependencyInjector().inject(this);
    }

    @Override
    protected ActiveRewardsLandingPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected ActiveRewardsLandingPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_active_rewards_landing);
        setUpActionBarWithTitle(R.string.AR_Landing_home_view_title_710)
                .setDisplayHomeAsUpEnabled(true);

        getPresenter().setShouldCheckIfUserHasLinkedDevices(getIntent().getBooleanExtra(EXTRA_SHOULD_CHECK_LINKED_DEVICES_ON_START, false));

        emptyView = findViewById(R.id.empty_state);
        contentView = findViewById(R.id.main_recyclerview);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeColors(getThemeAccentColor());
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    @Override
    protected void resume() {
        eventDispatcher.addEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    @Override
    protected void pause() {
        eventDispatcher.removeEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigationCoordinator.resetActiveRewardsDependencies();
    }

    private void setUpRecyclerViewWithActivity(final RecyclerView recyclerView,
                                               String range, int pointsTarget,
                                               String pointsAchieved, int initialPoints) {
        HashMap<Integer, GenericRecyclerViewAdapter> adapters = setUpAdaptersWithActivity(
                range, pointsTarget,
                pointsAchieved, initialPoints);
        final CompositeRecyclerViewAdapter adapter = new CompositeRecyclerViewAdapter(adapters, new int[]{
                R.layout.active_rewards_item_weekly_target,
                R.layout.menu_container,
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        ViewUtilities.scrollToTop(recyclerView);
    }

    private void setUpRecyclerViewNotStarted(final RecyclerView recyclerView,
                                             String date) {
        HashMap<Integer, GenericRecyclerViewAdapter> adapters = setUpAdaptersNotStarted(date);
        final CompositeRecyclerViewAdapter adapter = new CompositeRecyclerViewAdapter(adapters, new int[]{
                R.layout.view_ar_activity_not_started,
                R.layout.menu_container,
        });
        recyclerView.setAdapter(adapter);
        ViewUtilities.scrollToTop(recyclerView);
    }

    @NonNull
    private HashMap<Integer, GenericRecyclerViewAdapter> setUpAdaptersWithActivity(String range, int pointsTarget,
                                                                                   String pointsAchieved, int initialPoints) {
        HashMap<Integer, GenericRecyclerViewAdapter> adapters = new HashMap<>();
        adapters.put(R.layout.active_rewards_item_weekly_target, createWeeklyTargetItemAdapter(range, pointsTarget, pointsAchieved, initialPoints));
        adapters.put(R.layout.menu_container, createMenuContainerAdapter());
        return adapters;
    }

    @NonNull
    private HashMap<Integer, GenericRecyclerViewAdapter> setUpAdaptersNotStarted(String date) {
        HashMap<Integer, GenericRecyclerViewAdapter> adapters = new HashMap<>();
        adapters.put(R.layout.view_ar_activity_not_started, createAdapterNotStarted(date));
        adapters.put(R.layout.menu_container, createMenuContainerAdapter());
        return adapters;
    }

    private GenericRecyclerViewAdapter createMenuContainerAdapter() {
        MenuBuilder menuBuilder = new MenuBuilder(this)
                .addMenuItem(MenuItem.Builder.activity())
                .setClickListener(this);

        if (insurerConfigurationRepository.shouldShowRewardsMenuItem()) {
            menuBuilder.addMenuItem(MenuItem.Builder.rewards());
        }

        menuBuilder
                .addMenuItem(MenuItem.Builder.learnMore())
                .addMenuItem(MenuItem.Builder.help());
        // TODO: Hid Help on 24/10/2017

        return menuBuilder.build();
    }

    private GenericRecyclerViewAdapter createAdapterNotStarted(String date) {
        return new GenericRecyclerViewAdapter<>(this,
                Collections.singletonList(date),
                R.layout.view_ar_activity_not_started,
                new NotStartedContainer.Factory());
    }

    @NonNull
    private GenericRecyclerViewAdapter createWeeklyTargetItemAdapter(String range, int pointsTarget,
                                                                     String pointsAchieved, int initialPoints) {
        WeeklyTargetItem item = new WeeklyTargetItem();
        item.setRange(range);
        item.setPointsTarget(pointsTarget);
        item.setPointsAchieved(pointsAchieved);
        item.setInitialPoints(initialPoints);
        List<WeeklyTargetItem> items = Collections.singletonList(item);

        return new GenericRecyclerViewAdapter<>(this,
                items,
                R.layout.active_rewards_item_weekly_target,
                new WeeklyTargetViewHolderFactory());
    }

    private void hideSwipeLoadingIndicator() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void hideLoadingIndicator() {
        hideSwipeLoadingIndicator();
    }

    @Override
    public void onClicked(MenuItemType menuItemType) {
        Log.d(TAG, "clicked menu item: " + menuItemType);
        presenter.cancelGoalProgressAndDetailsRequest();
        navigationCoordinator.navigateOnMenuItemFromActiveRewardsLanding(this, menuItemType);
    }

    public void onClicked(int position, ActivityItem item) {
        Log.d(TAG, "clicked activity item @" + position);
        navigationCoordinator.navigateOnThisWeeksActivityItemFromActiveRewardsLanding(this, item);
    }

    @Override
    public void showConnectionErrorMessage() {
        showSnackBar(R.string.connectivity_error_alert_title_44);
    }

    @Override
    public void showGenericErrorMessage() {
        showSnackBar(R.string.error_unable_to_load_title_503);
    }

    private void showSnackBar(int stringId) {
        Snackbar.make(findViewById(R.id.activity_active_rewards_landing), stringId, Snackbar.LENGTH_LONG)
                .setAction(R.string.AR_connection_error_button_retry_789, getSnackBarRetryListener()).show();
    }

    @NonNull
    private View.OnClickListener getSnackBarRetryListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.fetchGoalProgressAndDetails();
            }
        };
    }

    @Override
    public void showNotStartedView(String startDate) {
        emptyView.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
        setUpRecyclerViewNotStarted((RecyclerView) findViewById(R.id.main_recyclerview), startDate);
    }

    @Override
    public void showLinkDeviceDialog() {
        AlertDialogFragment.create(AR_LINK_DEVICE_EVENT,
                getString(R.string.AR_landing_link_device_dialog_title_766),
                getString(R.string.AR_landing_link_device_dialog_message_767),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.link_now_button_text_434))
                .show(getSupportFragmentManager(), AR_LINK_DEVICE_EVENT);
    }

    @Override
    public void showGoal(String startDate, String endDate, int pointsTarget, String pointsAchieved, int initialPoints) {
        emptyView.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
        setUpRecyclerViewWithActivity((RecyclerView) findViewById(R.id.main_recyclerview),
                getRange(startDate, endDate), pointsTarget, pointsAchieved, initialPoints);
        swipeRefreshLayout.setEnabled(true);
    }

    @Override
    public void showUnclaimedRewardsCount(long unclaimedRewardsCount) {
//        Toast.makeText(this, String.format("%d", unclaimedRewardsCount), Toast.LENGTH_SHORT).show();
    }

    private String getRange(String startDate, String endDate) {
        if (isDateEmpty(startDate, endDate)) {
            return "";
        }
        return formatDate(startDate) + " - " + formatDate(endDate);
    }

    private String formatDate(String startDate) {
        return dateFormattingUtilities.formatDateAbbreviatedWeekdayAbbreviatedMonth(new LocalDate(startDate));
    }

    private boolean isDateEmpty(String startDate, String endDate) {
        return TextUtilities.isNullOrWhitespace(startDate) || TextUtilities.isNullOrWhitespace(endDate);
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        if (event.getType().equals(AR_LINK_DEVICE_EVENT) &&
                event.getClickedButtonType() == AlertDialogFragment.DismissedEvent.ClickedButtonType.Positive) {
            navigationCoordinator.navigateToWellnessDeviceLandingAfterOnboarding(this, true);
        }
    }

    private class WeeklyTargetViewHolderFactory implements GenericRecyclerViewAdapter.IViewHolderFactory<WeeklyTargetItem, ActiveRewardsWeeklyTargetViewHolder> {
        @Override
        public ActiveRewardsWeeklyTargetViewHolder createViewHolder(View itemView) {

            return new ActiveRewardsWeeklyTargetViewHolder.Builder()
                    .setActivityItemViewHolderFactory()
                    .setOnItemClickListener(ActiveRewardsLandingActivity.this)
                    .setData(presenter.getActivityList())
                    .build(itemView);
        }

    }
}
