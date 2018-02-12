package com.vitalityactive.va.eventsfeed.views.fragments;

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

import com.vitalityactive.va.BaseFragment;
import com.vitalityactive.va.EmptyStateViewHolder;
import com.vitalityactive.va.R;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.eventsfeed.EventsFeedCategoriesSelectedEvent;
import com.vitalityactive.va.eventsfeed.EventsFeedDay;
import com.vitalityactive.va.eventsfeed.EventsFeedIconProvider;
import com.vitalityactive.va.eventsfeed.EventsFeedRefreshCompletedEvent;
import com.vitalityactive.va.eventsfeed.presentation.EventsFeedMonthPresenter;
import com.vitalityactive.va.eventsfeed.views.adapters.AdapterDays;
import com.vitalityactive.va.networking.RequestFailedEvent;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.date.Date;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

import java.util.List;

import javax.inject.Inject;

public class EventsFeedMonthFragment extends BaseFragment implements EmptyStateViewHolder.EmptyStatusButtonClickedListener, EventListener<EventsFeedRefreshCompletedEvent> {
    private static final String ARGUMENT_MONTH = "MONTH";
    private SwipeRefreshLayout swipeRefreshLayout;
    private LayoutInflater inflater;

    @Inject EventDispatcher eventDispatcher;
    @Inject EventsFeedMonthPresenter presenter;
    @Inject EventsFeedIconProvider iconProvider;
    @Inject DateFormattingUtilities dateFormattingUtilities;
    @Inject TimeUtilities timeUtilities;

    @ColorInt private int globalTintColor;

    private RecyclerView recyclerView;
    private AdapterDays adapter;

    private Date firstDayOfMonth;
    private Date lastDayOfMonth;

    //listeners
    private EventListener<EventsFeedCategoriesSelectedEvent> categoriesSelectedEventListener = new EventListener<EventsFeedCategoriesSelectedEvent>() {
        @Override
        public void onEvent(EventsFeedCategoriesSelectedEvent event) {
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

    public static EventsFeedMonthFragment newInstance(int month, String globalTintColor) {
        EventsFeedMonthFragment fragment = new EventsFeedMonthFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_MONTH, month);
        arguments.putString(GLOBAL_TINT_COLOR, globalTintColor);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events_feed_month, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDependencyInjector().inject(this);

        if (getView() == null) {
            return;
        }

        inflater = getActivity().getLayoutInflater();

        eventDispatcher.addEventListener(EventsFeedCategoriesSelectedEvent.class, categoriesSelectedEventListener);
        globalTintColor = Color.parseColor(getArguments().getString(GLOBAL_TINT_COLOR));

        int monthIndex = getArguments().getInt(ARGUMENT_MONTH);

        firstDayOfMonth = timeUtilities.now().minusMonths(monthIndex).toFirstDayOfMonth();
        lastDayOfMonth = timeUtilities.now().minusMonths(monthIndex).toLastDayOfMonth();

        List<EventsFeedDay> eventsFeedDays = presenter.getEventsFeed(firstDayOfMonth, lastDayOfMonth);
        setUpRecyclerView((RecyclerView) getView().findViewById(R.id.events_feed_month_recycler_view), eventsFeedDays);
        setUpSwipeRefreshLayout((SwipeRefreshLayout) getView().findViewById(R.id.events_feed_swipe_refresh_layout), globalTintColor);
    }



    @Override
    protected void onAppear() {
        eventDispatcher.addEventListener(EventsFeedRefreshCompletedEvent.class, this);
        eventDispatcher.addEventListener(RequestFailedEvent.class, requestFailedEventEventListener);
        swipeRefreshLayout.setRefreshing(presenter.isCurrentlyRefreshing());
    }

    @Override
    protected void onDisappear() {
        eventDispatcher.removeEventListener(EventsFeedRefreshCompletedEvent.class, this);
        eventDispatcher.removeEventListener(RequestFailedEvent.class, requestFailedEventEventListener);
    }

    @Override
    public void onEmptyStateButtonClicked(EmptyStateViewHolder emptyStateViewHolder) {
        Log.d("PointsMonitorMonthFragm", "onEmptyStateButtonClicked()");
    }

    @Override
    public void onEvent(EventsFeedRefreshCompletedEvent event) {
        swipeRefreshLayout.setRefreshing(false);
        reloadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventDispatcher.removeEventListener(EventsFeedCategoriesSelectedEvent.class, categoriesSelectedEventListener);
    }

    private void reloadData() {
        adapter.setData(presenter.getEventsFeed(firstDayOfMonth, lastDayOfMonth));
        recyclerView.setAdapter(recyclerView.getAdapter());
    }

    private void setUpSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout, @ColorInt int globalTintColor) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        swipeRefreshLayout.setColorSchemeColors(globalTintColor);
        swipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) getParentFragment());
    }

    private void setUpRecyclerView(RecyclerView recyclerView, @NonNull List<EventsFeedDay> days) {
        this.recyclerView = recyclerView;
        adapter = new AdapterDays(getActivity(), days, inflater, globalTintColor,
                presenter, dateFormattingUtilities, this, iconProvider);
        recyclerView.setAdapter(adapter);
        ViewUtilities.addDividers(EventsFeedMonthFragment.this.getContext(), recyclerView);
    }
}
