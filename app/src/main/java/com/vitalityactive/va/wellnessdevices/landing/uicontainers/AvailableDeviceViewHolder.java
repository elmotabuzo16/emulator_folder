package com.vitalityactive.va.wellnessdevices.landing.uicontainers;

import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;

public class AvailableDeviceViewHolder extends GenericRecyclerViewAdapter.ViewHolder<PartnerDto> {
    private final TextView title;

    public AvailableDeviceViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
    }

    @Override
    public void bindWith(PartnerDto partner) {
        title.setText(partner.getDevice());
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<PartnerDto, AvailableDeviceViewHolder> {
        @Override
        public AvailableDeviceViewHolder createViewHolder(View itemView) {
            return new AvailableDeviceViewHolder(itemView);
        }
    }
}