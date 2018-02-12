package com.vitalityactive.va.wellnessdevices.linking.containers;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class MeasurementItemViewHolder extends GenericRecyclerViewAdapter.ViewHolder<MeasurementsContainer.MeasurementItem> {
    private final ImageView ivIcon;
    private final TextView tvTitle;

    public MeasurementItemViewHolder(View itemView) {
        super(itemView);
        ivIcon = itemView.findViewById(R.id.ivIcon);
        tvTitle = itemView.findViewById(R.id.tvTitle);
    }

    @Override
    public void bindWith(MeasurementsContainer.MeasurementItem measurementItem) {
        ivIcon.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), measurementItem.iconId));
        tvTitle.setText(measurementItem.title);
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<MeasurementsContainer.MeasurementItem, MeasurementItemViewHolder> {
        @Override
        public MeasurementItemViewHolder createViewHolder(View itemView) {
            return new MeasurementItemViewHolder(itemView);
        }
    }
}
