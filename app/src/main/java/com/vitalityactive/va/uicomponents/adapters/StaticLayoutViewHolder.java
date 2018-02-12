package com.vitalityactive.va.uicomponents.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

public class StaticLayoutViewHolder extends GenericRecyclerViewAdapter.ViewHolder<Void> {
    @Override
    public void bindWith(Void dataItem) {
    }

    private StaticLayoutViewHolder(View itemView) {
        super(itemView);
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<Void, StaticLayoutViewHolder> {
        @Override
        public StaticLayoutViewHolder createViewHolder(View itemView) {
            return new StaticLayoutViewHolder(itemView);
        }
    }

    @NonNull
    public static GenericRecyclerViewAdapter<Void, StaticLayoutViewHolder> buildAdapter(Context context, int layout) {
        return new GenericRecyclerViewAdapter<>(context, (Void) null, layout, new StaticLayoutViewHolder.Factory());
    }
}
