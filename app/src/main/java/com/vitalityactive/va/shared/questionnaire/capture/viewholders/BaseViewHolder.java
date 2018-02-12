package com.vitalityactive.va.shared.questionnaire.capture.viewholders;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.types.QuestionType;
import com.vitalityactive.va.shared.questionnaire.capture.QuestionnaireCapturePresenter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

abstract class BaseViewHolder extends GenericRecyclerViewAdapter.ViewHolder<Question> {
    protected final QuestionnaireCapturePresenter presenter;
    protected TextView title;
    private TextView details;
    private View footerLayout;
    private TextView footerText;
    private ImageView expandArrow;
    private Drawable upArrow;
    private Drawable downArrow;
    private ObjectAnimator expandAnimation;
    private ObjectAnimator collapseAnimation;
    private View prePopulationLayout;
    private TextView prePopulationText;
    private View prePopulationBottomMargin;
    private View.OnAttachStateChangeListener footerStateListener;
    private Context contextContainer;

    BaseViewHolder(View itemView, QuestionnaireCapturePresenter presenter) {
        super(itemView);
        this.contextContainer = itemView.getContext();
        this.presenter = presenter;

        assignViews();
        setArrowDrawables();
    }

    private void showExpandableArrowIfApplicable() {
        if (footerText.getLineCount() > 2) {
            expandArrow.setImageDrawable(downArrow);
            setAnimations(footerText.getLineCount());

            expandArrow.setVisibility(View.VISIBLE);
            footerText.setMaxLines(2);

            footerLayout.setOnClickListener(onFooterTextViewExpand());
        }
    }

    public Context getContext(){
        return contextContainer;
    }

    private void setAnimations(int lineCount) {
        expandAnimation = ObjectAnimator.ofInt(
                footerText,
                "maxLines",
                lineCount + 1).setDuration(50);

        collapseAnimation = ObjectAnimator.ofInt(
                footerText,
                "maxLines",
                2).setDuration(50);
    }

    private void assignViews() {
        title = (TextView) itemView.findViewById(R.id.vhr_question_text);
        details = (TextView) itemView.findViewById(R.id.vhr_question_detail);
        footerLayout = itemView.findViewById(R.id.vhr_question_footer);
        footerText = (TextView) itemView.findViewById(R.id.vhr_question_footer_text);
        expandArrow = (ImageView) itemView.findViewById(R.id.expand_arrow);
        prePopulationLayout = itemView.findViewById(R.id.vhr_question_pre_population_details);
        if (prePopulationLayout != null) {
            prePopulationText = (TextView) itemView.findViewById(R.id.vhr_question_pre_population_details_text);
            prePopulationBottomMargin = itemView.findViewById(R.id.vhr_question_pre_population_details_bottom_margin);
        }
    }

    private void setArrowDrawables() {
        downArrow = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_keyboard_arrow_down_white_24dp);
        upArrow = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_keyboard_arrow_up_white_24dp);

        ViewUtilities.tintDrawable(downArrow, ContextCompat.getColor(itemView.getContext(), R.color.light_disabled_38));
        ViewUtilities.tintDrawable(upArrow, ContextCompat.getColor(itemView.getContext(), R.color.light_disabled_38));
    }

    @Override
    public void bindWith(Question dataItem) {
        setVisibility(dataItem.getCanBeAnswered() && !dataItem.isChildQuestion(), dataItem.getQuestionType());
        setupPrePopulatedDetails(dataItem.isPrePopulatedAnswer(), dataItem.getPrePopulatedDate(), dataItem.getPrePopulatedSource());
        setupTitle(dataItem.getTitle(), !TextUtilities.isNullOrEmpty(dataItem.getTitle()));
        setupDetails(dataItem.getDetails(), !TextUtilities.isNullOrEmpty(dataItem.getDetails()));
        setupFooter(dataItem.getFooter(), !TextUtilities.isNullOrEmpty(dataItem.getFooter()));
        setupQuestion(dataItem);
    }

    @NonNull
    private View.OnClickListener onFooterTextViewExpand() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                footerLayout.setOnClickListener(onFooterTextViewCollapse());
                expandArrow.setImageDrawable(upArrow);

                expandAnimation.start();
            }
        };
    }

    @NonNull
    private View.OnClickListener onFooterTextViewCollapse() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                footerLayout.setOnClickListener(onFooterTextViewExpand());
                expandArrow.setImageDrawable(downArrow);

                collapseAnimation.start();
            }
        };
    }

    protected abstract void setupQuestion(Question question);

    private void setVisibility(boolean visible, QuestionType questionType) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        if (visible) {
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

            if (questionType != QuestionType.LABEL && questionType != QuestionType.SECTION_DESCRIPTION) {
                layoutParams.topMargin = ViewUtilities.pxFromDp(10);
            }
            itemView.setVisibility(View.VISIBLE);
        } else {
            layoutParams.height = 0;
            layoutParams.topMargin = ViewUtilities.pxFromDp(0);
            itemView.setVisibility(View.GONE);
        }
        itemView.setLayoutParams(layoutParams);
    }

    protected void clearIsPrePopulated() {
        setupPrePopulatedDetails(false, 0, 0);
    }

    private void setupPrePopulatedDetails(boolean isPrePopulatedAnswer, long date, int sourceKey) {
        hideFooterDividerWhenPrePopulationDetailsDisplayed(isPrePopulatedAnswer);
        if (prePopulationLayout == null) {
            return;
        }
        prePopulationLayout.setVisibility(isPrePopulatedAnswer ? View.VISIBLE : View.GONE);
        if (isPrePopulatedAnswer) {
            prePopulationText.setText(buildPrePopulatedEventSourceDetails(date, sourceKey));
        }
    }

    private void hideFooterDividerWhenPrePopulationDetailsDisplayed(boolean isPrePopulatedAnswer) {
        if(footerLayout != null){
            footerLayout.findViewById(R.id.divider).setVisibility(isPrePopulatedAnswer ? View.GONE : View.VISIBLE);
        }

    }

    private void hidePrePopulationBottomMarginWhenFooterIsVisible(boolean visible) {
        if (prePopulationBottomMargin != null) {
            prePopulationBottomMargin.setVisibility(visible ? View.GONE : View.INVISIBLE);
        }
    }

    private String buildPrePopulatedEventSourceDetails(long date, int sourceKey) {
        String formattedDate = DateFormattingUtilities.formatWeekdayDateMonthYear(itemView.getContext(), date);
        String source = mapEventKeyToHumanReadableSource(sourceKey);
        return getString(R.string.assessment_vhr_pre_population_details_570, source, formattedDate);
    }

    @NonNull
    private String mapEventKeyToHumanReadableSource(int sourceKey) {
        int id = mapEventKeyToStringId(sourceKey);
        if (id > 0) {
            return getString(id);

        }
        // fallback to empty string
        return "";
    }

    private
    @StringRes
    int mapEventKeyToStringId(int sourceKey) {
        switch (sourceKey) {
            case EventType._BMI:
            case EventType._HEIGHTCAPTURED:
            case EventType._WEIGHTCAPTURED:
            case EventType._WAISTCIRCUM:
            case EventType._BLOODPRESSURE:
            case EventType._DIASTOLICPRESSURE:
            case EventType._SYSTOLICPRESSURE:
            case EventType._FASTINGGLUCOSE:
            case EventType._RANDOMGLUCOSE:
            case EventType._HDLCHOLESTEROL:
            case EventType._LDLCHOLESTROL:
            case EventType._TOTCHOLESTEROL:
            case EventType._TRIGLYCCHOLESTEROL:
            case EventType._HBA1C:
            case EventType._URINARYTEST:
            case EventType._URINARYTESTASS:
            case EventType._LIPIDRATIOCALCULATED:
                return R.string.assessment_source_vhc_571;

            case EventType._DAILYMEALSCOMP:
            case EventType._FOODCHOICESCOMP:
            case EventType._LIFESTYLECHOICESCOMP:
                return R.string.assessment_source_vna_573;

            case EventType._GNRLHEALTHCOMP:
            case EventType._LIFESTYLECOMP:
            case EventType._SOCIALHABITSCOMP:
                return R.string.assessment_source_vhr_572;

            case EventType._DEVICEDATAUPLOAD:
            case EventType._DEVICESYNCING:
                return R.string.assessment_source_device_574;

            default:
                // unknown
                return 0;
        }
    }

    protected void setupTitle(String text, boolean visible) {
        this.title.setVisibility(visible ? View.VISIBLE : View.GONE);
        this.title.setText(text);
    }

    protected void setupDetails(String text, boolean visible) {
        if(this.details != null){
            this.details.setVisibility(visible ? View.VISIBLE : View.GONE);
            this.details.setText(text);
        }
    }

    protected void setupFooter(String text, boolean visible) {
        if (this.footerLayout == null || this.footerText == null)
            return;

        this.footerLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
        this.footerText.setText(text);

        hidePrePopulationBottomMarginWhenFooterIsVisible(visible);

        setupExpandableArrow(visible);
    }

    private void setupExpandableArrow(boolean visible) {
        if (visible) {
            footerText.removeOnAttachStateChangeListener(footerStateListener);
            footerText.addOnAttachStateChangeListener(footerStateListener = onAttachStateListener());
        }
    }

    private View.OnAttachStateChangeListener onAttachStateListener() {
        return new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                showExpansionAfterLayoutRendered(v);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                // no-op
            }
        };
    }

    private void showExpansionAfterLayoutRendered(View v) {
        v.post(new Runnable() {
            @Override
            public void run() {
                showExpandableArrowIfApplicable();
            }
        });
    }

    @NonNull
    protected String getString(@StringRes int resourceId) {
        return itemView.getResources().getString(resourceId);
    }

    protected String getString(@StringRes int resourceId, Object... formatArgs) {
        return itemView.getResources().getString(resourceId, formatArgs);
    }
}
