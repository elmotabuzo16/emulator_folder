package com.vitalityactive.va.activerewards.history.uicontainers;

import android.content.Context;
import android.view.ViewGroup;

import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.util.List;

public class PagingFooterViewAdapter <DataType, VH extends GenericRecyclerViewAdapter.ViewHolder<DataType>>
        extends GenericRecyclerViewAdapter<DataType, VH>{
    private VH container;

    public PagingFooterViewAdapter(Context context, List<DataType> data, int viewResourceId, IViewHolderFactory<DataType, VH> viewHolderFactory, OnItemClickListener<DataType> clickListener) {
        super(context, data, viewResourceId, viewHolderFactory, clickListener);
    }

//    public PagingFooterViewAdapter(Context context, List<DataType> data, int viewResourceId, IViewHolderFactory<DataType, VH> viewHolderFactory, OnItemClickListener<DataType> clickListener, OnTaggedMenuItemClickListener<DataType> taggedMenuItemClickListener) {
//        super(context, data, viewResourceId, viewHolderFactory, clickListener, taggedMenuItemClickListener);
//    }

    public PagingFooterViewAdapter(Context context, List<DataType> data, int viewResourceId, IViewHolderFactory<DataType, VH> viewHolderFactory) {
        super(context, data, viewResourceId, viewHolderFactory);
    }

    protected PagingFooterViewAdapter(List<DataType> data, int viewResourceId) {
        super(data, viewResourceId);
    }

    public PagingFooterViewAdapter(Context context, DataType data, int viewResourceId, IViewHolderFactory<DataType, VH> viewHolderFactory) {
        super(context, data, viewResourceId, viewHolderFactory);
    }

    public PagingFooterViewAdapter(Context context, DataType data, int viewResourceId, IViewHolderFactory<DataType, VH> viewHolderFactory, OnItemClickListener<DataType> clickListener) {
        super(context, data, viewResourceId, viewHolderFactory, clickListener);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        container = super.onCreateViewHolder(viewGroup, viewType);
        return container;
    }

    public VH getContainer() {
        return container;
    }
}