package com.vitalityactive.va.wellnessdevices.landing.uicontainers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;

import java.util.ArrayList;
import java.util.List;

public class LinkedDevicesContainer extends GenericTitledListContainerWithAdapter.TitledListWithAdapter {
    private GenericRecyclerViewAdapter<PartnerDto, LinkedDeviceViewHolder> adapter;

    public LinkedDevicesContainer(Context context,
                                  GenericRecyclerViewAdapter.OnItemClickListener<PartnerDto> clickListener
    ) {
        super(context.getString(R.string.WDA_landing_linked_section_title_472));
        this.adapter = getContentAdapter(context, clickListener);
    }

    @Override
    public RecyclerView.Adapter buildAdapter(Context context) {
        return adapter;
    }

    public void setDevices(List<PartnerDto> partners) {
        adapter.replaceData(partners);
    }

    public List<PartnerDto> getDevices() {
       return adapter.getData();
    }

    public boolean isEmpty(){
        return getDevices().isEmpty();
    }

    private static GenericRecyclerViewAdapter<PartnerDto, LinkedDeviceViewHolder> getContentAdapter(Context context,
                                  GenericRecyclerViewAdapter.OnItemClickListener<PartnerDto> clickedListener) {
        return new GenericRecyclerViewAdapter<>(context,
                new ArrayList<PartnerDto>(),
                R.layout.view_wd_linked_device_list_item,
                new LinkedDeviceViewHolder.Factory(),
                clickedListener);
    }
}
