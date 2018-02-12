package com.vitalityactive.va.vhc.viewholder;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.ProductFeatureType;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.vhc.dto.HealthAttributeGroupDTO;

import java.util.Locale;

import static com.vitalityactive.va.utilities.ViewUtilities.setResourceOfView;

public class InCompletedMeasurementItemViewHolder extends GenericRecyclerViewAdapter.ViewHolder<HealthAttributeGroupDTO> {
    private final TextView title;
    private final TextView pointsStatusText;
    private final ImageView icon;
    private final ImageView pointsStatusIcon;

    private InCompletedMeasurementItemViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        pointsStatusText = (TextView) itemView.findViewById(R.id.points_status_text);
        icon = (ImageView) itemView.findViewById(R.id.icon);
        pointsStatusIcon = (ImageView) itemView.findViewById(R.id.points_status_icon);
    }

    @Override
    public void bindWith(HealthAttributeGroupDTO dataItem) {
        title.setText(dataItem.getDescription());
        setResourceOfView(icon, getDisabledIcon(dataItem.getFeatureTypeKey()));
        icon.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.light_disabled_38));
        setResourceOfView(pointsStatusIcon, R.drawable.vhc_points_pending);
        pointsStatusIcon.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.light_disabled_38));

        if (dataItem.getMaxPotentialPoints() == 0)
            return;

        pointsStatusText.setText(buildPotentialPointsString(dataItem.getMaxPotentialPoints()));
        pointsStatusText.setVisibility(View.VISIBLE);
        pointsStatusIcon.setVisibility(View.VISIBLE);
    }

    private String buildPotentialPointsString(int potentialPoints) {
        return String.format(Locale.getDefault(),
                itemView.getContext().getString(R.string.home_card_card_earn_up_to_points_message_124),
                String.valueOf(potentialPoints));
    }

    private int getDisabledIcon(int featureType) {
        switch (featureType) {
            case ProductFeatureType._VHCBMI:
                return R.drawable.health_measure_bmi;
            case ProductFeatureType._VHCWAISTCIRCUM:
                return R.drawable.health_measure_waist_circumference;
            case ProductFeatureType._VHCBLOODGLUCOSE:
                return R.drawable.health_measure_bloodglucose;
            case ProductFeatureType._VHCBLOODPRESSURE:
                return R.drawable.health_measure_bloodpressure;
            case ProductFeatureType._VHCCHOLESTEROL:
            case ProductFeatureType._OTHERBLOODLIPID:
                return R.drawable.health_measure_cholesterol;
            case ProductFeatureType._VHCHBA1C:
                return R.drawable.health_measure_hba_1_c;
            case ProductFeatureType._VHCURINARYPROTEIN:
                return R.drawable.health_measure_urine;
            default:
                return 0;
        }
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<HealthAttributeGroupDTO, InCompletedMeasurementItemViewHolder> {
        @Override
        public InCompletedMeasurementItemViewHolder createViewHolder(View itemView) {
            return new InCompletedMeasurementItemViewHolder(itemView);
        }
    }
}
