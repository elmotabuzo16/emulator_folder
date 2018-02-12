package com.vitalityactive.va.vhc.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.ProductFeatureType;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.vhc.dto.HealthAttributeGroupDTO;

import java.util.Date;
import java.util.Locale;

import static com.vitalityactive.va.utilities.ViewUtilities.setTextOfView;

public class CompletedMeasurementItemViewHolder
        extends GenericRecyclerViewAdapter.ViewHolder<HealthAttributeGroupDTO> {
    private final Date currentMembershipPeriodStart;
    private final Date currentMembershipPeriodEnd;
    private TextView title;
    private TextView feedbackText;
    private ImageView feedbackIcon;
    private TextView pointsStatusText;
    private ImageView pointsStatusIcon;
    private TextView pointsStatusTipText;
    private ImageView icon;
    private int primaryColor;
    private int secondary_54;
    private Context context;

    private CompletedMeasurementItemViewHolder(View itemView,
                                               Date currentMembershipPeriodStart,
                                               Date currentMembershipPeriodEnd) {
        super(itemView);

        context = itemView.getContext();
        this.currentMembershipPeriodStart = currentMembershipPeriodStart;
        this.currentMembershipPeriodEnd = currentMembershipPeriodEnd;
    }

    @Override
    public void bindWith(HealthAttributeGroupDTO dataItem) {
        getColors();
        assignViews();
        setupTextViews(dataItem);
        setupImageViews(dataItem);
    }

    private void getColors() {
        primaryColor = ViewUtilities.getColorPrimaryFromTheme(itemView);
        secondary_54 = ContextCompat.getColor(context, R.color.secondary_54);
    }

    private void setupImageViews(HealthAttributeGroupDTO dataItem) {
        if (dataItem.groupHasFeedback()) {
            setResourceOfView(feedbackIcon, dataItem.inHealthyRange() ?
                    R.drawable.vhc_healthy_range : R.drawable.vhc_out_of_healthy_range);
        }

        setResourceOfView(pointsStatusIcon, R.drawable.vhc_points_pending);
        setResourceOfView(icon, getIcon(dataItem.getFeatureTypeKey()));

        setViewColors();

        setFeedbackIfGroupInPeriod(dataItem);
     //   setGroupDisabledIfPartiallyCompleted(dataItem);
    }

    private void setViewColors() {
        icon.setColorFilter(primaryColor);
        pointsStatusIcon.setColorFilter(secondary_54);
        feedbackIcon.setColorFilter(secondary_54);
    }

    private void setGroupDisabledIfPartiallyCompleted(HealthAttributeGroupDTO dataItem) {
        if (dataItem.isPartiallyComplete()) {
            setViewAsDisabled();
            
            pointsStatusTipText.setVisibility(View.VISIBLE);

            setTextOfView(pointsStatusTipText,
                    context.getString(R.string.landing_screen_points_message_249));
        }
    }

    private void setFeedbackIfGroupInPeriod(HealthAttributeGroupDTO dataItem) {
        if (!TimeUtilities.isDateInPeriod(
                dataItem.getMeasuredOn(),
                currentMembershipPeriodStart,
                currentMembershipPeriodEnd)) {
            setViewAsDisabled();

            feedbackIcon.setVisibility(View.GONE);
            feedbackText.setVisibility(View.GONE);
        }
    }

    private int getIcon(int featureType) {
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

    private void setViewAsDisabled() {
        title.setTextColor(secondary_54);
        icon.setColorFilter(secondary_54);
    }

    private void setupTextViews(HealthAttributeGroupDTO dataItem) {
        setTextOfView(title, dataItem.getDescription());

        if (dataItem.getMaxPotentialPoints() == 0) {
            hidePointsStatus();
        } else {
            showPointsStatus(dataItem);
        }
    }

    private void showPointsStatus(HealthAttributeGroupDTO dataItem) {
        showFeedbackIfAvailable(dataItem);
        setPointsStatus(dataItem);
    }

    private void setPointsStatus(HealthAttributeGroupDTO dataItem) {
        boolean inPeriod = TimeUtilities.isDateInPeriod(
                dataItem.getMeasuredOn(),
                currentMembershipPeriodStart,
                currentMembershipPeriodEnd);

        if (dataItem.getTotalEarnedPoints() == 0 && inPeriod && dataItem.groupHasReadings()) {
            setTextOfView(pointsStatusText, context.getString(R.string.points_no_points_earned_title_194));
        } else if (dataItem.getTotalEarnedPoints() > 0 && inPeriod) {
            setTextOfView(pointsStatusText, getPointsAwardedString(dataItem));
        } else {
            if (dataItem.getMaxPotentialPoints() > 0) {
                setTextOfView(pointsStatusText,
                        String.format(context.getString(R.string.home_card_card_earn_up_to_points_message_124), dataItem.getMaxPotentialPoints()));
            }
        }
    }

    private void showFeedbackIfAvailable(HealthAttributeGroupDTO dataItem) {
        if (dataItem.groupHasFeedback()) {
            feedbackIcon.setVisibility(View.VISIBLE);
            feedbackText.setVisibility(View.VISIBLE);
            setTextOfView(feedbackText, dataItem.inHealthyRange()
                    ? context.getString(R.string.range_in_healthy_title_190)
                    : context.getString(R.string.range_out_of_healthy_title_191));
        }
    }

    private void hidePointsStatus() {
        pointsStatusText.setVisibility(View.GONE);
        pointsStatusTipText.setVisibility(View.GONE);
        pointsStatusIcon.setVisibility(View.GONE);
    }

    @NonNull
    private String getPointsAwardedString(HealthAttributeGroupDTO dataItem) {
        return String.format(Locale.getDefault(),
                context.getString(R.string.points_x_of_y_points_title_193),
                String.valueOf(dataItem.getTotalEarnedPoints()), dataItem.getMaxPotentialPoints());
    }

    private void setResourceOfView(ImageView icon, int resourceId) {
        icon.setImageResource(resourceId);
    }

    private void assignViews() {
        title = (TextView) itemView.findViewById(R.id.title);
        feedbackText = (TextView) itemView.findViewById(R.id.feedback_text);
        feedbackIcon = (ImageView) itemView.findViewById(R.id.feedback_icon);
        pointsStatusText = (TextView) itemView.findViewById(R.id.points_status_text);
        pointsStatusIcon = (ImageView) itemView.findViewById(R.id.points_status_icon);
        pointsStatusTipText = (TextView) itemView.findViewById(R.id.points_status_tip);
        icon = (ImageView) itemView.findViewById(R.id.icon);
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<HealthAttributeGroupDTO,
            CompletedMeasurementItemViewHolder> {
        private final Date currentMembershipPeriodStart;
        private final Date currentMembershipPeriodEnd;

        public Factory(Date currentMembershipPeriodStart, Date currentMembershipPeriodEnd) {
            this.currentMembershipPeriodStart = currentMembershipPeriodStart;
            this.currentMembershipPeriodEnd = currentMembershipPeriodEnd;
        }

        @Override
        public CompletedMeasurementItemViewHolder createViewHolder(View itemView) {
            return new CompletedMeasurementItemViewHolder(
                    itemView,
                    currentMembershipPeriodStart,
                    currentMembershipPeriodEnd);
        }
    }
}
