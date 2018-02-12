package com.vitalityactive.va.activerewards.history.uicontainers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.GoalTrackerOutDto;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

public class MonthlyActivityContainer extends GenericTitledListContainerWithAdapter.TitledListWithAdapter {
    private GenericRecyclerViewAdapter<GoalTrackerOutDto, ActivityItemViewHolder> adapter;

    public MonthlyActivityContainer(Context context,
                                    String title,
                                    GenericRecyclerViewAdapter.OnItemClickListener<GoalTrackerOutDto> clickListener) {
        super(title);
        setShouldAddDividers(true);
        int dividerMarginLeft = 16;
        setDividersLeftPaddingPx(ViewUtilities.pxFromDp(dividerMarginLeft));
        this.adapter = getContentAdapter(context, clickListener);
    }

    @Override
    public RecyclerView.Adapter buildAdapter(Context context) {
        return adapter;
    }

    public void setActivities(List<GoalTrackerOutDto> activityItems) {
        adapter.replaceData(activityItems);
    }

    public List<GoalTrackerOutDto> getActivityItems() {
        return adapter.getData();
    }

    public boolean isEmpty() {
        return getActivityItems().isEmpty();
    }

    private GenericRecyclerViewAdapter<GoalTrackerOutDto, ActivityItemViewHolder> getContentAdapter(Context context,
                                                                                                    GenericRecyclerViewAdapter.OnItemClickListener<GoalTrackerOutDto> clickedListener) {
        return new GenericRecyclerViewAdapter<>(context,
                new ArrayList<GoalTrackerOutDto>(),
                R.layout.view_ar_history_list_item,
                new ActivityItemViewHolder.Factory(),
                clickedListener);
    }
}
