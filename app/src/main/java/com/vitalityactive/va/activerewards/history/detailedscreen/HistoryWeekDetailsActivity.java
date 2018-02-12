package com.vitalityactive.va.activerewards.history.detailedscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.Utils;
import com.vitalityactive.va.activerewards.dto.ActivityItem;
import com.vitalityactive.va.activerewards.dto.GoalTrackerOutDto;
import com.vitalityactive.va.activerewards.viewholder.EmptyActivityListContainer;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class HistoryWeekDetailsActivity
        extends BasePresentedActivity<HistoryWeekDetailsPresenter.UserInterface, HistoryWeekDetailsPresenter>
        implements HistoryWeekDetailsPresenter.UserInterface {
    public static final String EXTRA_DATA = "EXTRA_DATA";
    private GoalTrackerOutDto data;
    private ContainersRecyclerViewAdapter containersRecyclerViewAdapter;
    private ActivityListContainer activityListContainer;
    private RecyclerView mainRecyclerView;

    @Inject
    HistoryWeekDetailsPresenter presenter;
    @Inject
    DateFormattingUtilities dateFormattingUtilities;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);

        setContentView(R.layout.activity_ar_history_week_detailed);
        data = getIntent().getParcelableExtra(EXTRA_DATA);

        activityListContainer = new ActivityListContainer(this, getString(R.string.AR_landing_activity_cell_title_711), null);

        setUpActionBarWithTitle(createScreenTitle()).setDisplayHomeAsUpEnabled(true);
        mainRecyclerView = findViewById(R.id.main_recyclerview);
        setUpRecyclerView(mainRecyclerView);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected HistoryWeekDetailsPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected HistoryWeekDetailsPresenter getPresenter() {
        return presenter;
    }

    private void setUpRecyclerView(final RecyclerView recyclerView) {
        ArrayList<GenericRecyclerViewAdapter> adapters = setupAdapters();
        containersRecyclerViewAdapter = new ContainersRecyclerViewAdapter(adapters);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(containersRecyclerViewAdapter);
        ViewUtilities.addDividers(this, mainRecyclerView);
        ViewUtilities.scrollToTop(recyclerView);
    }

    private ArrayList<GenericRecyclerViewAdapter> setupAdapters() {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();
        adapters.add(createHeaderContainer());
        adapters.add(createContentAdapter());
        adapters.add(createFooterContainer());
        return adapters;
    }

    private GenericRecyclerViewAdapter<GoalTrackerOutDto, HistoryActivityItemViewHolder> createHeaderContainer() {
        return new GenericRecyclerViewAdapter<>(this,
                Collections.singletonList(data),
                R.layout.view_ar_history_details_header, //R.layout.view_ar_history_list_item,
                new HistoryActivityItemViewHolder.Factory());
    }

    private GenericRecyclerViewAdapter createContentAdapter() {
        List<ActivityItem> result = Utils.getActivityList(data);
        if (result.isEmpty()) {
            return new GenericRecyclerViewAdapter<>(this,
                    Collections.singletonList(new ActivityItem(null, null, null, null)),
                    R.layout.view_ar_no_activity_in_period,
                    new EmptyActivityListContainer.Factory());
        } else {
            activityListContainer.setActivityList(result);

            return new GenericRecyclerViewAdapter<>(this,
                    activityListContainer,
                    R.layout.view_ar_activity_list,
                    new GenericTitledListContainerWithAdapter.Factory<>());
        }
    }

    private GenericRecyclerViewAdapter createFooterContainer() {
        return new GenericRecyclerViewAdapter<>(this,
                Collections.singletonList(""),
                R.layout.view_footer_string,
                new FooterViewHolder.Factory());
    }

    private String createScreenTitle() {
        return dateFormattingUtilities.formatRangeDateMonthAbbreviatedYear(new LocalDate(data.getEffectiveFrom()),
                new LocalDate(data.getEffectiveTo()));
    }
}
