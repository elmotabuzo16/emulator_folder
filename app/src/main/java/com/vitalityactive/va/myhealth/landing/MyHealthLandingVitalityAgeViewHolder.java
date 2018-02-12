package com.vitalityactive.va.myhealth.landing;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.myhealth.content.VitalityAgeData;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

public class MyHealthLandingVitalityAgeViewHolder extends GenericRecyclerViewAdapter.ViewHolder<VitalityAgeData> {
    final GenericRecyclerViewAdapter.OnItemClickListener<VitalityAgeData> onItemClickListener;
    TextView vitalityAgeSummary;
    TextView vitalityAgeExplanation;
    TextView vitalityAgeValue;
    TextView vitalityAgeValuePlaceholder;
    ImageView vitalityAgeIcon;
    View unknownVitalityAgeView;
    View subheaderView;
    ImageView vitalityAgeBackgroundIcon;

    Context context;

    public MyHealthLandingVitalityAgeViewHolder(View itemView, Context context, GenericRecyclerViewAdapter.OnItemClickListener<VitalityAgeData> onItemClickListener) {
        super(itemView);
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.vitalityAgeSummary = (TextView) itemView.findViewById(R.id.vitality_age_summary);
        this.vitalityAgeExplanation = (TextView) itemView.findViewById(R.id.vitality_age_explanation);
        this.vitalityAgeIcon = (ImageView) itemView.findViewById(R.id.va_status_icon);
        this.vitalityAgeValue = (TextView) itemView.findViewById(R.id.vitality_age_value);
        this.vitalityAgeValuePlaceholder = (TextView) itemView.findViewById(R.id.vitality_age_value_placeholder);
        this.unknownVitalityAgeView = itemView.findViewById(R.id.vitality_age_unknown_subheader);
        this.subheaderView = itemView.findViewById(R.id.vitality_age_subheader);
        this.vitalityAgeBackgroundIcon = (ImageView) itemView.findViewById(R.id.vitality_age_bg);

    }

    @Override
    public void bindWith(final VitalityAgeData dataItem) {
        if (dataItem != null) {
            if (dataItem.isUnknown) {
                unknownVitalityAgeView.setVisibility(View.VISIBLE);
                subheaderView.setVisibility(View.GONE);
                vitalityAgeExplanation.setVisibility(View.GONE);
                vitalityAgeValuePlaceholder.setVisibility(View.VISIBLE);
                ViewUtilities.setTextOfView(vitalityAgeValuePlaceholder, dataItem.itemVitalityAgeValuePlaceHolder);
                vitalityAgeValue.setVisibility(View.GONE);
            } else {
                unknownVitalityAgeView.setVisibility(View.GONE);
                subheaderView.setVisibility(View.VISIBLE);
                if (dataItem.itemFeedbackSummary != null) {
                    ViewUtilities.setTextOfView(vitalityAgeSummary, dataItem.itemFeedbackSummary);
                } else {
                    vitalityAgeSummary.setVisibility(View.GONE);
                }
                if (dataItem.itemFeedbackExplanation != null) {
                    vitalityAgeExplanation.setVisibility(View.VISIBLE);
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClicked(1, dataItem);
                }
            });
        }
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<VitalityAgeData,
            MyHealthLandingVitalityAgeViewHolder> {
        private final GenericRecyclerViewAdapter.OnItemClickListener<VitalityAgeData> onItemClickListener;
        Context context;

        Factory(Context context, GenericRecyclerViewAdapter.OnItemClickListener<VitalityAgeData> onItemClickListener) {
            this.context = context;
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public MyHealthLandingVitalityAgeViewHolder createViewHolder(View itemView) {
            return new MyHealthLandingVitalityAgeViewHolder(itemView, context, onItemClickListener);
        }
    }

}
