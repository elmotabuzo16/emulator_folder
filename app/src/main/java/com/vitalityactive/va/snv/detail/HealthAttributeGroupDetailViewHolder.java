package com.vitalityactive.va.snv.detail;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.UnitsOfMeasure;
import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.snv.content.SNVHealthAttributeContent;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.utilities.date.NonUserFacingDateFormatter;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;
import com.vitalityactive.va.vhc.HealthAttributeFeedbackType;
import com.vitalityactive.va.vhc.content.VHCHealthAttributeContent;
import com.vitalityactive.va.vhc.dto.HealthAttributeDTO;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

import static com.vitalityactive.va.utilities.ViewUtilities.setResourceOfView;
import static com.vitalityactive.va.utilities.ViewUtilities.setTextOfView;

class HealthAttributeGroupDetailViewHolder extends GenericRecyclerViewAdapter.ViewHolder<HealthAttributeDTO> {
    private final Context context;
    private final SNVHealthAttributeContent snvHealthAttributeContent;
    private final Date currentMembershipPeriodStart;
    private final Date currentMembershipPeriodEnd;
    private final String groupDescription;
    private TextView attributeDescriptionText;
    private TextView groupValueText;
    private TextView groupMeasurementUnitText;
    private ImageView feedbackIcon;
    private TextView feedbackText;
    private TextView feedbackSubText;
    private ImageView pointsStatusIcon;
    private TextView pointsStatusText;
    private TextView pointsStatusSubtext;
    private TextView sourceText;
    private TextView dateTestedText;
    private View insetDivider;
    private TextView sourceTitle;
    private TextView subtitleText;

    private HealthAttributeGroupDetailViewHolder(View itemView,
                                                 SNVHealthAttributeContent snvHealthAttributeContent,
                                                 Date currentMembershipPeriodStart,
                                                 Date currentMembershipPeriodEnd,
                                                 String groupDescription) {
        super(itemView);

        this.context = itemView.getContext();
        this.snvHealthAttributeContent = snvHealthAttributeContent;
        this.currentMembershipPeriodStart = currentMembershipPeriodStart;
        this.currentMembershipPeriodEnd = currentMembershipPeriodEnd;
        this.groupDescription = groupDescription;

        assignViews(itemView);
    }

    private void assignViews(View itemView) {
        attributeDescriptionText = itemView.findViewById(R.id.title_text);
        groupValueText = itemView.findViewById(R.id.value_text);
        groupMeasurementUnitText = itemView.findViewById(R.id.unit_of_measure_text);
        feedbackIcon = itemView.findViewById(R.id.feedback_icon);
        feedbackText = itemView.findViewById(R.id.feedback_text);
        feedbackSubText = itemView.findViewById(R.id.feedback_subtext);
        pointsStatusIcon = itemView.findViewById(R.id.points_status_icon);
        pointsStatusText = itemView.findViewById(R.id.points_status_text);
        pointsStatusSubtext = itemView.findViewById(R.id.points_status_subtext);
        sourceText = itemView.findViewById(R.id.source_submitted_text);
        sourceTitle = itemView.findViewById(R.id.source_title_text);
        dateTestedText = itemView.findViewById(R.id.date_tested_text);
        insetDivider = itemView.findViewById(R.id.inset_divider);
        subtitleText = itemView.findViewById(R.id.subtitle_text);
    }

    @Override
    public void bindWith(HealthAttributeDTO dataItem) {
        setUpTextViews(dataItem);

        setUpImageViews();
    }

    private void setUpImageViews() {
        setResourceOfView(pointsStatusIcon, R.drawable.vhc_points_pending_med);

        int secondary_54 = ContextCompat.getColor(context, R.color.secondary_54);
        feedbackIcon.setColorFilter(secondary_54);
        pointsStatusIcon.setColorFilter(secondary_54);
    }

    private void setUpTextViews(HealthAttributeDTO dataItem) {
        UnitsOfMeasure unitOfMeasure = UnitsOfMeasure.fromValue(dataItem.getUnitOfMeasureTypeKey());

        setupGroupTitle(dataItem);
        setTextOfView(groupValueText, dataItem.getValue());
        setTextOfView(dateTestedText, getFormattedDate(dataItem.getMeasuredOn()));

        if (unitOfMeasure != UnitsOfMeasure.UNKNOWN) {
            setTextOfView(groupMeasurementUnitText, snvHealthAttributeContent.getUnitOfMeasureSymbol(unitOfMeasure));
        }

        displayFeedbackIfAvailable(dataItem);
        displayPointsStatusIfAvailable(dataItem);
        displayPointsStatusSubtextIfAvailable(dataItem);
        displaySourceIfAvailable(dataItem);
        displaySubtitleIfAvailable(dataItem);
    }

    private void setupGroupTitle(HealthAttributeDTO dataItem) {
        String fieldName = snvHealthAttributeContent.getFieldName(dataItem.getEventTypeKey());


        if (dataItem.getEventTypeKey() != EventType._BLOODPRESSURE && !fieldName.equalsIgnoreCase(groupDescription)) {
            setTextOfView(attributeDescriptionText, fieldName);
        } else {
            attributeDescriptionText.setVisibility(View.GONE);
        }
    }

    private void displaySubtitleIfAvailable(HealthAttributeDTO dataItem) {
        String validOptionDescription = snvHealthAttributeContent.getValidOptionDescription(dataItem.getValue());
        if (!validOptionDescription.isEmpty()) {
            subtitleText.setVisibility(View.VISIBLE);
            subtitleText.setText(validOptionDescription);
        }
    }

    private String getFormattedDate(String measuredOn) {
        Date date = getDate(measuredOn);
        return DateFormattingUtilities.formatWeekdayDateMonthAbbreviatedYear(itemView.getContext(), date.getTime());
    }

    private Date getDate(String measuredOn) {
        Date date = new Date();

        if (measuredOn != null) {
            try {
                date = NonUserFacingDateFormatter.javaUtilDateFromYearMonthDayString(measuredOn);
            } catch (ParseException ignored) {
            }
        }
        return date;
    }

    private void displaySourceIfAvailable(HealthAttributeDTO dataItem) {
        if (Objects.equals(dataItem.getSourceString(), "") || dataItem.getSourceString() == null)
            return;

        setTextOfView(sourceText, dataItem.getSourceString());

        sourceText.setVisibility(View.VISIBLE);
        sourceTitle.setVisibility(View.VISIBLE);
        insetDivider.setVisibility(View.VISIBLE);

    }

    private void displayPointsStatusSubtextIfAvailable(HealthAttributeDTO dataItem) {
        if (dataItem.getPointsStatus().isEmpty()) {
            return;
        }

        setTextOfView(this.pointsStatusSubtext, dataItem.getPointsStatus());
        this.pointsStatusSubtext.setVisibility(View.VISIBLE);
    }

    private void displayPointsStatusIfAvailable(HealthAttributeDTO dataItem) {
        String statusText = "";

        final int totalPotentialPoints = dataItem.getPotentialPoints();
        final int totalEarnedPoints = dataItem.getEarnedPoints();

        Date date = getDate(dataItem.getMeasuredOn());

        boolean dateInPeriod = TimeUtilities.isDateInPeriod(date,
                currentMembershipPeriodStart,
                currentMembershipPeriodEnd);

        if (dataItem.hasReading() && totalEarnedPoints == 0 && dateInPeriod) {
            statusText = context.getString(R.string.points_no_points_earned_title_194);
        } else if (totalEarnedPoints > 0 && dateInPeriod) {
            statusText = String.format(context.getString(R.string.points_x_of_y_points_title_193),
                    totalEarnedPoints,
                    totalPotentialPoints);
        } else if (totalPotentialPoints > 0) {
            statusText = String.format(context.getString(R.string.home_card_card_earn_up_to_points_message_124), totalPotentialPoints);
        }

        if (!statusText.isEmpty()) {
            setTextOfView(pointsStatusText, statusText);
            pointsStatusText.setVisibility(View.VISIBLE);
            pointsStatusIcon.setVisibility(View.VISIBLE);
        }
    }

    private void displayFeedbackIfAvailable(HealthAttributeDTO dataItem) {
        Integer feedbackTypeKey = dataItem.getFeedbackTypeKey();
        if (feedbackTypeKey == null)
            return;

        HealthAttributeFeedbackType healthAttributeFeedbackType = HealthAttributeFeedbackType.fromValue(dataItem.getFeedbackTypeKey());

        final String inHealthyRangeStringFromEventType =
                healthAttributeFeedbackType.inHealthyRange()
                        ? snvHealthAttributeContent.getInHealthyRangeText()
                        : snvHealthAttributeContent.getOutOfHealthyRangeText();

        if (dataItem.getFeedbackTypeName() != null) {
            setTextOfView(feedbackSubText, dataItem.getFeedbackTypeName());
            setTextOfView(feedbackText, inHealthyRangeStringFromEventType);
            setResourceOfView(feedbackIcon, healthAttributeFeedbackType.inHealthyRange() ?
                    R.drawable.vhc_healthy_range_med : R.drawable.out_of_healthy_range_med);

            feedbackText.setVisibility(View.VISIBLE);
            feedbackIcon.setVisibility(View.VISIBLE);
            feedbackSubText.setVisibility(View.VISIBLE);
        }
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<HealthAttributeDTO,
            HealthAttributeGroupDetailViewHolder> {

        private final SNVHealthAttributeContent snvHealthAttributeContent;
        private final Date currentMembershipPeriodStart;
        private final Date currentMembershipPeriodEnd;
        private final String groupDescription;

        public Factory(SNVHealthAttributeContent snvHealthAttributeContent,
                       Date currentMembershipPeriodStart,
                       Date currentMembershipPeriodEnd,
                       String groupDescription) {
            this.snvHealthAttributeContent = snvHealthAttributeContent;
            this.currentMembershipPeriodStart = currentMembershipPeriodStart;
            this.currentMembershipPeriodEnd = currentMembershipPeriodEnd;
            this.groupDescription = groupDescription;
        }

        @Override
        public HealthAttributeGroupDetailViewHolder createViewHolder(View itemView) {
            return new HealthAttributeGroupDetailViewHolder(itemView,
                    snvHealthAttributeContent,
                    currentMembershipPeriodStart,
                    currentMembershipPeriodEnd,
                    groupDescription);
        }
    }
}
