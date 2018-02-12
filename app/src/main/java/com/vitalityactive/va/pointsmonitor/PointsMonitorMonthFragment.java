package com.vitalityactive.va.pointsmonitor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vitalityactive.va.BaseFragment;
import com.vitalityactive.va.EmptyStateViewHolder;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.viewholder.TitleViewHolder;
import com.vitalityactive.va.constants.PointsEntryCategory;
import com.vitalityactive.va.dto.PointsEntryCategoryDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestFailedEvent;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

import java.util.List;

import javax.inject.Inject;

public class PointsMonitorMonthFragment extends BaseFragment implements EmptyStateViewHolder.EmptyStatusButtonClickedListener, EventListener<PointsMonitorRefreshCompletedEvent> {
    private static final String ARGUMENT_MONTH = "MONTH";
    private SwipeRefreshLayout swipeRefreshLayout;
    private LayoutInflater inflater;

    @Inject
    EventDispatcher eventDispatcher;
    @Inject
    PointsMonitorMonthPresenter presenter;
    @Inject
    PointsMonitorIconProvider iconProvider;
    @Inject
    DateFormattingUtilities dateFormattingUtilities;

    @ColorInt
    private int globalTintColor;

    private EventListener<PointsMonitorCategoriesSelectedEvent> categoriesSelectedEventListener = new EventListener<PointsMonitorCategoriesSelectedEvent>() {
        @Override
        public void onEvent(PointsMonitorCategoriesSelectedEvent event) {
            reloadData();
        }
    };

    private EventListener<RequestFailedEvent> requestFailedEventEventListener = new EventListener<RequestFailedEvent>() {
        @Override
        public void onEvent(RequestFailedEvent event) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    };

    private void reloadData() {
        adapter.setData(presenter.getPointsEntries(getArguments().getInt(ARGUMENT_MONTH)));
        recyclerView.setAdapter(recyclerView.getAdapter());
    }

    private RecyclerView recyclerView;

    private DaysAdapter adapter;

    public static PointsMonitorMonthFragment newInstance(int month, String globalTintColor) {
        PointsMonitorMonthFragment fragment = new PointsMonitorMonthFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_MONTH, month);
        arguments.putString(GLOBAL_TINT_COLOR, globalTintColor);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_points_monitor_month, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDependencyInjector().inject(this);

        if (getView() == null) {
            return;
        }

        inflater = getActivity().getLayoutInflater();

        eventDispatcher.addEventListener(PointsMonitorCategoriesSelectedEvent.class, categoriesSelectedEventListener);
        globalTintColor = Color.parseColor(getArguments().getString(GLOBAL_TINT_COLOR));

        setUpRecyclerView((RecyclerView) getView().findViewById(R.id.points_monitor_month_recycler_view), presenter.getPointsEntries(getArguments().getInt(ARGUMENT_MONTH)));
        setUpSwipeRefreshLayout((SwipeRefreshLayout) getView().findViewById(R.id.points_monitor_swipe_refresh_layout), globalTintColor);
    }

    @Override
    protected void onAppear() {
        eventDispatcher.addEventListener(PointsMonitorRefreshCompletedEvent.class, this);
        eventDispatcher.addEventListener(RequestFailedEvent.class, requestFailedEventEventListener);
        swipeRefreshLayout.setRefreshing(presenter.isCurrentlyRefreshing());
    }

    @Override
    protected void onDisappear() {
        eventDispatcher.removeEventListener(PointsMonitorRefreshCompletedEvent.class, this);
        eventDispatcher.removeEventListener(RequestFailedEvent.class, requestFailedEventEventListener);
    }

    private void setUpSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout, @ColorInt int globalTintColor) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        swipeRefreshLayout.setColorSchemeColors(globalTintColor);
        swipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) getParentFragment());
    }

    private void setUpRecyclerView(RecyclerView recyclerView, @NonNull List<PointsHistoryDay> days) {
        this.recyclerView = recyclerView;
        adapter = new DaysAdapter(days);
        recyclerView.setAdapter(adapter);
        ViewUtilities.addDividers(PointsMonitorMonthFragment.this.getContext(), recyclerView);
    }

    @Override
    public void onEmptyStateButtonClicked(EmptyStateViewHolder emptyStateViewHolder) {
        Log.d("PointsMonitorMonthFragm", "onEmptyStateButtonClicked()");
    }

    @Override
    public void onEvent(PointsMonitorRefreshCompletedEvent event) {
        swipeRefreshLayout.setRefreshing(false);
        reloadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventDispatcher.removeEventListener(PointsMonitorCategoriesSelectedEvent.class, categoriesSelectedEventListener);
    }

    private class DaysAdapter extends RecyclerView.Adapter {

        @NonNull
        private List<PointsHistoryDay> days;

        DaysAdapter(@NonNull List<PointsHistoryDay> days) {
            this.days = days;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(viewType, parent, false);
            if (viewType == R.layout.points_monitor_day_item) {
                return new DayViewHolder(itemView, globalTintColor);
            }
            if (viewType == R.layout.points_monitor_no_more_points_item) {
                return new TitleViewHolder(itemView);
            }
            return new EmptyStateViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (isLastItem(position)) {
                ((TitleViewHolder)holder).bindWith(getNoMoreItemsText());
            } else if (hasData()) {
                ((DayViewHolder) holder).bindWith(days.get(position));
            } else {
                EmptyState emptyState = getEmptyState();
                EmptyStateViewHolder emptyStateViewHolder = (EmptyStateViewHolder) holder;
                emptyStateViewHolder.setButtonColor(globalTintColor);
                emptyStateViewHolder.setup(emptyState.title, emptyState.subtitle, emptyState.buttonTitle, PointsMonitorMonthFragment.this);
            }
        }

        private String getNoMoreItemsText() {
            PointsEntryCategoryDTO selectedCategory = presenter.getSelectedCategory();
            if (isAssessmentSelected(selectedCategory)) {
                return getNoMoreItemsText(getString(R.string.PM_footnote_empty_state_assessment_message_545));
            } else if (isHealthyFoodSelected(selectedCategory)) {
                return getNoMoreItemsText(getString(R.string.PM_footnote_empty_state_nutrition_message_546));
            } else if (isFitnessSelected(selectedCategory)) {
                return getNoMoreItemsText(getString(R.string.PM_footnote_empty_state_fitness_message_548));
            } else if (isScreeningSelected(selectedCategory)) {
                return getNoMoreItemsText(getString(R.string.PM_footnote_empty_state_screening_message_547));
            } else if (isOtherSelected(selectedCategory)) {
                return getNoMoreItemsText(getString(R.string.PM_footnote_empty_state_other_message_551));
            }
            return getNoMoreItemsText(getString(R.string.PM_footnote_empty_state_all_points_message_544));
        }

        private String getNoMoreItemsText(String formatString) {
            return String.format(formatString,
                    dateFormattingUtilities.formatMonthYear(days.get(0).getDate()));
        }

        private EmptyState getEmptyState() {
            PointsEntryCategoryDTO selectedCategory = presenter.getSelectedCategory();
            if (isAssessmentSelected(selectedCategory)) {
                return new EmptyState(R.string.PM_empty_state_assessment_title_528, R.string.PM_empty_state_assessment_message_529, R.string.help_button_title_141);
            } else if (isHealthyFoodSelected(selectedCategory)) {
                return new EmptyState(R.string.PM_empty_state_nutrition_title_530, R.string.PM_empty_state_nutrition_message_531, R.string.help_button_title_141);
            } else if (isFitnessSelected(selectedCategory)) {
                return new EmptyState(R.string.PM_empty_state_fitness_title_534, R.string.PM_empty_state_fitness_message_535, R.string.help_button_title_141);
            } else if (isScreeningSelected(selectedCategory)) {
                return new EmptyState(R.string.PM_empty_state_screening_title_532, R.string.PM_empty_state_screening_message_533, R.string.help_button_title_141);
            }
            return new EmptyState(R.string.PM_empty_state_all_points_title_524, R.string.PM_empty_state_all_points_message_525, R.string.help_button_title_141);
        }

        private boolean isScreeningSelected(PointsEntryCategoryDTO selectedCategory) {
            return isSelected(selectedCategory, PointsEntryCategory._SCREENING);
        }

        private boolean isFitnessSelected(PointsEntryCategoryDTO selectedCategory) {
            return isSelected(selectedCategory, PointsEntryCategory._FITNESS);
        }

        private boolean isHealthyFoodSelected(PointsEntryCategoryDTO selectedCategory) {
            return isSelected(selectedCategory, PointsEntryCategory._HEALTHYFOOD);
        }

        private boolean isAssessmentSelected(PointsEntryCategoryDTO selectedCategory) {
            return isSelected(selectedCategory, PointsEntryCategory._ASSESSMENT);
        }

        private boolean isOtherSelected(PointsEntryCategoryDTO selectedCategory) {
            return selectedCategory.isOther();
        }

        private boolean isSelected(PointsEntryCategoryDTO selectedCategory, int categoryTypeKey) {
            return selectedCategory.getTypeKey() == categoryTypeKey;
        }

        @Override
        public int getItemCount() {
            return Math.max(1, days.size() + 1);
        }

        @Override
        public int getItemViewType(int position) {
            if (isLastItem(position)) {
                return R.layout.points_monitor_no_more_points_item;
            }
            if (hasData()) {
                return R.layout.points_monitor_day_item;
            }
            return R.layout.empty_state;
        }

        private boolean isLastItem(int position) {
            return position == days.size() && hasData();
        }

        private boolean hasData() {
            return !days.isEmpty();
        }

        public void setData(@NonNull List<PointsHistoryDay> days) {
            this.days = days;
        }

        private class EmptyState {
            private final int title;
            private final int subtitle;
            private final int buttonTitle;
            EmptyState(int title, int subtitle, int buttonTitle) {
                this.title = title;
                this.subtitle = subtitle;
                this.buttonTitle = buttonTitle;
            }
        }
    }

    private class PointsItemsAdapter extends RecyclerView.Adapter {

        private PointsHistoryDay day;
        private final int globalTintColor;

        PointsItemsAdapter(PointsHistoryDay day, int globalTintColor) {
            this.day = day;
            this.globalTintColor = globalTintColor;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PointsItemViewHolder(inflater.inflate(viewType, parent, false), iconProvider, globalTintColor);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            ((PointsItemViewHolder) holder).bindWith(day.getPointsEntries().get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PointsMonitorEntryDetailActivity.class);
                    intent.putExtra(PointsMonitorEntryDetailActivity.POINTS_ENTRY_ID, day.getPointsEntries().get(holder.getAdapterPosition()).getId());
                    intent.putExtra(PointsMonitorEntryDetailActivity.GLOBAL_TINT_COLOR, globalTintColor);
                    getActivity().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return day.getPointsEntries().size();
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.points_monitor_points_item;
        }
    }

    private class DayViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerView recyclerView;
        private final int globalTintColor;
        private final TextView title;

        DayViewHolder(View itemView, int globalTintColor) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            this.globalTintColor = globalTintColor;
        }

        public void bindWith(PointsHistoryDay day) {
            title.setText(dateFormattingUtilities.formatDateMonthAbbreviatedYear(day.getDate()));
            recyclerView.setAdapter(new PointsItemsAdapter(day, globalTintColor));
            ViewUtilities.addDividers(getContext(), recyclerView, getResources().getDimensionPixelSize(R.dimen.item_divider_inset));
        }
    }
}
