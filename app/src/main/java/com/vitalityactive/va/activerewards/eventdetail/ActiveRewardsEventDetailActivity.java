package com.vitalityactive.va.activerewards.eventdetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.ActivityItem;
import com.vitalityactive.va.menu.MenuBuilder;
import com.vitalityactive.va.menu.MenuContainerViewHolder;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.Collections;

public class ActiveRewardsEventDetailActivity extends BaseActivity implements MenuContainerViewHolder.OnMenuItemClickedListener {
    public static final String EXTRA_ACTIVITY_ITEM = "EXTRA_ACTIVITY_ITEM";
    private ActivityItem activityItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_rewards_event_detail);

        setUpActionBarWithTitle(R.string.AR_points_event_detail_view_title_685)
                .setDisplayHomeAsUpEnabled(true);

        activityItem = getIntent().getParcelableExtra(EXTRA_ACTIVITY_ITEM);

        final RecyclerView recyclerView = findViewById(R.id.main_recyclerview);
        recyclerView.setAdapter(createAdapter());

        ViewUtilities.addDividers(this, recyclerView);
        ViewUtilities.scrollToTop(recyclerView);
    }

    private ContainersRecyclerViewAdapter createAdapter() {
        return new ContainersRecyclerViewAdapter(createChildAdapters());
    }

    private ArrayList<GenericRecyclerViewAdapter> createChildAdapters() {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();
        adapters.add(createEventDetailAdapter());
        // TODO: 2017/09/01 Not part of drop 1
//        adapters.add(createHelpMenuAdapter());
        return adapters;
    }

    @NonNull
    private GenericRecyclerViewAdapter<ActivityItem, ActiveRewardsEventDetailViewHolder> createEventDetailAdapter() {
        return new GenericRecyclerViewAdapter<>(this,
                Collections.singletonList(activityItem),
                R.layout.activity_ar_event_detail,
                new ActiveRewardsEventDetailViewHolder.Factory(this));
    }

    private GenericRecyclerViewAdapter createHelpMenuAdapter() {
        return new MenuBuilder(this)
                .setMenuItems(MenuBuilder.MenuItemSet.Help)
                .setClickListener(this)
                .build();
    }

    @Override
    public void onClicked(MenuItemType menuItemType) {

    }
}
