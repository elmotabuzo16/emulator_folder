package com.vitalityactive.va.myhealth.vitalityageprofile;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.myhealth.content.AttributeItem;
import com.vitalityactive.va.myhealth.content.FeedbackItem;
import com.vitalityactive.va.myhealth.content.FeedbackTip;
import com.vitalityactive.va.myhealth.content.MyHealthContent;
import com.vitalityactive.va.myhealth.content.SectionItem;
import com.vitalityactive.va.myhealth.shared.MyHealthUtils;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

public class MyHealthVitalityAgeTipsViewHolder extends GenericRecyclerViewAdapter.ViewHolder<SectionItem> {

    private final MyHealthFeedbackTipClickListener<SectionItem> onItemClickListener;
    TextView attributeTitleView;
    TextView attributeValueView;
    TextView feedbackNameView;
    TextView feedbackTipNameView;
    TextView feedbackTipValueView;
    TextView sectionTitleView;
    ImageView sectionIconView;
    TextView moreTipsButton;
    View feedbackContentView;
    View feedbackContentViewPlaceholder;
    View headerSection;
    View feedbackTipContentView;
    int globalTintColor;
    Context context;

    public MyHealthVitalityAgeTipsViewHolder(View itemView, MyHealthFeedbackTipClickListener<SectionItem> onItemClickListener, int globalTintColor) {
        super(itemView);
        context = itemView.getContext();
        this.onItemClickListener = onItemClickListener;
        sectionTitleView = itemView.findViewById(R.id.section_title);
        sectionIconView = itemView.findViewById(R.id.section_icon);
        attributeTitleView = itemView.findViewById(R.id.myhealth_vitalityage_profile_item1);
        attributeValueView = itemView.findViewById(R.id.myhealth_vitalityage_profile_item2);
        feedbackNameView = itemView.findViewById(R.id.myhealth_vitalityage_profile_feedback_name);
        feedbackTipNameView = itemView.findViewById(R.id.myhealth_vitalityage_whattodo_title);
        feedbackTipValueView = itemView.findViewById(R.id.myhealth_vitalityage_whattodo);
        feedbackContentView = itemView.findViewById(R.id.feedback_content_view);
        feedbackContentViewPlaceholder = itemView.findViewById(R.id.feedback_content_view_placeholder);
        headerSection = itemView.findViewById(R.id.header_section);
        moreTipsButton = itemView.findViewById(R.id.more_tips);
        feedbackTipContentView = itemView.findViewById(R.id.feedback_tip_content);
        this.globalTintColor = globalTintColor;
    }

    @Override
    public void bindWith(final SectionItem sectionItem) {
        if (sectionItem != null) {
            if (sectionItem.getSectionTitle() != null) {
                ViewUtilities.setTextOfView(sectionTitleView, sectionItem.getSectionTitle());
            } else {
                sectionTitleView.setVisibility(View.GONE);
            }
            if (sectionIconView != null && sectionItem.getSectionIcon() != 0) {
                sectionIconView.setImageResource(sectionItem.getSectionIcon());
            }
            if (sectionItem.getTintColor() != null) {
                sectionIconView.setColorFilter(Color.parseColor(sectionItem.getTintColor()));
            }
            if (sectionItem.getAttributeItems() != null && !sectionItem.getAttributeItems().isEmpty()) {
                final AttributeItem attributeItem = sectionItem.getAttributeItems().get(0);
                if (attributeItem.getAttributeTitle() != null) {
                    ViewUtilities.setTextOfView(attributeTitleView, attributeItem.getAttributeTitle());
                }
                if (attributeItem.getDisplayValue() != null) {
                    MyHealthUtils.setTextOrPlaceholder(attributeValueView, MyHealthUtils.attemptRoundingUp(attributeItem.getDisplayValue()), context.getString(R.string.feedback_tips_no_data));
                } else {
                    ViewUtilities.setTextOfView(attributeValueView, context.getString(R.string.feedback_tips_no_data));
                }
                if (attributeItem.getFeedbackItems() != null && !attributeItem.getFeedbackItems().isEmpty()) {
                    FeedbackItem feedbackItem = attributeItem.getFeedbackItems().get(0);
                    if (feedbackItem.getFeedbackName() != null && (sectionItem.getSectionTypekey() == VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_BAD)) {
                        ViewUtilities.setTextOfView(feedbackNameView, feedbackItem.getFeedbackName());
                    } else {
                        feedbackNameView.setVisibility(View.GONE);
                    }
                    if (attributeItem.getAttributeTitle() == null && attributeItem.getAttributeValue() == null && feedbackItem.getFeedbackName() == null) {
                        hideFeedbackTipsSection(sectionItem.getSectionTypekey());
                    }
                    if (!feedbackItem.getFeedbackTips().isEmpty()) {
                        FeedbackTip feedbackTip = feedbackItem.getFeedbackTips().get(0);
                        if (feedbackTipNameView != null) {
                            ViewUtilities.setTextOfView(feedbackTipNameView, feedbackTip.getFeedbackTipName());
                        }
                        if (feedbackTipValueView != null) {
                            ViewUtilities.setTextOfView(feedbackTipValueView, feedbackTip.getFeedbackTipNote());
                        }
                        if (feedbackTip.getFeedbackTipName() == null && feedbackTip.getFeedbackTipNote() == null) {
                            hideFeedbackTipSection();
                        }
                    } else {
                        hideFeedbackTipSection();
                    }
                } else {
                    hideFeedbackTipSection();
                }
                if (onItemClickListener != null) {
                    feedbackContentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onItemClickListener.onClicked(1, MyHealthVitalityAgeProfileActivity.ATTRIBUTE_DETAILS, sectionItem.getSectionTypekey(), attributeItem.getAttributeTypeKey());
                        }
                    });
                }

            } else {
                hideFeedbackTipsSection(sectionItem.getSectionTypekey());
            }
            moreTipsButton.setVisibility(sectionItem.hasSubsection() ? View.VISIBLE : View.GONE);
            if (onItemClickListener != null) {
                moreTipsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onClicked(1, MyHealthVitalityAgeProfileActivity.MORE_RESULTS_SECTION, sectionItem.getSectionTypekey(), -1);
                    }
                });

            }
        }
        setButtonTint(moreTipsButton);
    }

    private void setButtonTint(TextView moreTipsButton) {
        try {
            moreTipsButton.setTextColor(globalTintColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideFeedbackTipSection() {
        feedbackTipContentView.setVisibility(View.GONE);
    }

    private void hideFeedbackTipsSection(int sectionKey) {
        ViewUtilities.setTextOfView(feedbackContentViewPlaceholder, context.getString(MyHealthContent.VitalityAgeTipsFeedbackPlaceholder.getPlaceholder(sectionKey)));
        feedbackContentViewPlaceholder.setVisibility(View.VISIBLE);
        feedbackTipContentView.setVisibility(View.GONE);
        feedbackContentView.setVisibility(View.GONE);
        moreTipsButton.setVisibility(View.GONE);
        feedbackNameView.setVisibility(View.GONE);
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<SectionItem,
            MyHealthVitalityAgeTipsViewHolder> {
        MyHealthFeedbackTipClickListener<SectionItem> onItemClickListener;
        int globalTintColor;

        public Factory(MyHealthFeedbackTipClickListener<SectionItem> onItemClickListener, int globalTintColor) {
            this.globalTintColor = globalTintColor;
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public MyHealthVitalityAgeTipsViewHolder createViewHolder(View itemView) {
            return new MyHealthVitalityAgeTipsViewHolder(itemView, onItemClickListener, globalTintColor);
        }
    }

}

