package com.vitalityactive.va.activerewards.menu;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.vitalityactive.va.R;
import com.vitalityactive.va.menu.MenuItem;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.util.List;

public class ActiveRewardsMenuContainerViewHolder extends GenericRecyclerViewAdapter.ViewHolder<List<MenuItem>> {
    private final RecyclerView recyclerView;
    private final View itemView;
    private final GenericRecyclerViewAdapter.OnItemClickListener<MenuItem> onItemClickListener;

    public ActiveRewardsMenuContainerViewHolder(View itemView, GenericRecyclerViewAdapter.OnItemClickListener<MenuItem> onItemClickListener) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
        this.itemView = itemView;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void bindWith(List<MenuItem> dataItem) {
        recyclerView.setAdapter(createMenuAdapter(dataItem));
        removeBottomMarginFromContainer();
    }

    private void removeBottomMarginFromContainer() {
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = 0;
            itemView.setLayoutParams(layoutParams);
        }
    }

    private RecyclerView.Adapter createMenuAdapter(List<MenuItem> data) {
        return new GenericRecyclerViewAdapter<>(itemView.getContext(), data, R.layout.menu_item_small_text, new ActiveRewardsMenuItemViewHolder.Factory(), onItemClickListener);
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<List<MenuItem>, ActiveRewardsMenuContainerViewHolder> {
        private final GenericRecyclerViewAdapter.OnItemClickListener<MenuItem> onItemClickListener;

        public Factory(GenericRecyclerViewAdapter.OnItemClickListener<MenuItem> onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public ActiveRewardsMenuContainerViewHolder createViewHolder(View itemView) {
            return new ActiveRewardsMenuContainerViewHolder(itemView, onItemClickListener);
        }
    }
}
