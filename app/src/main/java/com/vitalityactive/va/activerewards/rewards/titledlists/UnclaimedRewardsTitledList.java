package com.vitalityactive.va.activerewards.rewards.titledlists;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.CardMarginSettings;
import com.vitalityactive.va.activerewards.rewards.dto.UnclaimedRewardDTO;
import com.vitalityactive.va.activerewards.rewards.viewholder.UnclaimedRewardViewHolder;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class UnclaimedRewardsTitledList extends GenericTitledListContainerWithAdapter.TitledListWithAdapter {
    private GenericRecyclerViewAdapter<UnclaimedRewardDTO, UnclaimedRewardViewHolder> adapter;

    public UnclaimedRewardsTitledList(Context context, String title, boolean rewardChoice, CardMarginSettings settings, UnclaimedRewardViewHolder.OnUnclaimedRewardClickedListener clickedListener) {
        super(title, settings);
        adapter = new GenericRecyclerViewAdapter<>(context,
                new ArrayList<UnclaimedRewardDTO>(),
                R.layout.active_rewards_unclaimed_reward_list_item,
                new UnclaimedRewardViewHolder.Factory(clickedListener, rewardChoice));
    }

    @Override
    public RecyclerView.Adapter buildAdapter(Context context) {
        return adapter;
    }

    public void setData(List<UnclaimedRewardDTO> data) {
        adapter.replaceData(data);
    }

}
