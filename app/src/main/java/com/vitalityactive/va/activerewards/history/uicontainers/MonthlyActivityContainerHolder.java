package com.vitalityactive.va.activerewards.history.uicontainers;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.GoalTrackerOutDto;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.util.List;
import java.util.Map;

public class MonthlyActivityContainerHolder
        extends GenericRecyclerViewAdapter.ViewHolder<Map.Entry<String, List<GoalTrackerOutDto>>> {

    final private RecyclerView recyclerView;
    private MonthlyActivityContainer monthlyActivityContainer;
    private final GenericRecyclerViewAdapter.OnItemClickListener<GoalTrackerOutDto> clickListener;

    public MonthlyActivityContainerHolder(View itemView,
                                          GenericRecyclerViewAdapter.OnItemClickListener<GoalTrackerOutDto> clickListener) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.recycler_view);
        this.clickListener = clickListener;
    }

    @Override
    public void bindWith(Map.Entry<String, List<GoalTrackerOutDto>> item) {
        monthlyActivityContainer = new MonthlyActivityContainer(itemView.getContext(), item.getKey(), clickListener);
        monthlyActivityContainer.setActivities(item.getValue());
        recyclerView.setAdapter(createMonthlyActivityAdapter());
    }

    private GenericRecyclerViewAdapter createMonthlyActivityAdapter() {
        return new GenericRecyclerViewAdapter<>(itemView.getContext(),
                monthlyActivityContainer,
                R.layout.view_ar_history_month_container,
                new GenericTitledListContainerWithAdapter.Factory<>());
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<Map.Entry<String, List<GoalTrackerOutDto>>, MonthlyActivityContainerHolder> {
        private final GenericRecyclerViewAdapter.OnItemClickListener<GoalTrackerOutDto> clickListener;

        public Factory(GenericRecyclerViewAdapter.OnItemClickListener<GoalTrackerOutDto> clickListener){
            this.clickListener = clickListener;
        }

        @Override
        public MonthlyActivityContainerHolder createViewHolder(View itemView) {
            return new MonthlyActivityContainerHolder(itemView, clickListener);
        }
    }
}
