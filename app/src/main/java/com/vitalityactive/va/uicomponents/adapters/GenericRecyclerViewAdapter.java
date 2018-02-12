package com.vitalityactive.va.uicomponents.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

public class GenericRecyclerViewAdapter<DataType, VH extends GenericRecyclerViewAdapter.ViewHolder<DataType>>
        extends RecyclerView.Adapter<VH> {
    private static final String TAG = "G-RecyclerViewAdapter";
    private final int viewResourceId;

    private LayoutInflater inflater;
    protected List<DataType> data;
    private IViewHolderFactory<DataType, VH> viewHolderFactory;
    private final OnItemClickListener<DataType> clickListener;

    public interface IViewHolderFactory<T, VH extends ViewHolder<T>> {
        VH createViewHolder(View itemView);
    }

    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder {
        public abstract void bindWith(T dataItem);

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void layoutFor(T dataItem) {
            // by default no-op
        }
    }

    public interface OnItemClickListener<DataType> {
        void onClicked(int position, DataType item);
    }


    public GenericRecyclerViewAdapter(Context context, List<DataType> data, int viewResourceId, IViewHolderFactory<DataType, VH> viewHolderFactory, OnItemClickListener<DataType> clickListener) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.viewResourceId = viewResourceId;
        this.viewHolderFactory = viewHolderFactory;
        this.clickListener = clickListener;
    }

    public GenericRecyclerViewAdapter(Context context, List<DataType> data, int viewResourceId, IViewHolderFactory<DataType, VH> viewHolderFactory) {
        this(context, data, viewResourceId, viewHolderFactory, null);
    }

    protected GenericRecyclerViewAdapter(List<DataType> data, int viewResourceId) {
        this.data = data;
        this.viewResourceId = viewResourceId;
        this.clickListener = null;
    }

    public GenericRecyclerViewAdapter(Context context, DataType data, int viewResourceId, IViewHolderFactory<DataType, VH> viewHolderFactory) {
        this(context, Collections.singletonList(data), viewResourceId, viewHolderFactory);
    }

    public GenericRecyclerViewAdapter(Context context, DataType data, int viewResourceId, IViewHolderFactory<DataType, VH> viewHolderFactory, OnItemClickListener<DataType> clickListener) {
        this(context, Collections.singletonList(data), viewResourceId, viewHolderFactory, clickListener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return viewHolderFactory.createViewHolder(createHolderTemplate(viewGroup, viewType));
    }

    protected View createHolderTemplate(ViewGroup viewGroup, int viewType){
        return inflater.inflate(viewType, viewGroup, false);
    }

    @Override
    public int getItemViewType(int position) {
        return viewResourceId;
    }

    public int getViewResourceId() {
        return viewResourceId;
    }

    @Override
    public void onBindViewHolder(final VH viewHolder, int position) {
        final int positionInAdapter = position;
        DataType dataItem = data.get(position);
        viewHolder.layoutFor(dataItem);
        viewHolder.bindWith(dataItem);
        if (clickListener == null)
            return;

        viewHolder.itemView.setOnClickListener(view -> onItemClicked(positionInAdapter));
    }

    public DataType getItem(int position) {
        return data.get(position);
    }

    public void changeItem(int i, DataType item) {
        data.set(i, item);
        notifyItemChanged(i);
    }

    public void addItem(int i, DataType entity) {
        data.add(i, entity);
        notifyItemInserted(i);
    }

    public DataType deleteItem(int i) {
        DataType item = data.remove(i);
        notifyItemRemoved(i);

        return item;
    }

    public void moveItem(int i, int loc) {
        move(data, i, loc);
        notifyItemMoved(i, loc);
    }

    private void move(List<DataType> data, int a, int b) {
        DataType temp = data.remove(a);
        data.add(b, temp);
    }

    public List<DataType> getData() {
        return data;
    }

    public void setData(final List<DataType> data) {
        // Remove all deleted items.
        for (int i = this.data.size() - 1; i >= 0; --i) {
            if (getLocation(data, this.data.get(i)) < 0) {
                deleteItem(i);
            }
        }

        // Add and move items.
        for (int i = 0; i < data.size(); ++i) {
            DataType entity = data.get(i);
            int loc = getLocation(this.data, entity);
            if (loc < 0) {
                addItem(i, entity);
            } else if (loc != i) {
                moveItem(i, loc);
            }
        }
    }

    public void replaceData(List<DataType> newList) {
        this.data.clear();
        this.data.addAll(newList);
        notifyDataSetChanged();
    }

    public void replaceDataAndScrollToBottomIfNeeded(List<DataType> newList) {
        final boolean shouldScrollToBottom = !this.data.isEmpty();
        this.data.clear();
        this.data.addAll(newList);
        if(shouldScrollToBottom) {
            int curSize = getItemCount();
            notifyItemRangeInserted(curSize, newList.size());
        } else {
            notifyDataSetChanged();
        }
    }

    private int getLocation(List<DataType> data, DataType entity) {
        for (int j = 0; j < data.size(); ++j) {
            DataType newEntity = data.get(j);
            try {
                if (entity.equals(newEntity)) {
                    return j;
                }
            } catch (Exception ignored) {
            }
        }

        return -1;
    }

    private void onItemClicked(int position) {
        Log.d(TAG, "clicked on item " + position);
        if (clickListener != null) {
            clickListener.onClicked(position, getItem(position));
        }
    }
}
