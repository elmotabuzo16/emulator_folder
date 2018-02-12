package com.vitalityactive.va.partnerjourney.containers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.partnerjourney.PartnerListPresenterImpl;
import com.vitalityactive.va.partnerjourney.models.PartnerGroup;
import com.vitalityactive.va.partnerjourney.models.PartnerItem;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.List;

public class PartnerContainerViewHolder extends GenericRecyclerViewAdapter.ViewHolder<PartnerGroup> {
    private final PartnerListPresenterImpl presenter;

    public PartnerContainerViewHolder(View itemView, PartnerListPresenterImpl presenter) {
        super(itemView);
        this.presenter = presenter;
    }

    @Override
    public void bindWith(PartnerGroup dataItem) {
        bindName(dataItem);
        bindItems(dataItem);
    }

    private void bindName(PartnerGroup dataItem) {
        TextView groupName = itemView.findViewById(R.id.health_service_group_name);
        groupName.setText(dataItem.name);
        groupName.setVisibility(TextUtilities.isNullOrEmpty(dataItem.name) ? View.GONE : View.VISIBLE);
    }

    private void bindItems(PartnerGroup dataItem) {
        RecyclerView recyclerView = itemView.findViewById(R.id.health_service_items);
        recyclerView.setAdapter(getHealthServiceItemsAdapter(dataItem.items));
        ViewUtilities.addDividers(itemView.getContext(), recyclerView, ViewUtilities.pxFromDp(112));
    }

    @NonNull
    private GenericRecyclerViewAdapter<PartnerItem, PartnerViewHolder> getHealthServiceItemsAdapter(List<PartnerItem> items) {
        return new GenericRecyclerViewAdapter<>(itemView.getContext(),
                items,
                R.layout.partnerjourney_health_service_item,
                new PartnerViewHolder.Factory(presenter),
                new GenericRecyclerViewAdapter.OnItemClickListener<PartnerItem>() {
                    @Override
                    public void onClicked(int position, PartnerItem item) {
                        presenter.onHealthServiceItemClicked(item);
                    }
                });
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<PartnerGroup, PartnerContainerViewHolder> {
        private final PartnerListPresenterImpl presenter;

        public Factory(PartnerListPresenterImpl presenter) {
            this.presenter = presenter;
        }

        @Override
        public PartnerContainerViewHolder createViewHolder(View itemView) {
            return new PartnerContainerViewHolder(itemView, presenter);
        }
    }
}
