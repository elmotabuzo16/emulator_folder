package com.vitalityactive.va.pointsmonitor;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import com.vitalityactive.va.EmptyStateViewHolder;
import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.PointsEntryCategory;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dto.PointsEntryCategoryDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestFailedEvent;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PointsMonitorFragment
        extends BasePresentedFragment<PointsMonitorPresenter.UserInterface, PointsMonitorPresenter>
        implements PointsMonitorPresenter.UserInterface, SwipeRefreshLayout.OnRefreshListener, EventListener<AlertDialogFragment.DismissedEvent> {

    public static final String SELECTED_MONTH_BUNDLE_KEY = "SELECTED_MONTH";
    private static final String POINTS_MONITOR_CATEGORY_FILTER = "POINTS_MONITOR_CATEGORY_FILTER";
    private static final String ALERT_DIALOG_MESSAGE = "ALERT_DIALOG_MESSAGE";
    @Inject
    PointsMonitorPresenter presenter;
    @Inject
    PointsMonitorAvailablePointsCategoriesProvider availablePointsCategoriesProvider;
    @Inject
    EventDispatcher eventDispatcher;
    @Inject
    PointsMonitorIconProvider iconProvider;

    private TextView stickyYearTitle;
    private Point windowSize;
    private String globalTintColor;
    private Adapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private View loadingIndicator;
    private int selectedMonth;
    private TextView toolbarTitle;
    private final EventListener<PointsMonitorCategoriesSelectedEvent> categoriesSelectedListener = new EventListener<PointsMonitorCategoriesSelectedEvent>() {
        @Override
        public void onEvent(PointsMonitorCategoriesSelectedEvent event) {
            setToolbarTitleText();
        }
    };
    private EventListener<RequestFailedEvent> requestFailedEventEventListener;

    private void setToolbarTitleText() {
        toolbarTitle.setText(presenter.getSelectedCategory().getTitle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_points_monitor, container, false);
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
        setUpViewPager((ViewPager) getViewById(R.id.points_monitor_viewpager, getView()));
        setUpTabLayout((TabLayout) getViewById(R.id.points_monitor_tabs, getView()));

        if (savedInstanceState != null) {
            selectedMonth = savedInstanceState.getInt(SELECTED_MONTH_BUNDLE_KEY);
            viewPager.setCurrentItem(selectedMonth);
        }

        createRequestFailureEventListener();
        setUpWindowSize();
        setUpCategoryFilter();
        setUpStickyYearTitle((TextView) getView().findViewById(R.id.points_monitor_sticky_year_title));
        setToolbarDrawerIconColourToWhite();
    }

    private void createRequestFailureEventListener() {
        requestFailedEventEventListener = new EventListener<RequestFailedEvent>() {
            @Override
            public void onEvent(RequestFailedEvent event) {
                int snackBarErrorMessageId;
                int errorMessageId;

                final RequestFailedEvent.Type errorType = event.getType();

                if (errorType == RequestFailedEvent.Type.CONNECTION_ERROR) {
                    snackBarErrorMessageId = R.string.connectivity_error_alert_title_44;
                    errorMessageId = R.string.connectivity_error_alert_message_45;
                } else {
                    snackBarErrorMessageId = R.string.PM_alert_unable_to_update_title_557;
                    errorMessageId = R.string.PM_alert_unable_to_update_points_message_558;
                }

                if (presenter.cachedDataExists()) {

                    String errorTitle = getResources().getString(snackBarErrorMessageId) ;
                    String errorMessage = getResources().getString(errorMessageId);
                    String negativeText = getResources().getString(R.string.cancel_button_title_24).toUpperCase();
                    String positiveText = getResources().getString(R.string.try_again_button_title_43).toUpperCase();

                    showAlertDialogMessage(errorTitle, errorMessage, positiveText, negativeText );

                    //noinspection ConstantConditions
                    // Remove snackbar and replace it with pop up alert
                    /*Snackbar.make(getView().findViewById(R.id.fragment_points_monitor), snackBarErrorMessageId, Snackbar.LENGTH_LONG)
                            .setAction(R.string.email_preference_snackbar_retry_button_text, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onRefresh();
                                }
                            })
                            .show();*/
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

    private void showAlertDialogMessage(String title, String message, String positiveMessage, String negativeMessage){
        AlertDialogFragment alert = AlertDialogFragment.create(
                ALERT_DIALOG_MESSAGE,
                title,
                message,
                negativeMessage,
                null,
                positiveMessage);
        alert.show(getFragmentManager(), ALERT_DIALOG_MESSAGE);
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
                    PointsHistoryMonth pointsHistoryMonth = getPointsHistoryMonth(i);

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

    private PointsHistoryMonth getSelectedPointsHistoryMonth() {
        return getPointsHistoryMonth(tabLayout.getSelectedTabPosition());
    }

    private TabLayout.Tab getSelectedTab() {
        return tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
    }

    private boolean isYearInTabTitle(PointsHistoryMonth pointsHistoryMonth, TabLayout.Tab tab) {
        return tab.getText() != null && tab.getText().length() > getTabTitle(pointsHistoryMonth).length();
    }

    @NonNull
    private String getTabTitle(PointsHistoryMonth pointsHistoryMonth) {
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
        toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setVisibility(View.VISIBLE);
        setToolbarTitleText();
        toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogFragment.create(
                        POINTS_MONITOR_CATEGORY_FILTER,
                        getString(R.string.PM_category_filter_header_514),
                        getCategories(),
                        null,
                        null,
                        null,
                        null)
                        .setCustomPrimaryColor(globalTintColor)
                        .show(getChildFragmentManager(), POINTS_MONITOR_CATEGORY_FILTER);
            }
        });
    }

    @NonNull
    private ArrayList<AlertDialogFragment.AlertDialogItem> getCategories() {
        ArrayList<AlertDialogFragment.AlertDialogItem> alertDialogItems = new ArrayList<>();
        alertDialogItems.add(getAllPointsAlertDialogItem());
        alertDialogItems.add(getAssesmentAlertDialogItem());
        alertDialogItems.add(getScreeningAlertDialogItem());
        alertDialogItems.add(getGetActiveAlertDialogItem());
        for (PointsEntryCategoryDTO category : availablePointsCategoriesProvider.getPointsEntryCategories()) {
            alertDialogItems.add(getMultiChoiceItem(category));
        }
        alertDialogItems.add(getOtherAlertDialogItem());
        return alertDialogItems;
    }

    private AlertDialogFragment.AlertDialogItem getMultiChoiceItem(PointsEntryCategoryDTO category) {
        return getMultiChoiceItem(category.getTitle(), category.getTypeKey(), isChecked(category.getTypeKey()));
    }

    @NonNull
    private AlertDialogFragment.AlertDialogItem getMultiChoiceItem(String title, int categoryTypeKey, boolean checked) {
        return new AlertDialogFragment.AlertDialogItem(title, iconProvider.getIconResourceId(categoryTypeKey), categoryTypeKey, checked);
    }


    @NonNull
    private AlertDialogFragment.AlertDialogItem getScreeningAlertDialogItem() {
        return getMultiChoiceItem(getString(R.string.PM_category_filter_screening_title_518), PointsEntryCategory._SCREENING, isScreeningChecked());
    }
    @NonNull
    private AlertDialogFragment.AlertDialogItem getGetActiveAlertDialogItem() {
        return getMultiChoiceItem(getString(R.string.PM_category_filter_fitness_title_519), PointsEntryCategory._FITNESS, isFitnessChecked());
    }

    @NonNull
    private AlertDialogFragment.AlertDialogItem getAllPointsAlertDialogItem() {
        return getMultiChoiceItem(getString(R.string.PM_category_filter_all_points_title_515), 999, isAllPointsChecked());
    }

    @NonNull
    private AlertDialogFragment.AlertDialogItem getAssesmentAlertDialogItem() {
        return getMultiChoiceItem(getString(R.string.PM_category_filter_assessment_title_516), PointsEntryCategory._ASSESSMENT, isAssessmentChecked());
        // return getMultiChoiceItem(getString(R.string.PM_category_filter_all_points_title_515), 5, isAllPointsChecked());
    }

    @NonNull
    private AlertDialogFragment.AlertDialogItem getOtherAlertDialogItem() {
        return getMultiChoiceItem(getString(R.string.PM_category_filter_pother_title_521), -1, isOtherChecked());
    }


    private boolean isChecked(int identifier) {
        return presenter.getSelectedCategory().getTypeKey() == identifier;
    }

    private boolean isAllPointsChecked() {
        return presenter.getSelectedCategory().isAll();
    }

    private boolean isOtherChecked() {
        return presenter.getSelectedCategory().isOther();
    }
    private boolean isAssessmentChecked() {
        return presenter.getSelectedCategory().isAssessment();
    }

    private boolean isScreeningChecked() {
        return presenter.getSelectedCategory().isScreening();
    }

    private boolean isFitnessChecked() {
        return presenter.getSelectedCategory().isFitness();
    }

    private ActionBar getSupportActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

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
    protected PointsMonitorPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected PointsMonitorPresenter getPresenter() {
        return presenter;
    }

    private void setUpViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.setOffscreenPageLimit(1);
        adapter = new Adapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
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

    private PointsHistoryMonth getPointsHistoryMonth(int index) {
        return adapter.getData().get(index);
    }

    private void addYearToSelectedTab(int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        if (tab != null) {
            PointsHistoryMonth pointsHistoryMonth = getPointsHistoryMonth(position);
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
        eventDispatcher.addEventListener(AlertDialogFragment.DismissedEvent.class, this);
        eventDispatcher.addEventListener(PointsMonitorCategoriesSelectedEvent.class, categoriesSelectedListener);
        eventDispatcher.addEventListener(RequestFailedEvent.class, requestFailedEventEventListener);
    }

    @Override
    protected void disappear() {
        eventDispatcher.removeEventListener(AlertDialogFragment.DismissedEvent.class, this);
        eventDispatcher.removeEventListener(PointsMonitorCategoriesSelectedEvent.class, categoriesSelectedListener);
        eventDispatcher.removeEventListener(RequestFailedEvent.class, requestFailedEventEventListener);
    }

    @Override
    public void showMonths(@NonNull List<PointsHistoryMonth> months) {
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

    private void addYearIfSelectedTabWasTheFirstOneBecauseOnPageSelectedWontBeCalledToDoItAutomatically() {
        if (selectedMonth == 0) {
            addYearToSelectedTab(selectedMonth);
        }
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

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        if (event.getType().equals(POINTS_MONITOR_CATEGORY_FILTER)) {
            for (AlertDialogFragment.AlertDialogItem alertDialogItem : event.getAlertDialogItems()) {
                if (alertDialogItem.isChecked()) {
                    presenter.onUserSelectsCategory(alertDialogItem.getIdentifier());
                    break;
                }
            }
        }else if(event.getType().equals(ALERT_DIALOG_MESSAGE)){
            if(event.getClickedButtonType() == AlertDialogFragment.DismissedEvent.ClickedButtonType.Positive){
                ((PointsMonitorFragment)getParentFragment()).showLoadingIndicator();
                ((SwipeRefreshLayout.OnRefreshListener) getParentFragment()).onRefresh();
            }
        }
    }

    public static class EmptyStateFragment extends Fragment {
        public static final String ERROR_MESSAGE = "ERROR_MESSAGE";
        public static final String ERROR_SUBTITLE = "ERROR_SUBTITLE";
        private static final String GLOBAL_TINT_COLOR = "GLOBAL_TINT_COLOR";

        @NonNull
        private static EmptyStateFragment newInstance(RequestFailedEvent.Type errorState, int globalTintColor) {
            Bundle bundle = new Bundle();
            bundle.putInt(EmptyStateFragment.GLOBAL_TINT_COLOR , globalTintColor);

            if (errorState == RequestFailedEvent.Type.CONNECTION_ERROR) {
                bundle.putInt(EmptyStateFragment.ERROR_MESSAGE, R.string.connectivity_error_alert_title_44);
                bundle.putInt(EmptyStateFragment.ERROR_SUBTITLE, R.string.connectivity_error_alert_message_45);
            } else {
                bundle.putInt(EmptyStateFragment.ERROR_MESSAGE, R.string.error_unable_to_load_title_503);
                bundle.putInt(EmptyStateFragment.ERROR_SUBTITLE, R.string.error_unable_to_load_message_504);
            }

            EmptyStateFragment emptyStateFragment = new EmptyStateFragment();
            emptyStateFragment.setArguments(bundle);

            return emptyStateFragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View inflatedView = inflater.inflate(R.layout.empty_state, container, false);

            int messageId = getArguments().getInt(ERROR_MESSAGE);
            int subtitleId = getArguments().getInt(ERROR_SUBTITLE);
            int globalTintColor = getArguments().getInt(GLOBAL_TINT_COLOR);

            new EmptyStateViewHolder(inflatedView).setup(messageId,
                    subtitleId,
                    R.string.try_again_button_title_43,
                    new EmptyStateViewHolder.EmptyStatusButtonClickedListener() {
                        @Override
                        public void onEmptyStateButtonClicked(EmptyStateViewHolder emptyStateViewHolder) {
                            ((PointsMonitorFragment)getParentFragment()).showLoadingIndicator();
                            ((SwipeRefreshLayout.OnRefreshListener) getParentFragment()).onRefresh();
                        }
                    }).setButtonColor(globalTintColor);

            return inflatedView;
        }
    }

    private class Adapter extends FragmentStatePagerAdapter {
        public RequestFailedEvent.Type errorState;
        @NonNull
        private List<PointsHistoryMonth> data = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (errorState != null) {
                return EmptyStateFragment.newInstance(errorState, Color.parseColor(globalTintColor));
            }

            return PointsMonitorMonthFragment.newInstance(position, globalTintColor);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getTabTitle(data.get(position));
        }

        @NonNull
        public List<PointsHistoryMonth> getData() {
            return data;
        }

        public void setData(@NonNull List<PointsHistoryMonth> data) {
            this.data = data;
        }
    }
}
