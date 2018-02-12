package com.vitalityactive.va.shared.questionnaire.viewholder;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireSetInformation;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.circularprogressbardrawable.CircularProgressBarDrawable;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.Locale;

public class QuestionnaireLandingHeaderViewHolder extends GenericRecyclerViewAdapter.ViewHolder<QuestionnaireSetInformation> {
    private QuestionnaireLandingHeaderViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindWith(QuestionnaireSetInformation dataItem) {
        showMainDrawable(itemView, dataItem);
        setupTitleText(dataItem);
        setupSubtitleText(dataItem);
    }

    private void setupSubtitleText(QuestionnaireSetInformation dataItem) {
        final TextView subtitle = (TextView) itemView.findViewById(R.id.subtitle);
        subtitle.setText(dataItem.isCompleted() ? buildSubtitleString(dataItem.getTotalEarnedPoints()) : dataItem.getDescription());
    }

    private String buildSubtitleString(int totalEarnedPoints) {
        return String.format(itemView.getContext().getString(R.string.landing_header_description_completed_389), totalEarnedPoints);
    }

    private void setupTitleText(QuestionnaireSetInformation dataItem) {
        final TextView title = (TextView) itemView.findViewById(R.id.title);
        title.setText(buildTitleString(dataItem));
    }

    private String buildTitleString(QuestionnaireSetInformation dataItem) {
        String titleText;
        if (dataItem.isCompleted()) {
            titleText = itemView.getContext().getString(R.string.confirmation_completed_title_117);
        } else {
            titleText = String.format(Locale.getDefault(),
                    itemView.getContext().getString(R.string.home_card_card_earn_title_292),
                    dataItem.getTotalPotentialPoints());
        }
        return titleText;
    }

    private void showMainDrawable(View rootView, QuestionnaireSetInformation homeCardDTO) {
        if (homeCardDTO.isCompleted()) {
            int color = ViewUtilities.getColorPrimaryFromTheme(itemView);
            Drawable drawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.completed_main);
            final ImageView logo = (ImageView) rootView.findViewById(R.id.logo);
            logo.setColorFilter(color);
            logo.setImageDrawable(drawable);

            logo.setVisibility(View.VISIBLE);
        } else {
            setupCircularProgressBar(rootView, homeCardDTO);
        }
    }

    private void setupCircularProgressBar(View rootView, QuestionnaireSetInformation homeCardDTO) {
        final ProgressBar progressBar =
                (ProgressBar) rootView.findViewById(R.id.circular_progress_bar);
        final TextView progressBarTextView = (TextView) rootView.findViewById(R.id.circular_progress_bar_text);

        Drawable drawable = getProgressBarDrawable(rootView, progressBarTextView, homeCardDTO);

        progressBar.setProgressDrawable(drawable);
        progressBar.setVisibility(View.VISIBLE);
    }

    private Drawable getProgressBarDrawable(View rootView, TextView progressBarTextView, QuestionnaireSetInformation homeCardDTO) {
        RelativeLayout logoLayout = (RelativeLayout) rootView.findViewById(R.id.large_logo);
        final ViewGroup.LayoutParams layoutParams = logoLayout.getLayoutParams();

        return new CircularProgressBarDrawable.Builder()
                .setSize(layoutParams.width, layoutParams.height)
                .setFillStrokeWidth(CircularProgressBarDrawable.DEFAULT_STROKE_WIDTH)
                .setSegmentGapWidth(6)
                .setCompletedSegmentCount(homeCardDTO.totalCompleted())
                .setTotalSegmentCount(homeCardDTO.getTotalQuestionnaires())
                .setCompletedColor(ContextCompat.getColor(rootView.getContext(),
                        R.color.jungle_green))
                .setIncompleteColor(ContextCompat.getColor(rootView.getContext(),
                        R.color.light_divider_12))
                .setExtraText(progressBarTextView)
                .build();
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<QuestionnaireSetInformation,
            QuestionnaireLandingHeaderViewHolder> {

        @Override
        public QuestionnaireLandingHeaderViewHolder createViewHolder(View itemView) {
            return new QuestionnaireLandingHeaderViewHolder(itemView);
        }
    }
}
