package com.vitalityactive.va.activerewards.history.detailedscreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.ActivityItem;
import com.vitalityactive.va.activerewards.landing.uicontainers.ActiveRewardsActivityItemViewHolder;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActivityListContainer extends GenericTitledListContainerWithAdapter.TitledListWithAdapter {
    private GenericRecyclerViewAdapter<ActivityItem, ActiveRewardsActivityItemViewHolder> adapter;

    public ActivityListContainer(Context context,
                                 String title,
                                 GenericRecyclerViewAdapter.OnItemClickListener<ActivityItem> clickListener) {
        super(title);
        this.adapter = getContentAdapter(context, clickListener);
        addDividers = true;
    }

    @Override
    public RecyclerView.Adapter buildAdapter(Context context) {
        return adapter;
    }

    public void setActivityList(List<ActivityItem> activityItems) {
        adapter.replaceData(activityItems);
    }

    public List<ActivityItem> getActivityItems() {
        return adapter.getData();
    }

    public boolean isEmpty() {
        return getActivityItems().isEmpty();
    }

    private static GenericRecyclerViewAdapter<ActivityItem, ActiveRewardsActivityItemViewHolder> getContentAdapter(Context context,
                                                                                                                   GenericRecyclerViewAdapter.OnItemClickListener<ActivityItem> clickedListener) {
        return new GenericRecyclerViewAdapter<>(context,
                new ArrayList<ActivityItem>(),
                R.layout.active_rewards_activity_item,
                new ActiveRewardsActivityItemViewHolder.Factory(),
                clickedListener);
    }

}
