package com.vitalityactive.va.myhealth.vitalityageprofile;


import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.myhealth.content.VitalityAgeData;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

public class MyHealthVitalityAgeProfileViewHolder extends GenericRecyclerViewAdapter.ViewHolder<VitalityAgeData> {
    TextView vitalityAgeSummary;
    TextView vitalityAgeExplanation;
    TextView vitalityAgeValue;
    TextView vitalityAgeValuePlaceholder;
    ImageView vitalityAgeIcon;
    ImageView vitalityAgeBackgroundIcon;
    Context context;
    Button learnMoreButton;
    View.OnClickListener learnMoreOnClickListener;

    public MyHealthVitalityAgeProfileViewHolder(View itemView, View.OnClickListener learnMoreOnClickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.vitalityAgeExplanation = (TextView) itemView.findViewById(R.id.vitality_age_explanation);
        this.vitalityAgeIcon = (ImageView) itemView.findViewById(R.id.va_status_icon);
        this.vitalityAgeValue = (TextView) itemView.findViewById(R.id.vitality_age_value);
        this.vitalityAgeValuePlaceholder = (TextView) itemView.findViewById(R.id.vitality_age_value_placeholder);
        this.vitalityAgeBackgroundIcon = (ImageView) itemView.findViewById(R.id.vitality_age_bg);
        this.vitalityAgeSummary = (TextView) itemView.findViewById(R.id.vitality_age_summary);
        this.learnMoreOnClickListener = learnMoreOnClickListener;
        this.learnMoreButton = (Button) itemView.findViewById(R.id.vitality_age_learnmore);
    }

    @Override
    public void bindWith(final VitalityAgeData dataItem) {
        if (dataItem != null) {
            if (dataItem.itemFeedbackSummary != null) {
                ViewUtilities.setTextOfView(vitalityAgeSummary, dataItem.itemFeedbackSummary);
            } else {
                vitalityAgeSummary.setVisibility(View.GONE);
            }
            if (dataItem.itemFeedbackExplanation != null) {
                ViewUtilities.setTextOfView(vitalityAgeExplanation, dataItem.itemFeedbackExplanation);
            } else {
                vitalityAgeExplanation.setVisibility(View.GONE);
            }
            if (dataItem.itemVitalityAgeValue != null) {
                ViewUtilities.setTextOfView(vitalityAgeValue, dataItem.itemVitalityAgeValue);
                vitalityAgeValue.setVisibility(View.VISIBLE);
                vitalityAgeValuePlaceholder.setVisibility(View.GONE);
            } else if (dataItem.itemVitalityAgeValuePlaceHolder != null) {
                ViewUtilities.setTextOfView(vitalityAgeValuePlaceholder, dataItem.itemVitalityAgeValuePlaceHolder);
                vitalityAgeValuePlaceholder.setVisibility(View.VISIBLE);
                vitalityAgeValue.setVisibility(View.GONE);
            }
            vitalityAgeIcon.setImageResource(dataItem.summaryIconResourceId);
            vitalityAgeBackgroundIcon.setImageResource(dataItem.bgIconResource);
        }
        learnMoreButton.setOnClickListener(learnMoreOnClickListener);
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<VitalityAgeData,
            MyHealthVitalityAgeProfileViewHolder> {
        View.OnClickListener learnMoreOnClickListener;

        public Factory(View.OnClickListener onClickListener) {
            learnMoreOnClickListener = onClickListener;
        }

        @Override
        public MyHealthVitalityAgeProfileViewHolder createViewHolder(View itemView) {
            return new MyHealthVitalityAgeProfileViewHolder(itemView, learnMoreOnClickListener);
        }
    }

}