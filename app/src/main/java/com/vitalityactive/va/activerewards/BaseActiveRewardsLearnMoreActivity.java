package com.vitalityactive.va.activerewards;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.NavigationCoordinator;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.TitleSubtitleAndIcon;
import com.vitalityactive.va.activerewards.menu.ActiveRewardsMenuContainerViewHolder;
import com.vitalityactive.va.activerewards.viewholder.ActiveRewardsLearnMoreContainerViewHolder;
import com.vitalityactive.va.menu.MenuItem;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class BaseActiveRewardsLearnMoreActivity extends BaseActivity implements GenericRecyclerViewAdapter.OnItemClickListener<MenuItem> {
    @Inject
    NavigationCoordinator navigationCoordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_active_rewards_learn_more);
        setUpActionBarWithTitle(R.string.learn_more_button_title_104)
                .setDisplayHomeAsUpEnabled(true);

        getDependencyInjector().inject(this);

        RecyclerView recyclerView = findViewById(R.id.main_recyclerview);
        recyclerView.setAdapter(createAdapter());
        ViewUtilities.scrollToTop(recyclerView);
    }

    private ContainersRecyclerViewAdapter createAdapter() {
        return new ContainersRecyclerViewAdapter(createChildAdapters());
    }

    private ArrayList<GenericRecyclerViewAdapter> createChildAdapters() {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();
        adapters.add(createLearnMoreAdapter());
        adapters.add(createMenuAdapter());
        return adapters;
    }

    private GenericRecyclerViewAdapter createLearnMoreAdapter() {
        return new GenericRecyclerViewAdapter<>(this, createListData(), R.layout.active_rewards_learn_more_container_item, new ActiveRewardsLearnMoreContainerViewHolder.Factory());
    }

    private GenericRecyclerViewAdapter createMenuAdapter() {
        return new GenericRecyclerViewAdapter<>(this, createMenuData(), R.layout.menu_container, new ActiveRewardsMenuContainerViewHolder.Factory(this));
    }

    protected List<List<MenuItem>> createMenuData() {
        return Collections.singletonList(Collections.singletonList(
                new MenuItem(MenuItemType.Activity, R.drawable.benefit_guides, R.string.AR_learn_more_benefit_guide_title_728)
        ));
    }

    private List<List<TitleSubtitleAndIcon>> createListData() {
        return Collections.singletonList(Arrays.asList(
                new TitleSubtitleAndIcon(getString(R.string.AR_learn_more_active_weekly_points_target_title_661), getString(R.string.AR_learn_more_active_weekly_points_target_content_712), R.drawable.learnmore_activate),
                new TitleSubtitleAndIcon(getString(R.string.AR_learn_more_work_out_reach_your_target_title_729), getString(R.string.AR_learn_more_work_out_reach_your_target_content_680), R.drawable.learnmore_workout),
                new TitleSubtitleAndIcon(getString(R.string.AR_learn_more_claim_reward_title_662), getString(R.string.AR_learn_more_claim_reward_content_713), R.drawable.learnmore_claim)
        ));
    }

    @Override
    public void onClicked(int position, MenuItem item) {
        navigationCoordinator.navigateToActiveRewardsBenefitGuide(this);
    }
}
