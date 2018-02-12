package com.vitalityactive.va.uicomponents.learnmore;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.util.List;

public class LearnMoreContainerViewHolder extends GenericRecyclerViewAdapter.ViewHolder<List<LearnMoreItem>> {
    private final RecyclerView recyclerView;

    public LearnMoreContainerViewHolder(View itemView) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
    }

    @Override
    public void bindWith(List<LearnMoreItem> dataItem) {
        recyclerView.setAdapter(createListAdapter(dataItem));
    }

    private RecyclerView.Adapter createListAdapter(List<LearnMoreItem> data) {
        return new GenericRecyclerViewAdapter<>(itemView.getContext(),
                data,
                R.layout.view_learn_more_list_item,
                new LearnMoreItemViewHolder.Factory());
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<List<LearnMoreItem>, LearnMoreContainerViewHolder> {
        @Override
        public LearnMoreContainerViewHolder createViewHolder(View itemView) {
            return new LearnMoreContainerViewHolder(itemView);
        }
    }
}
