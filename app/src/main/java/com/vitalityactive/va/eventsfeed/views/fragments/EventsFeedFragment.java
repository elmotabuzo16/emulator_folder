package com.vitalityactive.va.eventsfeed.views.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedFragment;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.eventsfeed.EventsFeedAlertDialogFragment;
import com.vitalityactive.va.eventsfeed.EventsFeedAvailableEventsCategoriesProvider;
import com.vitalityactive.va.eventsfeed.EventsFeedCategoriesSelectedEvent;
import com.vitalityactive.va.eventsfeed.EventsFeedIconProvider;
import com.vitalityactive.va.eventsfeed.data.EventsFeedMonth;
import com.vitalityactive.va.eventsfeed.data.dto.EventsFeedCategoryDTO;
import com.vitalityactive.va.eventsfeed.presentation.EventsFeedPresenter;
import com.vitalityactive.va.eventsfeed.views.adapters.AdapterEventsFeed;
import com.vitalityactive.va.networking.RequestFailedEvent;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class EventsFeedFragment extends BasePresentedFragment<EventsFeedPresenter.UserInterface, EventsFeedPresenter>
        implements EventsFeedPresenter.UserInterface, SwipeRefreshLayout.OnRefreshListener, EventListener<EventsFeedAlertDialogFragment.DismissedEvent> {

    public static final String SELECTED_MONTH_BUNDLE_KEY = "SELECTED_MONTH";
    private static final String EVENTS_FEED_CATEGORY_FILTER = "EVENTS_FEED_CATEGORY_FILTER";

    @Inject EventsFeedPresenter presenter;
    @Inject EventsFeedAvailableEventsCategoriesProvider eventsFeedAvailableEventsCategoriesProvider;
    @Inject EventDispatcher eventDispatcher;
    @Inject EventsFeedIconProvider iconProvider;

    private TextView stickyYearTitle;
    private Point windowSize;
    private String globalTintColor;
    private AdapterEventsFeed adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private View loadingIndicator;
    private int selectedMonth;
    private TextView toolbarTitle;

    private final EventListener<EventsFeedCategoriesSelectedEvent> categoriesSelectedListener = new EventListener<EventsFeedCategoriesSelectedEvent>() {
        @Override
        public void onEvent(EventsFeedCategoriesSelectedEvent event) {
            setToolbarTitleText();
        }
    };
    private EventListener<RequestFailedEvent> requestFailedEventEventListener;

    private void setToolbarTitleText() {
        toolbarTitle.setText(presenter.getSelectedCategory().getTitle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events_feed, container, false);
    }

    @Override
    protected void activityCreated(@Nullable Bundle savedInstanceState) {
        if (getView() == null) {
            return;
        }

        globalTintColor = getArguments().getString(GLOBAL_TINT_COLOR);
        setLoadingIndicatorColor(globalTintColor);

        flattenToolbar();
        loadingIndicator = getViewById(R.id.loading_indicator, getView());
        setUpViewPager((ViewPager) getViewById(R.id.events_feed_viewpager, getView()));
        setUpTabLayout((TabLayout) getViewById(R.id.events_feed_tabs, getView()));

        if (savedInstanceState != null) {
            selectedMonth = savedInstanceState.getInt(SELECTED_MONTH_BUNDLE_KEY);
            viewPager.setCurrentItem(selectedMonth);
        }

        createRequestFailureEventListener();
        setUpWindowSize();
        setUpCategoryFilter();
        setUpStickyYearTitle((TextView) getView().findViewById(R.id.events_feed_sticky_year_title));
        setToolbarDrawerIconColourToWhite();
    }


    //EventsFeedPresenter.UserInterface
    @Override
    public void showMonths(@NonNull List<EventsFeedMonth> months) {
        adapter.setData(months);
        onPointsSuccessfullyLoaded();
        setStickyYearTitleText();
        addYearIfSelectedTabWasTheFirstOneBecauseOnPageSelectedWontBeCalledToDoItAutomatically();
    }

    @Override
    public void onPointsSuccessfullyLoaded() {
        adapter.errorState = null;
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(selectedMonth);
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }


    //EventListener
    @Override
    public void onEvent(EventsFeedAlertDialogFragment.DismissedEvent event) {

        List<EventsFeedCategoryDTO> selectedCats = new ArrayList<>();
        if (event.getType().equals(EVENTS_FEED_CATEGORY_FILTER) && event.getClickedButtonType() == EventsFeedAlertDialogFragment.DismissedEvent.ClickedButtonType.Positive) {

            for (EventsFeedAlertDialogFragment.AlertDialogItem alertDialogItem : event.getAlertDialogItems()) {
                if (alertDialogItem.isChecked()) {
                    selectedCats.add(new EventsFeedCategoryDTO(alertDialogItem.getIdentifier(), alertDialogItem.getTitle()));
                }
            }

            presenter.onUserSelectsCategory(selectedCats);
        }
    }

    //
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_MONTH_BUNDLE_KEY, selectedMonth);
    }

    private View getViewById(int resourceId, @NonNull View view) {
        return view.findViewById(resourceId);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected EventsFeedPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected EventsFeedPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showLoadingIndicator() {
        ViewUtilities.swapVisibility(loadingIndicator, viewPager);
    }

    @Override
    public void hideLoadingIndicator() {
        ViewUtilities.swapVisibility(viewPager, loadingIndicator);
    }

    @Override
    protected void appear() {
        eventDispatcher.addEventListener(EventsFeedAlertDialogFragment.DismissedEvent.class, this);
        eventDispatcher.addEventListener(EventsFeedCategoriesSelectedEvent.class, categoriesSelectedListener);
        eventDispatcher.addEventListener(RequestFailedEvent.class, requestFailedEventEventListener);
    }

    @Override
    protected void disappear() {
        eventDispatcher.removeEventListener(EventsFeedAlertDialogFragment.DismissedEvent.class, this);
        eventDispatcher.removeEventListener(EventsFeedCategoriesSelectedEvent.class, categoriesSelectedListener);
        eventDispatcher.removeEventListener(RequestFailedEvent.class, requestFailedEventEventListener);
    }

    @Override
    public void onRefresh() {
        selectedMonth = viewPager.getCurrentItem();
        presenter.onUserSwipesToRefresh();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        raiseToolbar();
        resetToolbarTitle();
    }


    private void createRequestFailureEventListener() {
        requestFailedEventEventListener = new EventListener<RequestFailedEvent>() {
            @Override
            public void onEvent(RequestFailedEvent event) {
                int snackBarErrorMessageId;

                final RequestFailedEvent.Type errorType = event.getType();

                if (errorType == RequestFailedEvent.Type.CONNECTION_ERROR) {
                    snackBarErrorMessageId = R.string.connectivity_error_alert_title_44;
                } else {
                    snackBarErrorMessageId = R.string.error_unable_to_load_title_503;
                }

                if (presenter.cachedDataExists()) {
                    //noinspection ConstantConditions
                    Snackbar.make(getView().findViewById(R.id.fragment_events_feed), snackBarErrorMessageId, Snackbar.LENGTH_LONG)
                            .setAction(R.string.AR_connection_error_button_retry_789, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onRefresh();
                                }
                            })
                            .show();
                } else {
                    viewPager.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.errorState = errorType;
                            viewPager.setAdapter(adapter);
                            viewPager.setCurrentItem(selectedMonth);
                            hideLoadingIndicator();
                        }
                    });
                }
            }
        };
    }

    private void setUpTabLayout(final TabLayout tabLayout) {
        this.tabLayout = tabLayout;
        tabLayout.setBackgroundColor(Color.parseColor(globalTintColor));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (getActivity() == null) {
                    tabLayout.getViewTreeObserver().removeOnScrollChangedListener(this);
                    return;
                }
                if (!(tabLayout.getChildAt(0) instanceof ViewGroup)) {
                    return;
                }

                ViewGroup firstChild = (ViewGroup) tabLayout.getChildAt(0);

                if (adapter.getData().size() < firstChild.getChildCount()) {
                    return;
                }

                int stickyTitleChangeDistance = getStickyTitleChangeDistance();

                toggleStickyYearTitleVisibility(firstChild);

                for (int i = 0; i < firstChild.getChildCount(); ++i) {
                    View childAt = firstChild.getChildAt(i);
                    int[] locations = new int[2];
                    childAt.getLocationOnScreen(locations);
                    EventsFeedMonth pointsHistoryMonth = getPointsHistoryMonth(i);

                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    if (tab == null) {
                        continue;
                    }

                    if (locations[0] <= stickyTitleChangeDistance) {
                        setStickyYearTitleText(pointsHistoryMonth.getYear());
                    }
                }
            }
        });
    }

    private void toggleStickyYearTitleVisibility(ViewGroup firstChild) {
        int[] locations = new int[2];
        firstChild.getChildAt(tabLayout.getSelectedTabPosition()).getLocationOnScreen(locations);
        if (locations[0] <= stickyYearTitle.getLeft() || locations[0] > windowSize.x) {
            stickyYearTitle.setVisibility(View.VISIBLE);
            TabLayout.Tab tab = getSelectedTab();
            if (tab != null && isYearInTabTitle(getSelectedPointsHistoryMonth(), tab)) {
                tab.setText(getTabTitle(getSelectedPointsHistoryMonth()));
            }
        } else {
            stickyYearTitle.setVisibility(View.GONE);
            TabLayout.Tab selectedTab = getSelectedTab();
            if (selectedTab != null && selectedTab.getText() != null && selectedTab.getText().length() <= getTabTitle(getSelectedPointsHistoryMonth()).length())
                addYearToSelectedTab(tabLayout.getSelectedTabPosition());
        }
    }

    private EventsFeedMonth getSelectedPointsHistoryMonth() {
        return getPointsHistoryMonth(tabLayout.getSelectedTabPosition());
    }

    private TabLayout.Tab getSelectedTab() {
        return tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
    }

    private boolean isYearInTabTitle(EventsFeedMonth pointsHistoryMonth, TabLayout.Tab tab) {
        return tab.getText() != null && tab.getText().length() > getTabTitle(pointsHistoryMonth).length();
    }

    @NonNull
    private String getTabTitle(EventsFeedMonth pointsHistoryMonth) {
        return pointsHistoryMonth.getMonthName() + "\n";
    }

    private int getStickyTitleChangeDistance() {
        return getDimensionPixelSize(R.dimen.points_monitor_sticky_year_title_change_distance);
    }

    private int getDimensionPixelSize(int dimension) {
        return getResources().getDimensionPixelSize(dimension);
    }

    private void setStickyYearTitleText(String year) {
        stickyYearTitle.setText(year);
    }

    private void setUpWindowSize() {
        windowSize = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(windowSize);
    }

    private void setUpStickyYearTitle(TextView stickyYearTitle) {
        this.stickyYearTitle = stickyYearTitle;
        raiseStickyYearTitle();
    }

    private void setStickyYearTitleText() {
        setStickyYearTitleText(getPointsHistoryMonth(0).getYear());
    }

    private void raiseStickyYearTitle() {
        if (isSDKLollipopOrAbove()) {
            stickyYearTitle.setElevation(getRaisedToolbarElevation());
        }
    }

    private void setUpCategoryFilter() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle = getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.Settings_events_all_title_936));
        toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventsFeedAlertDialogFragment.create(
                        EVENTS_FEED_CATEGORY_FILTER,
                        getString(R.string.Settings_events_popover_title_939),
                        getCategories(),
                        getString(R.string.cancel_button_title_24),
                        getString(R.string.profile_change_email_ok))
                        .setCustomPrimaryColor(globalTintColor)
                        .show(getChildFragmentManager(), EVENTS_FEED_CATEGORY_FILTER);
            }
        });
    }

    @NonNull
    private ArrayList<EventsFeedAlertDialogFragment.AlertDialogItem> getCategories() {
        ArrayList<EventsFeedAlertDialogFragment.AlertDialogItem> alertDialogItems = new ArrayList<>();
        alertDialogItems.add(getAllPointsAlertDialogItem());

        for (EventsFeedCategoryDTO category : eventsFeedAvailableEventsCategoriesProvider.getEventsFeedEntryCategories()) {
            alertDialogItems.add(getMultiChoiceItem(category));
        }
        alertDialogItems.add(getOtherAlertDialogItem());

        return alertDialogItems;
    }

    private EventsFeedAlertDialogFragment.AlertDialogItem getMultiChoiceItem(EventsFeedCategoryDTO category) {
        return getMultiChoiceItem(category.getTitle(), category.getTypeKey(), isChecked(category.getTypeKey()));
    }

    @NonNull
    private EventsFeedAlertDialogFragment.AlertDialogItem getMultiChoiceItem(String title, int categoryTypeKey, boolean checked) {
        return new EventsFeedAlertDialogFragment.AlertDialogItem(title, iconProvider.getIconResourceId(categoryTypeKey), categoryTypeKey, checked);
    }

    @NonNull
    private EventsFeedAlertDialogFragment.AlertDialogItem getAllPointsAlertDialogItem() {
        return getMultiChoiceItem(getString(R.string.Settings_events_all_title_936), 999, isAllPointsChecked());
    }

    @NonNull
    private EventsFeedAlertDialogFragment.AlertDialogItem getOtherAlertDialogItem() {
        return getMultiChoiceItem(getString(R.string.PM_category_filter_pother_title_521), -1, isOtherChecked());
    }

    private boolean isChecked(int identifier) {
//        return presenter.getSelectedCategory().getTypeKey() == identifier;

        for (EventsFeedCategoryDTO categoryDTO: presenter.getSelectedCategories()){
            if(categoryDTO.getTypeKey() == identifier){
                return true;
            }
        }
        return false;

//       return presenter.getSelectedCategories().contains(identifier);
    }

    private boolean isAllPointsChecked() {
        return presenter.getSelectedCategory().isAll();
    }

    private boolean isOtherChecked() {
        return presenter.getSelectedCategory().isOther();
    }

    private ActionBar getSupportActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }


    private void setUpViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.setOffscreenPageLimit(1);
        adapter = new AdapterEventsFeed(getChildFragmentManager(), globalTintColor);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                EventsFeedMonth pointsHistoryMonth = getPointsHistoryMonth(position);
                presenter.setCurrentDateSelected(position, pointsHistoryMonth.getYear());
                selectedMonth = position;
                resetTabNames();
                addYearToSelectedTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void resetTabNames() {
        for (int i = 0; i < tabLayout.getTabCount(); ++i) {
            resetTabName(i);
        }
    }

    private void resetTabName(int index) {
        TabLayout.Tab tab = tabLayout.getTabAt(index);
        if (tab != null) {
            tab.setText(getTabTitle(getPointsHistoryMonth(index)));
        }
    }

    private EventsFeedMonth getPointsHistoryMonth(int index) {
        return adapter.getData().get(index);
    }

    private void addYearToSelectedTab(int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        if (tab != null) {
            EventsFeedMonth pointsHistoryMonth = getPointsHistoryMonth(position);
            tab.setText(pointsHistoryMonth.getMonthName() + "\n" + pointsHistoryMonth.getYear());
        }
    }

    private void flattenToolbar() {
        setToolbarElevationPx(0);
    }

    private void setToolbarElevationPx(int elevationPx) {
        if (isSDKLollipopOrAbove()) {
            getActivity().findViewById(R.id.toolbar).setElevation(elevationPx);
        }
    }

    private void addYearIfSelectedTabWasTheFirstOneBecauseOnPageSelectedWontBeCalledToDoItAutomatically() {
        if (selectedMonth == 0) {
            addYearToSelectedTab(selectedMonth);
        }
    }

    private void resetToolbarTitle() {
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbarTitle.setVisibility(View.GONE);
    }

    private void raiseToolbar() {
        if (isSDKLollipopOrAbove()) {
            setToolbarElevationPx(getRaisedToolbarElevation());
        }
    }

    private int getRaisedToolbarElevation() {
        return getDimensionPixelSize(R.dimen.toolbar_elevation);
    }

    private boolean isSDKLollipopOrAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


}
