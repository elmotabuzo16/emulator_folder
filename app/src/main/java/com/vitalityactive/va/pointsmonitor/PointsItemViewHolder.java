package com.vitalityactive.va.pointsmonitor;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dto.PointsEntryDTO;

class PointsItemViewHolder extends PointsItemContentViewHolder {
    private final ImageView icon;
    private final TextView description;
    private final TextView device;
    private final PointsMonitorIconProvider iconProvider;

    PointsItemViewHolder(View itemView, PointsMonitorIconProvider iconProvider, int globalTintColor) {
        super(itemView);
        this.iconProvider = iconProvider;
        icon = itemView.findViewById(R.id.icon);
        description = itemView.findViewById(R.id.description);
        device = itemView.findViewById(R.id.device);
    }

    @Override
    void bindWith(PointsEntryDTO dataItem) {
        super.bindWith(dataItem);
        icon.setImageResource(iconProvider.getIconResourceId(dataItem.getCategory()));
        setVisibilityAndText(device, dataItem.getDevice());
        setVisibilityAndText(description, dataItem.getDescription());
    }
}
