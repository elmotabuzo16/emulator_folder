package com.vitalityactive.va.uicomponents.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.vitalityactive.va.help.HelpAdapter;

import java.util.Map;

/**
 * A container adapter for RecyclerViewAdapters that have multiple items. We assume a view type only
 * occurs in sequence, and not more than once in the list (each adapter is bound to its view type).
 */
public class CompositeRecyclerViewAdapter extends RecyclerView.Adapter<GenericRecyclerViewAdapter.ViewHolder> {
    private final Map<Integer, GenericRecyclerViewAdapter> adapters;
    private final int[] viewTypes;
    private final int[] offsetInAdapter;

    public CompositeRecyclerViewAdapter(Map<Integer, GenericRecyclerViewAdapter> adapters, int[] viewTypes) {
        this.adapters = adapters;
        this.viewTypes = viewTypes;
        offsetInAdapter = new int[viewTypes.length];
        int offset = 0;
        for (int position = 0; position < offsetInAdapter.length; ++position) {
            offsetInAdapter[position] = offset;
            if (++offset == adapters.get(viewTypes[position]).getItemCount()) {
                offset = 0;
            }
        }
    }

    @Override
    public GenericRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return adapters.get(viewType).onCreateViewHolder(parent, viewType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(GenericRecyclerViewAdapter.ViewHolder holder, int position) {
        GenericRecyclerViewAdapter adapter = adapters.get(viewTypes[position]);
        adapter.onBindViewHolder(holder, offsetInAdapter[position]);
    }

    @Override
    public int getItemViewType(int position) {
        return adapters.get(viewTypes[position]).getViewResourceId();
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        for (GenericRecyclerViewAdapter adapter : adapters.values()) {
            itemCount += adapter.getItemCount();
        }
        return itemCount;
    }
}
