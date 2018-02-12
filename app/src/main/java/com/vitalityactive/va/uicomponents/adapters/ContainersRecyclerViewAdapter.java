package com.vitalityactive.va.uicomponents.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A container adapter for RecyclerViewAdapters that have single items
 */
public class ContainersRecyclerViewAdapter extends RecyclerView.Adapter<GenericRecyclerViewAdapter.ViewHolder> {
    private final HashMap<Integer, GenericRecyclerViewAdapter> viewHolderFactoryMap;
    private final List<GenericRecyclerViewAdapter> adapters;
    private final ArrayList<Boolean> visible = new ArrayList<>();

    public ContainersRecyclerViewAdapter(List<GenericRecyclerViewAdapter> adapters) {
        this.adapters = adapters;
        viewHolderFactoryMap = new HashMap<>();
        for (GenericRecyclerViewAdapter adapter : adapters) {
            // We make the assumption here that a view type will always bind to the same view holder type
            viewHolderFactoryMap.put(adapter.getViewResourceId(), adapter);
            visible.add(true);
        }
    }

    @Override
    public GenericRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewHolderFactoryMap.get(viewType).onCreateViewHolder(parent, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(GenericRecyclerViewAdapter.ViewHolder holder, int position) {
        GenericRecyclerViewAdapter visibleAdapterAtPosition = getVisibleAdapterAtPosition(position);
        if (visibleAdapterAtPosition == null) {
            return;
        }
        visibleAdapterAtPosition.onBindViewHolder(holder, 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (adapters.isEmpty()) {
            return 0;
        }
        GenericRecyclerViewAdapter visibleAdapterAtPosition = getVisibleAdapterAtPosition(position);
        return visibleAdapterAtPosition == null ? adapters.get(0).getViewResourceId() : visibleAdapterAtPosition.getViewResourceId();
    }

    private GenericRecyclerViewAdapter getVisibleAdapterAtPosition(int position) {
        int index = position;
        for (int i = 0; i <= position; i++) {
            if (!visible.get(i)) {
                index++;
            }
        }
        return index < adapters.size() ? adapters.get(index) : null;
    }

    @Override
    public int getItemCount() {
        int visibleCount = 0;
        for (Boolean visible : this.visible) {
            if (visible) {
                visibleCount++;
            }
        }
        return visibleCount;
    }

    public void hideContainer(int position) {
        showContainer(position, false);
    }

    public void showContainer(int position, boolean visible) {
        this.visible.remove(position);
        this.visible.add(position, visible);
    }

    public void addOrReplaceAdapter(GenericRecyclerViewAdapter adapter, int position) {
        if (adapters.get(position).getViewResourceId() == adapter.getViewResourceId()) {
            adapters.remove(position);
            adapters.add(position, adapter);
            notifyItemChanged(position);
        } else {
            visible.add(position, true);
            adapters.add(position, adapter);
            viewHolderFactoryMap.put(adapter.getViewResourceId(), adapter);
            notifyItemInserted(position);
        }
    }
}
