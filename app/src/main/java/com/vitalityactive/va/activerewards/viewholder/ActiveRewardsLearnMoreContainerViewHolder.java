package com.vitalityactive.va.activerewards.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.TitleSubtitleAndIcon;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.util.List;

public class ActiveRewardsLearnMoreContainerViewHolder extends GenericRecyclerViewAdapter.ViewHolder<List<TitleSubtitleAndIcon>> {
    private final RecyclerView recyclerView;

    public ActiveRewardsLearnMoreContainerViewHolder(View itemView) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
    }

    @Override
    public void bindWith(List<TitleSubtitleAndIcon> dataItem) {
        recyclerView.setAdapter(createListAdapter(dataItem));
    }

    private RecyclerView.Adapter createListAdapter(List<TitleSubtitleAndIcon> data) {
        return new GenericRecyclerViewAdapter<>(itemView.getContext(), data, R.layout.active_rewards_learn_more_list_item, new TitleSubtitleAndIconViewHolder.Factory());
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<List<TitleSubtitleAndIcon>, ActiveRewardsLearnMoreContainerViewHolder> {
        @Override
        public ActiveRewardsLearnMoreContainerViewHolder createViewHolder(View itemView) {
            return new ActiveRewardsLearnMoreContainerViewHolder(itemView);
        }
    }
}
