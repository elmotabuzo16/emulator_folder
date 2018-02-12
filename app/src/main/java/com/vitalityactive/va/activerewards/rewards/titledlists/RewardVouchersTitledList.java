package com.vitalityactive.va.activerewards.rewards.titledlists;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.CardMarginSettings;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.activerewards.rewards.viewholder.RewardVoucherViewHolder;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class RewardVouchersTitledList extends GenericTitledListContainerWithAdapter.TitledListWithAdapter {
    private GenericRecyclerViewAdapter<RewardVoucherDTO, RewardVoucherViewHolder> adapter;

    public RewardVouchersTitledList(Context context, String title, CardMarginSettings settings, RewardVoucherViewHolder.OnRewardVoucherClickedListener clickedListener) {
        super(title, settings);
        adapter = new GenericRecyclerViewAdapter<>(context,
                new ArrayList<RewardVoucherDTO>(),
                R.layout.active_rewards_reward_voucher_list_item,
                new RewardVoucherViewHolder.Factory(clickedListener));
    }

    @Override
    public RecyclerView.Adapter buildAdapter(Context context) {
        return adapter;
    }

    public void setData(List<RewardVoucherDTO> data) {
        adapter.replaceData(data);
    }

}
