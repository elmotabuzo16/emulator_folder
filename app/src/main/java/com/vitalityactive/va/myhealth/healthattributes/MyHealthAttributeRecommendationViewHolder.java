package com.vitalityactive.va.myhealth.healthattributes;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.myhealth.content.FeedbackItem;
import com.vitalityactive.va.myhealth.content.HealthAttributeRecommendationItem;
import com.vitalityactive.va.myhealth.shared.MyHealthUtils;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.List;

public class MyHealthAttributeRecommendationViewHolder extends GenericRecyclerViewAdapter.ViewHolder<HealthAttributeRecommendationItem> {

    TextView attributeValueView;
    TextView feedbackNameView;
    TextView recommendedValueView;
    TextView recommendationTitleView;
    TextView sourceOriginView;
    TextView sourceDateView;
    ImageView expandMoreView;
    View feedbackPlaceHolder;
    View attributeSourceView;
    View recommendedResultLayout;
    View whyIsItImportantLayout;
    TextView recentResultView;
    ExpandableTextview expandableFeedbackContent;
    Context context;

    public MyHealthAttributeRecommendationViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        attributeValueView = itemView.findViewById(R.id.attribute_value);
        feedbackNameView = itemView.findViewById(R.id.feedback_name);
        recommendedValueView = itemView.findViewById(R.id.recommended_value);
        recommendationTitleView = itemView.findViewById(R.id.recommendation_title);
        sourceOriginView = itemView.findViewById(R.id.source_origin);
        sourceDateView = itemView.findViewById(R.id.source_date);
        expandMoreView = itemView.findViewById(R.id.expand_more);
        expandableFeedbackContent = itemView.findViewById(R.id.feedback_layout);
        feedbackPlaceHolder = itemView.findViewById(R.id.feedback_placeholder);
        recentResultView = itemView.findViewById(R.id.recent_result);
        attributeSourceView = itemView.findViewById(R.id.myhealth_feedback_attribute_source_section);
        recommendedResultLayout = itemView.findViewById(R.id.recommended_result_layout);
        whyIsItImportantLayout = itemView.findViewById(R.id.whyisitimportant_layout);
    }

    @Override
    public void bindWith(HealthAttributeRecommendationItem dataItem) {
        if (dataItem != null) {
            if (dataItem.getEventDateLogged() != null) {
                ViewUtilities.setTextOfView(sourceDateView, context.getString(R.string.summary_screen_date_tested_title_185, dataItem.getEventDateLogged()));
            }
            if (dataItem.getSource() != null) {
                ViewUtilities.setTextOfView(sourceOriginView, dataItem.getSource());
            } else {
                ViewUtilities.setTextOfView(sourceOriginView, context.getString(R.string.detail_screen_source_self_submitted_message_202));
            }
            HealthAttributeRecommendationItem.Recommendation displayRecommendation = dataItem.getDisplayRecommendation();
            if (displayRecommendation != null && displayRecommendation.getFriendlyValue() != null) {
                ViewUtilities.setTextOfView(recommendedValueView, displayRecommendation.getFriendlyValue());
                recommendedResultLayout.setVisibility(View.VISIBLE);
            } else {
                ViewUtilities.setTextOfView(recommendedValueView, context.getString(R.string.feedback_tips_no_data));
                recommendedResultLayout.setVisibility(View.GONE);
            }
            if (dataItem.getSectionTypeKey() != VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_UNKNOWN) {
                List<FeedbackItem> feedbacks = dataItem.getFeedbacks();
                if (feedbacks != null && feedbacks.size() > 0) {
                    FeedbackItem feedbackItem = MyHealthUtils.getDisplayableFeedback(feedbacks);
                    if (feedbackItem.getDisplayValue() != null) {
                        ViewUtilities.setTextOfView(attributeValueView, MyHealthUtils.attemptRoundingUp(feedbackItem.getDisplayValue()));
                    } else {
                        ViewUtilities.setTextOfView(attributeValueView, context.getString(R.string.feedback_tips_no_data));
                    }
                    if (feedbackItem.getFeedbackName() != null) {
                        ViewUtilities.setTextOfView(feedbackNameView, feedbackItem.getFeedbackName());
                    } else {
                        feedbackNameView.setVisibility(View.GONE);
                    }
                    if (feedbackItem.getWhyIsThisImportant() != null) {
                        expandableFeedbackContent.setText(feedbackItem.getWhyIsThisImportant());
                    } else {
                        whyIsItImportantLayout.setVisibility(View.GONE);
                    }
                } else {
                    emptyView();
                }
            } else {
                emptyView();
            }

        }

    }

    private void emptyView() {
        feedbackPlaceHolder.setVisibility(View.VISIBLE);
        attributeValueView.setVisibility(View.GONE);
        feedbackNameView.setVisibility(View.GONE);
        recentResultView.setText(context.getString(R.string.myhealth_feedback_attribute_capture_result));
        attributeSourceView.setVisibility(View.GONE);
        expandableFeedbackContent.setText("");
    }


    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<HealthAttributeRecommendationItem,
            MyHealthAttributeRecommendationViewHolder> {

        public Factory() {
        }

        @Override
        public MyHealthAttributeRecommendationViewHolder createViewHolder(View itemView) {
            return new MyHealthAttributeRecommendationViewHolder(itemView);
        }
    }

}
