package com.vitalityactive.va.activerewards.rewards.titledlists;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.CardMarginSettings;
import com.vitalityactive.va.activerewards.dto.TitleAndSubtitle;
import com.vitalityactive.va.activerewards.viewholder.TitleAndSubtitleViewHolder;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class NoRewardAvailableTitledList extends GenericTitledListContainerWithAdapter.TitledListWithAdapter {

    private final String subtitle;

    public NoRewardAvailableTitledList(String title, String subtitle, CardMarginSettings settings) {
        super(title, settings);
        this.subtitle = subtitle;
    }

    @Override
    public RecyclerView.Adapter buildAdapter(Context context) {
        return new GenericRecyclerViewAdapter<>(context,
                new TitleAndSubtitle(subtitle, context.getString(R.string.AR_rewards_earned_cell_description_717)),
                R.layout.active_rewards_none_available,
                new TitleAndSubtitleViewHolder.Factory());
    }

}
