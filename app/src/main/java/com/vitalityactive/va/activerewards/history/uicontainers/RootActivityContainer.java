package com.vitalityactive.va.activerewards.history.uicontainers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.GoalTrackerOutDto;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RootActivityContainer extends GenericTitledListContainerWithAdapter.TitledListWithAdapter {
    private GenericRecyclerViewAdapter<Map.Entry<String, List<GoalTrackerOutDto>>, MonthlyActivityContainerHolder> adapter;

    public RootActivityContainer(Context context,
                                 GenericRecyclerViewAdapter.OnItemClickListener<GoalTrackerOutDto> clickListener) {
        super("");
        setShouldAddDividers(true);
        this.adapter = getContentAdapter(context, clickListener);
    }

    @Override
    public RecyclerView.Adapter buildAdapter(Context context) {
        return adapter;
    }

    public void setHistoryData(Map<String, List<GoalTrackerOutDto>> items) {
        List<Map.Entry<String, List<GoalTrackerOutDto>>> data = new ArrayList<>();
        for (Map.Entry<String, List<GoalTrackerOutDto>> entry : items.entrySet()) {
            data.add(entry);
        }
        adapter.replaceDataAndScrollToBottomIfNeeded(data);
    }

    private static GenericRecyclerViewAdapter<Map.Entry<String, List<GoalTrackerOutDto>>, MonthlyActivityContainerHolder>
    getContentAdapter(Context context,
                      GenericRecyclerViewAdapter.OnItemClickListener<GoalTrackerOutDto> clickListener) {
        return new GenericRecyclerViewAdapter<>(context,
                new ArrayList<Map.Entry<String, List<GoalTrackerOutDto>>>(),
                R.layout.view_ar_root_activity_history_container,
                new MonthlyActivityContainerHolder.Factory(clickListener));
    }
}
