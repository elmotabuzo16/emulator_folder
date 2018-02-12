package com.vitalityactive.va.wellnessdevices.pointsmonitor.containers;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.wellnessdevices.WdEventType;

public class MeasurementDescriptionContainer extends GenericRecyclerViewAdapter.ViewHolder<WdEventType> {
    private final ImageView ivIcon;
    private final TextView tvHeader;
    private final TextView tvDescription;

    private MeasurementDescriptionContainer(View itemView) {
        super(itemView);
        ivIcon = itemView.findViewById(R.id.iv_wd_points);
        tvHeader = itemView.findViewById(R.id.tv_wd_points_header);
        tvDescription = itemView.findViewById(R.id.tv_condition_text);
    }

    @Override
    public void bindWith(WdEventType eventType) {
        ivIcon.setImageDrawable(getTintedDrawable(eventType.getDrawableIconSmall(), R.color.wellnessdevices_blue));
        tvHeader.setText(eventType.getHeader());
        tvDescription.setText(itemView.getResources().getString(eventType.getDescription(), (Object)null));
    }

    private Drawable getTintedDrawable(@DrawableRes int drawableId, int colorId) {
        int color = ResourcesCompat.getColor(itemView.getContext().getResources(), colorId, itemView.getContext().getTheme());
        return ViewUtilities.tintDrawable(ContextCompat.getDrawable(itemView.getContext(), drawableId), color);
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<WdEventType, MeasurementDescriptionContainer> {

        public Factory() {
        }

        @Override
        public MeasurementDescriptionContainer createViewHolder(View itemView) {
            return new MeasurementDescriptionContainer(itemView);
        }
    }

}
